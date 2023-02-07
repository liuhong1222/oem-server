package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentWeixinpay;
import com.credit.oem.admin.modules.agent.entity.AgentWxLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface AgentWxLoginMapper extends BaseCreditMapper<AgentWxLogin, Long> {

    AgentWxLogin selectByAgentId(@Param("agentId") Long agentId);

}