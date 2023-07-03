package com.shiyi.service;

import com.shiyi.common.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface ApiHomeService {

    /**
     * 添加访问量
     * @param request
     * @return
     */
    public ResponseResult report(HttpServletRequest request);

    /**
     * 获取首页数据
     * @return
     */
    public ResponseResult selectPubicData();

    /**
     * 获取网站相关信息
     * @return
     */
    public ResponseResult getWebSiteInfo();
}
