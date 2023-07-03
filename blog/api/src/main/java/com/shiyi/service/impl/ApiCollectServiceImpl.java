package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Collect;
import com.shiyi.mapper.CollectMapper;
import com.shiyi.service.ApiCollectService;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.ApiArticleInfoVO;
import com.shiyi.vo.ApiArticleListVO;
import com.shiyi.vo.ApiCollectListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiCollectServiceImpl implements ApiCollectService {

    private final CollectMapper collectMapper;

    /**
     * 我的收藏列表
     * @return
     */
    @Override
    public ResponseResult selectCollectList() {
        Page<ApiCollectListVO> result = collectMapper.selectCollectList(new Page<ApiCollectListVO>(PageUtils.getPageNo(),PageUtils.getPageSize()),StpUtil.getLoginIdAsString());
        return ResponseResult.success(result);
    }

    /**
     * 收藏文章
     * @param articleId 文章id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult collect(Integer articleId) {
        Collect collect = Collect.builder().userId(StpUtil.getLoginIdAsString()).articleId(articleId).build();
        collectMapper.insert(collect);
        return ResponseResult.success();
    }

    /**
     * 取消收藏
     * @param articleId 文章id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult cancel(Integer articleId) {
        collectMapper.delete(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId,StpUtil.getLoginIdAsString()).eq(Collect::getArticleId,articleId));
        return ResponseResult.success();
    }
}
