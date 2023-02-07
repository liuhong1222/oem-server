package com.credit.oem.admin.modules.agent.model.data;

import com.credit.oem.admin.common.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户充值记录
 *
 * @author chenzj
 * @since 2018/8/13
 */
public class FinanceUserRechargeData {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 充值时间
     */
    @ExcelField(value = "充值时间", order = 1)
    private Date payTime;
    /**
     * 客户id
     */
    @ExcelField(value = "客户编号", order = 2)
    private Long userId;
    /**
     * 客户名称
     */
    @ExcelField(value = "客户名称", order = 3)
    private String userName;
    /**
     * 客户手机号
     */
    @ExcelField(value = "手机号", order = 4)
    private String userMobile;
    /**
     * 代理商名称
     */
    @ExcelField(value = "代理商名称", order = 5)
    private String agentCompanyName;
    /**
     * 订单编号
     */
    @ExcelField(value = "订单编号", order = 6)
    private String orderNo;
    /**
     * 套餐名称
     */
    @ExcelField(value = "套餐", order = 7)
    private String packageName;
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
     * 赠送条数
     */
    @ExcelField(value = "赠送条数", order = 10)
    private Long freeNumber;
    /**
     * 金额
     */
    @ExcelField(value = "金额(元)", order = 11)
    private BigDecimal money;
    /**
     * 入账方式
     */
    private Integer payType;
    /**
     * 入账方式名称
     */
    @ExcelField(value = "方式", order = 12)
    private String payTypeName;
    /**
     * 代理商id
     */
    private Long agentId;
    
    /**
     * 支付状态
     */
    private String status;
    /**
     * 支付状态名称
     */
    @ExcelField(value = "审核状态", order = 13)
    private String statusName;
    /**
     * 备注
     */
    @ExcelField(value = "备注", order = 14)
    private String remark;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getAgentCompanyName() {
        return agentCompanyName;
    }

    public void setAgentCompanyName(String agentCompanyName) {
        this.agentCompanyName = agentCompanyName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getFreeNumber() {
		return freeNumber;
	}

	public void setFreeNumber(Long freeNumber) {
		this.freeNumber = freeNumber;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
