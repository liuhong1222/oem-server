package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentLevel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentLevelMapper extends BaseCreditMapper<AgentLevel, Long> {

    AgentLevel queryOneByAgentId(Long agentId);

}
