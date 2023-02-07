package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.modules.agent.entity.AgentSysUser;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserUpdateParam;
import com.github.pagehelper.PageInfo;

/**
 * @author chenzj
 * @since 2018/8/17
 */
public interface AgentSysUserService {

    PageInfo list(AgentSysUserParam param, Long sysUserId);

    void saveAgentSysUser(AgentSysUserSaveParam param, Long sysUserId);

    void updateAgentSysUser(AgentSysUserUpdateParam param, Long sysUserId);

    void deleteAgentSysUser(Long deleteSysUserId, Long sysUserId);

    /**
     * 查找并验证代理商主账号信息
     */
    AgentSysUser findAndVerifyMasterAgentSysUser(Long sysUserId);
}
