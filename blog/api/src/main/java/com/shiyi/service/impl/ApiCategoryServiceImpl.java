package com.shiyi.service.impl;

import com.shiyi.common.ResponseResult;
import com.shiyi.mapper.CategoryMapper;
import com.shiyi.service.ApiCategoryService;
import com.shiyi.vo.ApiCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiCategoryServiceImpl implements ApiCategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 首页分类列表
     * @return
     */
    @Override
    public ResponseResult selectCategoryList() {
        List<ApiCategoryListVO> list = categoryMapper.selectApitCategoryList();
        return ResponseResult.success(list);
    }
}
