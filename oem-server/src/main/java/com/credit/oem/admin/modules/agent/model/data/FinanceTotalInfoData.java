package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;

/**
 * 财务管理，合计信息
 * @author chenzj
 * @since 2018/9/6
 */
public class FinanceTotalInfoData {

    /**
     * 条数
     */
    private Long number;

    /**
     * 金额
     */
    private BigDecimal money;

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
}
