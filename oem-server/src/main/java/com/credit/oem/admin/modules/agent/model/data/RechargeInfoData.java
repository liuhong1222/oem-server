package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;

/**
 * 充值信息
 *
 * @author chenzj
 * @since 2018/8/13
 */
public class RechargeInfoData {
   
	private Integer orderId;
	private String mobile;
	private BigDecimal money;
	private BigDecimal price;
	private Long number;
	private Long freeNumber;
	private String remark;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public Long getFreeNumber() {
		return freeNumber;
	}
	public void setFreeNumber(Long freeNumber) {
		this.freeNumber = freeNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
