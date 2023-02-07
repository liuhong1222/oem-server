package com.credit.oem.admin.modules.agent.model.param;

import java.math.BigDecimal;

/**
 * 充值参数
 * author zhangx
 * date  2018/8/31 10:31
 */
public class CustRechargeParam {
    /**
     * 用户id
     */
    private Integer creUserId;
    private Long agentPackageId;
    /**
     * 对应数据库中的money,单价或套餐价
     */
    private BigDecimal price;
    private Long number;
    private Long freeNumber;

    /**5支付宝，6对公转账，7赠送，8退款*/
    private String payType;
    /**
     * 总额
     */
    private BigDecimal amount;
    private String remark;
    /**'1 充值 2 退款'*/
    private String type;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getAgentPackageId() {
        return agentPackageId;
    }

    public void setAgentPackageId(Long agentPackageId) {
        this.agentPackageId = agentPackageId;
    }

    public Integer getCreUserId() {
        return creUserId;
    }

    public void setCreUserId(Integer creUserId) {
        this.creUserId = creUserId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public Long getFreeNumber() {
		return freeNumber;
	}

	public void setFreeNumber(Long freeNumber) {
		this.freeNumber = freeNumber;
	}
}
