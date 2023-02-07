package com.credit.oem.admin.modules.agent.entity;

import java.util.Date;

/**
 * 代理商账户
 */
public class AgentAccount {
    /**
     * 主键
     */
    private Long id;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 空号剩余条数
     */
    private Long emptyBalance;

    /**
     * 空号预警条数
     */
    private Long emptyWarnNumber;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getEmptyBalance() {
        return emptyBalance;
    }

    public void setEmptyBalance(Long emptyBalance) {
        this.emptyBalance = emptyBalance;
    }

    public Long getEmptyWarnNumber() {
        return emptyWarnNumber;
    }

    public void setEmptyWarnNumber(Long emptyWarnNumber) {
        this.emptyWarnNumber = emptyWarnNumber;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
