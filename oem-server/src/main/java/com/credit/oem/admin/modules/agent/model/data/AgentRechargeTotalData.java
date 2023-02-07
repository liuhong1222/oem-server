package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;

/**
 * 代理商总充值信息
 * @author chenzj
 * @since 2018/8/13
 */
public class AgentRechargeTotalData {

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 充值总计(元)
     */
    private BigDecimal totalRechargeMoney;

    /**
     * 充值总条数
     */
    private Long totalRechargeNumber;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getTotalRechargeMoney() {
        return totalRechargeMoney;
    }

    public void setTotalRechargeMoney(BigDecimal totalRechargeMoney) {
        this.totalRechargeMoney = totalRechargeMoney;
    }

    public Long getTotalRechargeNumber() {
        return totalRechargeNumber;
    }

    public void setTotalRechargeNumber(Long totalRechargeNumber) {
        this.totalRechargeNumber = totalRechargeNumber;
    }
}
