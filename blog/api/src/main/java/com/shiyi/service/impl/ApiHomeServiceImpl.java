package com.shiyi.service.impl;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.WebConfig;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.CategoryMapper;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.mapper.WebConfigMapper;
import com.shiyi.service.ApiHomeService;
import com.shiyi.service.RedisService;
import com.shiyi.utils.IpUtil;
import com.shiyi.vo.ApiArticleListVO;
import com.shiyi.vo.ApiTagVO;
import com.shiyi.vo.SystemArticleListVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.shiyi.common.FieldConstants.LIMIT_ONE;

@Service
@RequiredArgsConstructor
public class ApiHomeServiceImpl implements ApiHomeService {

    private final RedisService redisService;

    private final WebConfigMapper webConfigMapper;

    private final ArticleMapper articleMapper;

    private final TagsMapper tagsMapper;

    private final CategoryMapper categoryMapper;

    private final static String ak = "f94be500c45148bc185be24a38c04ad3";

    private final static String sk = "27563ca627d5db0d57e831ca4de0f75f";

    /**
     * 添加访问量
     * @param request
     * @return
     */
    public ResponseResult report(HttpServletRequest request) {
        // 获取ip
        String ipAddress = IpUtil.getIp(request);
        // 获取访问设备
        UserAgent userAgent = IpUtil.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            // 访问量+1
            redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1);
            // 保存唯一标识
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        return ResponseResult.success();
    }

    /**
     * 获取首页数据
     * @return
     */
    public ResponseResult selectPubicData() {

        //获取首页轮播
        List<SystemArticleListVO> articles = articleMapper.selectListByBanner();

        //获取标签云
        List<ApiTagVO> tags = tagsMapper.selectListCountArticle();
        //最新文章
        List<ApiArticleListVO> apiArticleListVOS = articleMapper.selectUpToDateArticle();

        return ResponseResult.success().putExtra("articles",articles).putExtra("newArticleList",apiArticleListVOS).putExtra("tagCloud",tags);

    }

    /**
     * 获取网站相关信息
     * @return
     */
    public ResponseResult getWebSiteInfo() {
        //网站信息
        WebConfig webConfig = webConfigMapper.selectOne(new LambdaQueryWrapper<WebConfig>()
                .select(WebConfig::getAuthorAvatar,WebConfig::getIsMusicPlayer,WebConfig::getAuthorInfo,WebConfig::getTouristAvatar,WebConfig::getBulletin,
                        WebConfig::getQqNumber,WebConfig::getGitee,WebConfig::getGithub,WebConfig::getLogo,WebConfig::getWechat,
                        WebConfig::getAboutMe,WebConfig::getEmail,WebConfig::getShowList,WebConfig::getLoginTypeList,
                        WebConfig::getRecordNum,WebConfig::getAuthor,WebConfig::getAliPay,WebConfig::getWeixinPay,
                        WebConfig::getWebUrl, WebConfig::getSummary,WebConfig::getName,WebConfig::getKeyword)
                .last(LIMIT_ONE));

        //获取文章数
        Integer articleCount = articleMapper.selectCount(null);
        Integer categoryCount = categoryMapper.selectCount(null);
        Integer tagCount = tagsMapper.selectCount(null);

        //获取访问量
        Object count = redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        //获取访客量
        Long visitorAccess = redisService.getCacheSetKeyNumber(RedisConstants.UNIQUE_VISITOR);
        //热门文章
        List<ApiArticleListVO> hotArticles = articleMapper.selectHotArticleList();
        return ResponseResult.success()
                .putExtra("articleCount",articleCount).putExtra("categoryCount",categoryCount)
                .putExtra("tagCount",tagCount).putExtra("webConfig",webConfig).putExtra("siteAccess", Optional.ofNullable(count).orElse(0))
                .putExtra("visitorAccess",visitorAccess).putExtra("hotArticles",hotArticles);
    }

    /**
     * 获取各大平台的热搜
     * @param type 平台
     * @return
     */
    public ResponseResult hot(String type) {
        String url = "https://www.coderutil.com/api/resou/v1/" + type;
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access-key", ak);
        paramMap.put("secret-key", sk);
        String result= HttpUtil.get(url, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return ResponseResult.success(jsonObject);
    }


}
