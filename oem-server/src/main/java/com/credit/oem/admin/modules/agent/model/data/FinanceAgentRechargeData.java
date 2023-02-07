package com.credit.oem.admin.modules.agent.model.data;

import com.credit.oem.admin.common.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商充值记录
 *
 * @author chenzj
 * @since 2018/8/13
 */
public class FinanceAgentRechargeData {
    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 代理商id
     */
    @ExcelField(value = "代理商序号", order = 1)
    private Long agentId;

    /**
     * 代理商编号
     */
    private String agentNo;

    /**
     * 商户编号
     */
    @ExcelField(value = "商户编号", order = 2)
    private String mchId;

    /**
     * 代理商名称
     */
    @ExcelField(value = "代理商名称", order = 3)
    private String companyName;

    /**
     * 代理商手机号
     */
    @ExcelField(value = "代理商手机号", order = 4)
    private String agentMobile;

    /**
     * 充值时间
     */
    @ExcelField(value = "充值时间", order = 5)
    private Date payTime;

    /**
     * 订单编号
     */
    @ExcelField(value = "订单编号", order = 6)
    private String orderNo;

    /**
     * 代理级别名称
     */
    @ExcelField(value = "代理等级", order = 7)
    private String levelName;

    /**
     * 单价
     */
    @ExcelField(value = "单价(元/条)", order = 8)
    private BigDecimal price;

    /**
     * 条数
     */
    @ExcelField(value = "条数", order = 9)
    private Long number;

    /**
     * 金额
     */
    @ExcelField(value = "金额(元)", order = 10)
    private BigDecimal money;

    /**
     * 入账方式
     */
    private Integer payType;

    /**
     * 入账方式名称
     */
    @ExcelField(value = "方式", order = 11)
    private String payTypeName;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
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

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }
}
