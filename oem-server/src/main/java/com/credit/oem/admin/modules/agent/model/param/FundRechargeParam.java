package com.credit.oem.admin.modules.agent.model.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName FundRechargeParam
 * author   zhangx
 * date     2018/8/17 14:30
 * description 代理商通过支付宝充值
 */
public class FundRechargeParam {

    @ApiModelProperty("单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty("万条数")
    @NotNull(message = "条数不能为空")
    private BigDecimal number;

    @ApiModelProperty("万元金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal money;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}

