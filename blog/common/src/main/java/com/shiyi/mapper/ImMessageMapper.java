package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.entity.ImMessage;
import com.shiyi.vo.ImMessageVO;
import com.shiyi.vo.ImOnlineUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface ImMessageMapper extends BaseMapper<ImMessage> {

    /**
     * 获取历史聊天记录
     * @return
     */
    Page<ImMessageVO> selectPublicHistoryList(Page<ImMessageVO> imMessageVOPage );

    /**
     * 获取在线用户列表
     */
    List<ImOnlineUserVO> selectPublicOnlineUserList(@Param("keys") Set<String> keys);

    /**
     * 获取单聊历史聊天记录
     * @return
     */
    Page<ImMessageVO> selectPublicUserImHistoryList(@Param("page")Page<ImMessageVO> imMessageVOPage,
                                              @Param("fromUserId") String fromUserId,@Param("toUserId")String toUserId);

}
