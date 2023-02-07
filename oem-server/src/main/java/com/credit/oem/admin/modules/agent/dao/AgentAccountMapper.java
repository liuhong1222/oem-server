package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentAccountMapper extends BaseCreditMapper<AgentAccount, Long> {

    AgentAccount queryOneByAgentId(Long agentId);

    /**
     * 更新账户剩余条数
     */
    int addEmptyBalanceByAgentId(@Param("agentId") Long agentId, @Param("addNumber") Long addNumber);

    int addEmptyBalanceByAgentIdAndVersion(@Param("agentId") Long agentId, @Param("addNumber") Long addNumber,@Param("version") Integer version);

    int subEmptyBalanceByAgentId(@Param("agentId") Long agentId, @Param("subNumber") Long subNumber);
}
