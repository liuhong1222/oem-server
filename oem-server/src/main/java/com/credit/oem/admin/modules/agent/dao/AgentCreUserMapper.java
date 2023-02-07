package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentCreUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentCreUserMapper extends BaseCreditMapper<AgentCreUser, Long> {

    List<AgentCreUser> selectByCreUserId(Integer creUserId);

    int deleteByCreUserId(Integer creUserId);
    
    int saveAgentCreUser(AgentCreUser agentCreUser);
}
