package com.shiyi.controller;


import com.shiyi.common.ResponseResult;
import com.shiyi.service.ApiPhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("v1/photoAlbum")
@Api(tags = "相册接口-API")
@RequiredArgsConstructor
public class ApiPhotoAlbumController {

    private final ApiPhotoAlbumService albumService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ResponseResult.class, notes = "相册列表")
    public ResponseResult selectPhotoAlbumList(){
        return albumService.selectPhotoAlbumList();
    }

}

