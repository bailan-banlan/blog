package com.shiyi.service.impl;

import com.shiyi.common.ResponseResult;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.service.ApiTagService;
import com.shiyi.vo.ApiTagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiTagServiceImpl implements ApiTagService {

    private final TagsMapper tagsMapper;

    /**
     *  标签列表
     * @return
     */
    @Override
    public ResponseResult selectTagList() {
        List<ApiTagVO> tags = tagsMapper.selectListCountArticle();
        return ResponseResult.success(tags);
    }
}
