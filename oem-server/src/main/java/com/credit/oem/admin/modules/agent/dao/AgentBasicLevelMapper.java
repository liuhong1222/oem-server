package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentBasicLevel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentBasicLevelMapper extends BaseCreditMapper<AgentBasicLevel, Long> {

    List<AgentBasicLevel> selectListByNotDeleted();

    AgentBasicLevel queryOneByLevel(Integer level);

    void deleteOneById(Long id);

}
