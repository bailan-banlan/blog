package com.shiyi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Photo;
import com.shiyi.mapper.PhotoMapper;
import com.shiyi.service.ApiPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiPhotoServiceImpl implements ApiPhotoService {

    private final PhotoMapper photoMapper;

    /**
     * 获取所有照片
     * @param photoAlbumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectListPhoto(Integer photoAlbumId, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<Photo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (photoAlbumId != null) {
            objectLambdaQueryWrapper.eq(Photo::getAlbumId, photoAlbumId);
        }
        Page<Photo> photoPage = photoMapper.selectPage(new Page<>(pageNo, pageSize),objectLambdaQueryWrapper);
        return ResponseResult.success(photoPage);
    }
}
