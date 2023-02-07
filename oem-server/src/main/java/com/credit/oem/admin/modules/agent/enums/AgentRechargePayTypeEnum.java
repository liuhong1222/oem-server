package com.credit.oem.admin.modules.agent.enums;

/**
 * @author chenzj
 * @since 2018/8/13
 */
public enum AgentRechargePayTypeEnum {
    ALIPAY(1,"支付宝"),
    UNIONPAY(2,"银联"),
    CHUANGLAN(3,"创蓝充值"),
    ADMIN(4,"管理员充值"),
    CORPORATE_ACCOUNT(5,"对公转账"),
    PRESENT(6,"赠送"),
    USER_RECHAGE_ONLINE(7,"官网充值扣款"),
    USER_REFUND(8,"用户退款"),

    ;
    private Integer code;
    private String descri;

    AgentRechargePayTypeEnum(Integer code, String descri) {
        this.code = code;
        this.descri = descri;
    }

    public static AgentRechargePayTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AgentRechargePayTypeEnum value : AgentRechargePayTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public static String getDescri(Integer code){
        if (code != null && AgentRechargePayTypeEnum.getEnumByCode(code) != null) {
            return getEnumByCode(code).getDescri();
        } else {
            return "其他";
        }
    }

}
