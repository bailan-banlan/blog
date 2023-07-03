package com.shiyi.service;

import com.shiyi.common.ResponseResult;

public interface ApiPhotoService {
    /**
     * 获取所有照片
     * @param photoAlbumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    ResponseResult selectListPhoto(Integer photoAlbumId, Integer pageNo, Integer pageSize);

}
