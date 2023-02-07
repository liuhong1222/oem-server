package com.credit.oem.admin.modules.agent.model.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author chenzj
 * @since 2018/8/11
 */
public class AgentBasicLevelSaveParam {

    @ApiModelProperty("级别数")
    @NotNull(message = "级别数不能为空")
    private Integer level;

    @ApiModelProperty("级别名称")
    @NotBlank(message = "级别名称不能为空")
    private String name;

    @ApiModelProperty("单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty("预警条数")
    @NotNull(message = "预警条数不能为空")
    private Long emptyWarnNumber;


    @ApiModelProperty("最小充值金额")
    @NotNull(message = "最小充值金额不能为空")
    private BigDecimal minRecharge;

    @ApiModelProperty("最大充值金额")
    @NotNull(message = "最大充值金额不能为空")
    private BigDecimal maxRecharge;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEmptyWarnNumber() {
        return emptyWarnNumber;
    }

    public void setEmptyWarnNumber(Long emptyWarnNumber) {
        this.emptyWarnNumber = emptyWarnNumber;
    }

    public BigDecimal getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(BigDecimal minRecharge) {
        this.minRecharge = minRecharge;
    }

    public BigDecimal getMaxRecharge() {
        return maxRecharge;
    }

    public void setMaxRecharge(BigDecimal maxRecharge) {
        this.maxRecharge = maxRecharge;
    }
}
