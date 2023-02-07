package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentTrdOrderId;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentTrdOrderIdMapper extends BaseCreditMapper<AgentTrdOrderId, Long> {

    List<AgentTrdOrderId> queryListByNotUpdated();

}
