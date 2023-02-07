package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author zhangx
 * date  2018/8/2 11:32
 * 原表设计不符合规范，不能继承base
 */
@Repository
@Mapper
public interface MessageMapper {
    int insert(Message record);//自动生成的

    int insertSelective(Message record);//自动生成的

    Message selectMessageByCreateAndTitle(@Param("title") String title, @Param("createby") String createby);

    int insertBatch(List<Message> list);


    /**
     * 批量修改用户消息
     */
    int modifyByAgentMessageId(@Param("title") String title, @Param("message") String message,
                               @Param("agentId") Long agentId, @Param("agentMessageId") Long agentMessageId,
                               @Param("type") String type, @Param("updater") String updater);

    /**
     * 审核通过代理商消息
     */
    int approvedMessage(@Param("agentMessageId") Long agentMessageId);

    /**
     * 删除代理商消息
     */
    int deleteMessage(@Param("id") String id, @Param("agentMessageId") Long agentMessageId);

    /**
     * 获取用户手机列表
     */
    List<String> selectMobileListByAgentMessageId(@Param("agentMessageId") Long agentMessageId);

}
