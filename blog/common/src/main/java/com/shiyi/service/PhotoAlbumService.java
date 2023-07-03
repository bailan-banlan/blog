package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.PhotoAlbum;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    ResponseResult listAlbum(String name);

    ResponseResult getAlbumById(Integer id);

    ResponseResult insertAlbum(PhotoAlbum photoAlbum);

    ResponseResult updateAlbum(PhotoAlbum photoAlbum);

    ResponseResult deleteAlbumById(Integer id);

}
