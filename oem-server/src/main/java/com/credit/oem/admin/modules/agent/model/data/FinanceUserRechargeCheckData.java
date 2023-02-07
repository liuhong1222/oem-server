package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入账审核列表
 *
 * @author chenzj
 * @since 2018/8/13
 */
public class FinanceUserRechargeCheckData {

    private Integer orderId;
    private Date createTime;
    private String  userId;
    private String userPhone;
    private String agentCompanyName;
    private BigDecimal money;
    private String remark;
    private String status;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getAgentCompanyName() {
		return agentCompanyName;
	}
	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
}
