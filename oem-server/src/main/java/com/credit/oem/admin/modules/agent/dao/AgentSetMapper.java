package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.model.param.AgentSetListParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AgentSetMapper {

    List<Map> agentSetList(AgentSetListParam param);

    List<Map> findBasicInfo(@Param("agentId") Long agentId);
}
