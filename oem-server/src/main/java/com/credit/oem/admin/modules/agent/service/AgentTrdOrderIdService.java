package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.modules.agent.entity.AgentTrdOrderId;

/**
 * @author chenzj
 * @since 2018/8/15
 */
public interface AgentTrdOrderIdService {
    void updateInfo();

    void updateAgentTrdOrderIdInfo(AgentTrdOrderId originAgentTrdOrderId);

    void insertFromTrdorder();
}
