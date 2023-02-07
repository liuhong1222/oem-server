package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentAlipay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentAlipayMapper extends BaseCreditMapper<AgentAlipay, Long> {

    AgentAlipay selectByAgentId(@Param("agentId") Long agentId);

}
