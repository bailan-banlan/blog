package com.shiyi.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.ImMessage;
import com.shiyi.mapper.ImMessageMapper;
import com.shiyi.service.ApiImMessageService;
import com.shiyi.service.ImMessageService;
import com.shiyi.utils.BeanCopyUtils;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.ImMessageVO;
import com.shiyi.vo.ImOnlineUserVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Service
@RequiredArgsConstructor
public class ApiImMessageServiceImpl  implements ApiImMessageService {

    private final ImMessageMapper imMessageMapper;

    @Override
    public ResponseResult selectHistoryList() {
        Page<ImMessageVO> page = imMessageMapper.selectPublicHistoryList(new Page<>(PageUtils.getPageNo(),
                        PageUtils.getPageSize()));
        return ResponseResult.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrInsert(ImMessageVO obj) {
        ImMessage imMessage = BeanCopyUtils.copyObject(obj, ImMessage.class);
        //撤回消息
        if (obj.getIsWithdraw() == 1) {
            imMessageMapper.updateById(imMessage);
        }else {
            //保存消息到数据库
            imMessageMapper.insert(imMessage);
        }
    }

    @Override
    public List<ImOnlineUserVO> selectOnlineUserList(Set<String> keys) {
        return imMessageMapper.selectPublicOnlineUserList(keys);
    }

    @Override
    public ResponseResult selectUserImHistoryList(String fromUserId, String toUserId) {
        Page<ImMessageVO> page = imMessageMapper.selectPublicUserImHistoryList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
                fromUserId,toUserId);
        return ResponseResult.success(page);
    }
}
