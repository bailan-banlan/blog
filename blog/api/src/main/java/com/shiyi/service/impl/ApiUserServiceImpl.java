package com.shiyi.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.dto.EmailLoginDTO;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.entity.User;
import com.shiyi.entity.UserInfo;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.enums.UserStatusEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.UserInfoMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.ApiUserService;
import com.shiyi.service.RedisService;
import com.shiyi.service.WebConfigService;
import com.shiyi.utils.*;
import com.shiyi.vo.UserInfoVO;
import com.shiyi.vo.WxUserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shiyi.common.ResultCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiUserServiceImpl implements ApiUserService {

    private final UserMapper userMapper;

    private final RedisService redisService;

    private final HttpServletRequest request;

    private final UserInfoMapper userInfoMapper;


    /**
     * 邮箱登录
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseResult emailLogin(EmailLoginDTO vo) {
        if (vo.getEmail().contains("test")) {
            throw new BusinessException("演示账号只能登录后台管理系统！");
        }
        User user = userMapper.selectNameAndPassword(vo.getEmail(), AesEncryptUtils.aesEncrypt(vo.getPassword()));
        if (user == null) {
            throw new BusinessException(ERROR_PASSWORD.desc);
        }

        if (user.getStatus() == UserStatusEnum.disable.code) {
            throw new BusinessException(EMAIL_DISABLE_LOGIN.desc);
        }

        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());

        //七天有效时间  登录
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));

        //组装数据
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .avatar(userInfo.getAvatar()).nickname(userInfo.getNickname())
                .intro(userInfo.getIntro()).webSite(userInfo.getWebSite()).email(user.getUsername())
                .loginType(user.getLoginType()).token(StpUtil.getTokenValue()).build();

        return ResponseResult.success(userInfoVO);
    }

    /**
     * 判断用户是否微信登录成功
     *
     * @param loginCode 用户临时id
     * @return
     */
    @Override
    public ResponseResult wxIsLogin(String loginCode) {
        UserInfoVO user = (UserInfoVO) redisService.getCacheObject(RedisConstants.WX_LOGIN_USER + loginCode);
        if (user == null) {
            return ResponseResult.error("用户未被授权");
        }
        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        user.setToken(StpUtil.getTokenValue());
        return ResponseResult.success(user);
    }

    /**
     * 微信扫码公众号登录
     * @param message
     * @return
     */
    @Override
    public String wechatLogin(WxMpXmlMessage message) {
        String content = message.getContent().toUpperCase();
        //先判断登录码是否已过期
        boolean loginFlag = redisService.hasKey(RedisConstants.WX_LOGIN_USER_STATUE + content);
        if (!loginFlag) {
            return "验证码已过期";
        }
        UserInfoVO userInfoVO = userMapper.selectByUserName(message.getFromUser());
        if (userInfoVO == null) {
            String ip = IpUtil.getIp(request);
            String ipSource = IpUtil.getIp2region(ip);

            // 保存用户信息
            UserInfo userInfo = UserInfo.builder()
                    .nickname("WECHAT-" + RandomUtils.generationCapital(6))
                    .avatar("http://img.shiyit.com/66bb121d47e94b89945d29bb6e3e6cab.jpg")
                    .build();
            userInfoMapper.insert(userInfo);
            // 保存账号信息
            User user = User.builder()
                    .userInfoId(userInfo.getId())
                    .username(message.getFromUser())
                    .password(AesEncryptUtils.aesEncrypt(message.getFromUser()))
                    .loginType(LoginTypeEnum.WECHAT.getType())
                    .lastLoginTime(DateUtil.getNowDate())
                    .ipAddress(ip)
                    .ipSource(ipSource)
                    .roleId(2)
                    .build();
            userMapper.insert(user);
            //组装返回信息
            userInfoVO = UserInfoVO.builder().id(user.getId()).loginType(user.getLoginType())
                    .avatar(userInfo.getAvatar()).email(userInfo.getEmail()).nickname(userInfo.getNickname())
                    .intro(userInfo.getIntro()).webSite(userInfo.getWebSite()).build();
        }


        //修改redis缓存 以便监听是否已经授权成功
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER + content, userInfoVO, 60, TimeUnit.SECONDS);
        return "网站登录成功！(若页面长时间未跳转请刷新验证码)";
    }

    /**
     * 获取微信公众号登录验证码
     *
     * @return
     */
    @Override
    public ResponseResult getWechatLoginCode() {
        String code = "DL" + RandomUtils.generationNumber(4);
        redisService.setCacheObject(RedisConstants.WX_LOGIN_USER_STATUE + code, false, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public ResponseResult selectUserInfo() {
        UserInfo userInfo = userInfoMapper.getByUserId(StpUtil.getLoginIdAsString());
        return ResponseResult.success(userInfo);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(UserInfoDTO vo) {
        UserInfo userInfo = BeanCopyUtils.copyObject(vo, UserInfo.class);
        int update = userInfoMapper.updateById(userInfo);
        return update > 0 ? ResponseResult.success("修改信息成功") : ResponseResult.error(ERROR_DEFAULT.getDesc());
    }

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult selectUserInfoByToken(String token) {
        log.info("根据token获取信息请求中，{}", token);
        Object userId = StpUtil.getLoginIdByToken(token);
        if (userId == null) {
            throw new BusinessException("无效的token");
        } else {
            User user = userMapper.selectById(userId.toString());
            UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
            UserInfoVO userInfoVO = UserInfoVO.builder().nickname(userInfo.getNickname())
                    .avatar(userInfo.getAvatar()).intro(userInfo.getIntro()).id(user.getId())
                    .build();

            return ResponseResult.success(userInfoVO);
        }
    }

    /**
     * 第三方登录授权之后的逻辑
     *
     * @param response
     * @param source
     * @param request
     * @param httpServletResponse
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        String result = com.alibaba.fastjson.JSONObject.toJSONString(response.getData());
        log.info("第三方登录验证结果:{}", result);

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
        Object uuid = jsonObject.get("uuid");
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getIp2region(ipAddress);
        // 判断是否已注册
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            // 更新登录信息
            userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));

            //更新头像和昵称
            userInfoMapper.update(new UserInfo(), new LambdaUpdateWrapper<UserInfo>()
                    .set(UserInfo::getAvatar, jsonObject.get("avatar"))
                    .set(UserInfo::getNickname, jsonObject.get("nickname"))
                    .eq(UserInfo::getId, user.getUserInfoId()));

        } else {
            // 获取第三方用户信息，保存到数据库返回
            // 保存用户信息
            UserInfo userInfo = UserInfo.builder()
                    .nickname(source.equals("github") ? jsonObject.get("username").toString() : jsonObject.get("nickname").toString())
                    .avatar(jsonObject.get("avatar").toString())
                    .build();
            userInfoMapper.insert(userInfo);
            // 保存账号信息
            user = User.builder()
                    .userInfoId(userInfo.getId())
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(com.shiyi.utils.DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.normal.getCode())
                    .build();
            userMapper.insert(user);
        }

        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect("http://www.shiyit.com/?token=" + StpUtil.getTokenValue());
    }

    /**
     * 检查是否是一次登录
     *
     * @param wxMaUserInfo
     * @param request
     * @return
     */
    private UserInfoVO checkIsRegister(WxUserInfoVO.WxMaUserInfo wxMaUserInfo, HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        String ipSource = IpUtil.getIp2region(ip);

        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(wxMaUserInfo.getNickName())
                .avatar(wxMaUserInfo.getAvatarUrl())
                .build();
        userInfoMapper.insert(userInfo);
        // 保存账号信息
        User user = User.builder()
                .userInfoId(userInfo.getId())
                .username(wxMaUserInfo.getOpenId())
                .password(AesEncryptUtils.aesEncrypt(wxMaUserInfo.getOpenId()))
                .loginType(LoginTypeEnum.WECHAT.getType())
                .lastLoginTime(DateUtil.getNowDate())
                .ipAddress(ip)
                .ipSource(ipSource)
                .roleId(2)
                .build();
        userMapper.insert(user);
        //组装返回信息
        return UserInfoVO.builder().id(user.getId()).loginType(user.getLoginType())
                .avatar(userInfo.getAvatar()).email(userInfo.getEmail()).nickname(userInfo.getNickname())
                .intro(userInfo.getIntro()).webSite(userInfo.getWebSite()).build();
    }
}
