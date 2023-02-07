package com.credit.oem.admin.modules.agent.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础代理商等级
 */
public class AgentBasicLevel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 级别数
     */
    private Integer level;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 空号预警条数
     */
    private Long emptyWarnNumber;

    /**
     * 最小充值金额
     */
    private BigDecimal minRecharge;

    /**
     * 最大充值金额
     */
    private BigDecimal maxRecharge;

    /**
     * 状态 0-删除  1-正常
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEmptyWarnNumber() {
        return emptyWarnNumber;
    }

    public void setEmptyWarnNumbe(Long emptyWarnNumber) {
        this.emptyWarnNumber = emptyWarnNumber;
    }

    public BigDecimal getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(BigDecimal minRecharge) {
        this.minRecharge = minRecharge;
    }

    public BigDecimal getMaxRecharge() {
        return maxRecharge;
    }

    public void setMaxRecharge(BigDecimal maxRecharge) {
        this.maxRecharge = maxRecharge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
