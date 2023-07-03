package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.PhotoAlbum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.mapper.PhotoAlbumMapper;
import com.shiyi.service.ApiPhotoAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiPhotoAlbumServiceImpl implements ApiPhotoAlbumService {

    private final PhotoAlbumMapper photoAlbumMapper;

    /**
     * 相册列表
     * @return
     */
    @Override
    public ResponseResult selectPhotoAlbumList() {
        List<PhotoAlbum> photoAlbums = photoAlbumMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .select(PhotoAlbum::getId, PhotoAlbum::getName).eq(PhotoAlbum::getStatus, YesOrNoEnum.NO.getCode()));
        return ResponseResult.success(photoAlbums);
    }
}
