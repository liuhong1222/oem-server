package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;

/**
 * 代理商级别查询返回参数
 * @author chenzj
 * @since 2018/8/11
 */
public class AgentBasicLevelData {
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
        this.name = name;
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

    public void setEmptyWarnNumber(Long emptyWarnNumber) {
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
}
