package com.credit.oem.admin.modules.agent.model.param;

import java.math.BigDecimal;

/**
 * 充值参数
 * author zhangx
 * date  2018/8/31 10:31
 */
public class CustRefundParam {
    /**
     * 用户id
     */
    private Integer creUserId;
    private BigDecimal price;
    private Integer number;
    private BigDecimal amount;
    private String remark;
    /**'1 充值 2 退款'*/
    private String type;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
}
