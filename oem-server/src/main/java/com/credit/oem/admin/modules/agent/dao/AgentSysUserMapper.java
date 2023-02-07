package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentSysUser;
import com.credit.oem.admin.modules.agent.model.data.AgentSysUserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgentSysUserMapper extends BaseCreditMapper<AgentSysUser, Long> {

    /**
     * 根据代理商id 获取代理商和管理员关联关系记录列表
     * @param AgentId
     * @return
     */
    List<AgentSysUser> queryListByAgentId(Long AgentId);

    /**
     * 根据代理商id 获取主账号信息
     * @param AgentId
     * @return
     */
    AgentSysUser queryMasterSysUserByAgentId(Long AgentId);

    /**
     * 根据管理员userId 获取代理商和管理员关联关系记录列表
     */
    AgentSysUser queryOneBySysUserId(Long sysUserId);

    /**
     * 根据userId列表获取代理商账号信息
     */
    List<AgentSysUserData> queryAgentSysUserDataListByMobileAndUserIdList(@Param("mobile") String mobile,@Param("sysUserIdList") List<Long> sysUserIdList);


}
