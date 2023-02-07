package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.UpdatePackageParam;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserService
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
public interface AgentDeskService {

    R getAgentDeskInfo(Long agentId, Long sysUserId);

    R updateWarnNumber(Long agentId, Long warnNumber);

    R updateMobile(Long sysUserId, String mobile);

    R findMobile(Long sysUserId);

    R updateMail(Long sysUserId, String mail);

    R findAgentPackage(Long agentId);

    R updateAgentPackage(Long agentId, UpdatePackageParam param);

    R getAdminDeskInfo();


}
