package com.credit.oem.admin.modules.agent.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商套餐
 */
public class AgentPackage {
    /**
     * 主键
     */
    private Long id;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 产品id   1空号检查
     */
    private Long productId;

    /**
     * 套餐id
     */
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 条数
     */
    private Integer number;

    /**
     * 实际价格
     */
    private BigDecimal money;

    /**
     * 流水号前缀
     */
    private String orderCode;

    /**
     * 打折=discout/100
     */
    private BigDecimal discout;

    /**
     * 原价
     */
    private BigDecimal originalCost;

    /**
     * 排序
     */
    private Integer orderNo;

    /**
     * 逻辑删除标志（1：已删 0：未删）
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public BigDecimal getDiscout() {
        return discout;
    }

    public void setDiscout(BigDecimal discout) {
        this.discout = discout;
    }

    public BigDecimal getOriginalCost() {
        return originalCost;
    }

    public void setOriginalCost(BigDecimal originalCost) {
        this.originalCost = originalCost;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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
