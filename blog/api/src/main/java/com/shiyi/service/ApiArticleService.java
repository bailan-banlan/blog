package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.dto.ArticleInsertDTO;

public interface ApiArticleService {

    /**
     * 首页分页获取文章
     * @return
     */
    public ResponseResult selectArticleList(Integer categoryId, Integer tagId);

    /**
     * 首页获取文章详情
     * @param id 文章id
     * @return
     */
    public ResponseResult selectArticleInfo(Integer id);


    /**
     * 搜索文章
     * @param keywords 搜索关键词
     * @return
     */
    public ResponseResult searchArticle(String keywords);


    /**
     * 文章归档
     * @return
     */
    public ResponseResult archive();

    /**
     * 文章点赞
     * @param articleId 文章id
     * @return
     */
    public ResponseResult articleLike(Integer articleId);

    /**
     * 校验秘钥
     * @param code 验证码
     * @return
     */
    public ResponseResult checkCode(String code);

    /**
     * 用户添加文章
     * @param dto
     * @return
     */
    public ResponseResult insertArticle(ArticleInsertDTO dto);

    /**
     * 查询我的文章
     * @return
     */
    public ResponseResult selectMyArticle();

    /**
     * 删除我的文章
     * @param id
     * @return
     */
    ResponseResult deleteMyArticle(Long id);

    /**
     * 获取我的文章详情
     * @param id
     * @return
     */
    ResponseResult selectMyArticleInfo(Long id);

}
