package com.shiyi.service;

import com.shiyi.common.ResponseResult;

public interface ApiPhotoAlbumService {
    //web端方法开始

    /**
     * 获取所有相册
     * @return
     */
   public ResponseResult selectPhotoAlbumList();

}
