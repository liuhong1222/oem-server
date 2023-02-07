package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentWebsite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentWebsiteMapper extends BaseCreditMapper<AgentWebsite, Long> {

    AgentWebsite selectByAgentId(@Param("agentId") Long agentId);

}
