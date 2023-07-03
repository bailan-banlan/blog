package com.shiyi.controller;


import com.shiyi.common.ResponseResult;
import com.shiyi.service.impl.ApiHomeServiceImpl;
import com.shiyi.service.impl.HomeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Api(tags = "门户首页管理")
public class ApiHomeController {

    private final ApiHomeServiceImpl homeService;


    @RequestMapping(value = "/report",method = RequestMethod.GET)
    @ApiOperation(value = "增加访问量", httpMethod = "GET", response = ResponseResult.class, notes = "增加访问量")
    public ResponseResult report(HttpServletRequest request){
        return homeService.report(request);
    }

    @GetMapping("/webSiteInfo")
    @ApiOperation(value = "网站相关信息", httpMethod = "GET", response = ResponseResult.class, notes = "网站相关信息")
    public ResponseResult getWebSiteInfo(){
        return homeService.getWebSiteInfo();
    }

    @GetMapping("/")
    @ApiOperation(value = "首页共享数据", httpMethod = "GET", response = ResponseResult.class, notes = "首页共享数据")
    public ResponseResult selectHomeData(){
        return homeService.selectPubicData();
    }

    @GetMapping("/hot")
    @ApiOperation(value = "获取各大平台热搜", httpMethod = "GET", response = ResponseResult.class, notes = "获取各大平台热搜")
    public ResponseResult hot(String type){
        return homeService.hot(type);
    }

}

