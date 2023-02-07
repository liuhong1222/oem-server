package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentWeixinpay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentWeixinpayMapper extends BaseCreditMapper<AgentWeixinpay, Long> {

    AgentWeixinpay selectByAgentId(@Param("agentId") Long agentId);

}
