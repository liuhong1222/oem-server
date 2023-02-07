package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentMessage;
import com.credit.oem.admin.modules.agent.model.data.MessageInfoData;
import com.credit.oem.admin.modules.agent.model.param.MessageInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentMessageMapper extends BaseCreditMapper<AgentMessage, Long> {

    /**
     * 查询代理商消息列表
     */
    List<MessageInfoData> queryAgentMessageList(MessageInfoParam param);

    /**
     * 审核代理商消息
     */
    int auditAgentMessage(@Param("id") Long id, @Param("auditSysUserId") Long auditSysUserId,
                          @Param("auditState") Integer auditState, @Param("auditRemark") String auditRemark);

    /**
     * 修改代理商消息
     */
    int modifyAgentMessage(@Param("title") String title, @Param("message") String message,
                           @Param("id") Long id, @Param("auditState") Integer auditState,
                           @Param("type") String type, @Param("updater") String updater);

    /**
     * 删除代理商消息
     */
    int deleteAgentMessage(@Param("id") Long id, @Param("agentId") Long agentId);

    /**
     * 数秒内最近的代理商消息
     */
    long countByCreatorWithinSeconds(@Param("creator") String creator, @Param("seconds") Long seconds);

}
