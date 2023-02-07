package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.model.param.AgentSetListParam;
import com.credit.oem.admin.modules.agent.model.param.UpdateBasicInfoParam;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserService
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
public interface AgentSetService {

    R agentSetList(AgentSetListParam param);

    R delAgent(Long agentId);

    R findBasicInfo(Long agentId);

    R updateBasicInfo(UpdateBasicInfoParam param);

    R findDomainInfo(Long agentId);

    R updateDomainInfo(AgentDomain param);

    R findCustService(Long agentId);

    R updateCustService(AgentCustService param);

    R findContract(Long agentId);

    R updateContract(AgentContract param);

    R findAlipay(Long agentId);

    R updateAlipay(AgentAlipay param);

    R findWeixinpay(Long agentId);

    R updateWeixinpay(AgentWeixinpay param,Long sysUserId);

    R auditApproved(Long agentId,Long sysUserId);

    R auditRejected(Long agentId,Long sysUserId,String remark);

    R auditState(Long agentId);

    R findWxLogin(Long agentId);

    R updateWxLogin(AgentWxLogin param);



}
