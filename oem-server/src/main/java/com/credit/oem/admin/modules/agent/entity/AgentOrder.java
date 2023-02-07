package com.credit.oem.admin.modules.agent.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商充值退款记录
 */
public class AgentOrder {
    /**
     * 主键
     */
    private Long id;

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 产品ID 目前默认 1：空号检测
     */
    private Long productId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 第三方支付订单号
     */
    private String tradeNo;

    /**
     * 操作类型 1 充值 2 退款
     */
    private Integer type;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 条数
     */
    private Long number;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 交易类型：1支付宝  2银联  3创蓝充值  4管理员充值  5对公转账  6赠送
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 状态：0待处理，1成功，2失败
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private Integer roleType;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }
}
