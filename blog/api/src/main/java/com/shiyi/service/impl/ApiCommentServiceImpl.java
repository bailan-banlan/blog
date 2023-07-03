package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Comment;
import com.shiyi.entity.UserInfo;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.CommentMapper;
import com.shiyi.mapper.UserInfoMapper;
import com.shiyi.service.ApiCommentService;
import com.shiyi.utils.HTMLUtils;
import com.shiyi.utils.IpUtil;
import com.shiyi.vo.ApiCommentListVO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ApiCommentServiceImpl implements ApiCommentService {

    private final CommentMapper commentMapper;

    private final UserInfoMapper userInfoMapper;

    private final HttpServletRequest request;

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertComment(Comment comment) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        //获取ip地址
        String ipAddress = IpUtil.getIp2region(IpUtil.getIp(request));
        String os = userAgent.getOperatingSystem().getName();
        if (os.contains("mac") || os.contains("Mac")) {
            comment.setSystem("mac");
        } else if (os.contains("Windows")) {
            comment.setSystem("windowns");
        }else {
            comment.setSystem("android");
        }
        //过滤内容 如删除html标签和敏感词替换
        String content = HTMLUtils.deleteTag(comment.getContent());
        comment.setContent(content);
        comment.setSystemVersion(os);
        comment.setIpAddress(ipAddress);
        comment.setUserId(StpUtil.getLoginIdAsString());
        int insert = commentMapper.insert(comment);
        if (insert == 0){
            throw new BusinessException("评论失败");
        }
        return ResponseResult.success(comment);
    }

    @Override
    public ResponseResult selectCommentByArticleId(int pageNo, int pageSize, Long articleId) {
        //获取评论父级评论
        Page<ApiCommentListVO> pageList = commentMapper.selectCommentPage(new Page<ApiCommentListVO>(pageNo,pageSize),articleId);
        //获取子级
        for (ApiCommentListVO vo : pageList.getRecords()) {
            List<Comment> commentList = commentMapper.selectList(
                    new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, vo.getId()).orderByDesc(Comment::getCreateTime));
            for (Comment e : commentList) {
                UserInfo replyUserInfo = userInfoMapper.getByUserId(e.getReplyUserId());
                UserInfo userInfo1 = userInfoMapper.getByUserId(e.getUserId());
                ApiCommentListVO apiCommentListVO = ApiCommentListVO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .replyUserId(e.getReplyUserId())
                        .nickname(userInfo1.getNickname())
                        .webSite(userInfo1.getWebSite())
                        .replyNickname(replyUserInfo.getNickname())
                        .replyWebSite(replyUserInfo.getWebSite())
                        .content(e.getContent())
                        .avatar(userInfo1.getAvatar())
                        .createTime(e.getCreateTime())
                        .browser(e.getBrowser())
                        .browserVersion(e.getBrowserVersion())
                        .system(e.getSystem())
                        .systemVersion(e.getSystemVersion())
                        .ipAddress(e.getIpAddress())
                        .build();
                vo.getChildren().add(apiCommentListVO);
            }
        }
        return ResponseResult.success(pageList);
    }
}
