package com.credit.oem.admin.modules.agent.model.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 代理商充值
 * @author chenzj
 * @since 2018/8/13
 */
public class AgentRechargeParam {

    @ApiModelProperty("代理商id")
    @NotNull(message = "代理商id不能为空")
    private Long agentId;

    @ApiModelProperty("单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty("条数")
    @NotNull(message = "条数不能为空")
    private Long number;

    @ApiModelProperty("金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal money;

    @ApiModelProperty("入账类型")
    @NotNull(message = "入账类型不能为空")
    private Integer payType;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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
}
