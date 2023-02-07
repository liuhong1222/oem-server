package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentPackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentPackageMapper extends BaseCreditMapper<AgentPackage, Long> {

    List<AgentPackage> selectByAgentId(@Param("agentId") Long agentId);

    AgentPackage findOne(@Param("agentId") Long agentId);
    
    int insertByAgentId(@Param("agentId") Long agentId);
}
