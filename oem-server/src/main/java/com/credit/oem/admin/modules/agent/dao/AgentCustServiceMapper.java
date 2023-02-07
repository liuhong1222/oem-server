package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentCustService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentCustServiceMapper extends BaseCreditMapper<AgentCustService, Long> {
    AgentCustService selectByAgentId(@Param("agentId") Long agentId);

    List<AgentCustService> selectByMeiqiaEntid(@Param("meiqiaEntid") String meiqiaEntid);


}
