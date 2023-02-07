package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.Agent;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoData;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentMapper extends BaseCreditMapper<Agent, Long> {
    /**
     * 查询代理商列表
     */
    List<AgentInfoData> queryAgentInfoList(AgentInfoParam param);

    /**
     * 根据公司名称查询代理商
     */
    Agent queryOneByCompanyName(String companyName);

    /**
     * 根据商户编号查询代理商
     */
    Agent queryOneByMchId(String mchId);

    /**
     * 根据营业执照编号查询代理商
     */
    Agent queryOneByLicenseNo(String licenseNo);

    /**
     * 更新代理商编号
     */
    void updateAgentNoById(@Param("id") Long id, @Param("agentNo") String agentNo);

    /**
     * 更新代理商状态
     */
    void updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新代理商删除状态
     */
    void updateDeleteStatusById(@Param("id") Long id, @Param("status") Integer status);

    Agent selectAgentBySysUserId(@Param("sysUserId") Long sysUserId);

    Long countCreUserByAgentId(@Param("agentId") Long agentId);

    Long countAgent();

    /**
     * 更新代理商审核状态
     */
    int updateAuditStateById(@Param("id") Long id, @Param("auditState") Integer auditState);

    /**
     * 根据客户id查询代理商
     */
    Agent queryOneByCreUserId(Integer creUserId);

    /**
     * 模糊查询公司名称
     */
    List<String> queryCompanyNameList(String companyName);

}
