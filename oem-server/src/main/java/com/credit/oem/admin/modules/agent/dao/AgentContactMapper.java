package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentContactMapper extends BaseCreditMapper<AgentContact, Long> {

    AgentContact queryOneByAgentId(Long agentId);

    /**
     * 根据手机号查询代理商列表
     */
    List<AgentContact> queryListByMobile(String mobile);

    int updateMobileByAgentId(@Param("agentId")Long agentId, @Param("mobile")String mobile);

}
