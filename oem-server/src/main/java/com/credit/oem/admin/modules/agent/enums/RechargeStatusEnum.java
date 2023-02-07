package com.credit.oem.admin.modules.agent.enums;

/**
 * @author chenzj
 * @since 2018/8/13
 */
public enum RechargeStatusEnum {

    WAITING("0","待处理"),
    SUCCESS("1","成功"),
    FAILED("2","失败"),
    WAITCHECK("3","待审核"),
    REFUSED("4","已驳回"),
    ;
    private String status;
    private String descri;

    RechargeStatusEnum(String status, String descri) {
        this.setStatus(status);
        this.descri = descri;
    }

    public static RechargeStatusEnum getEnumByCode(String status) {
        if (status == null) {
            return null;
        }
        for (RechargeStatusEnum value : RechargeStatusEnum.values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public static String getDescri(String status){
        if (status != null && RechargeStatusEnum.getEnumByCode(status) != null) {
            return getEnumByCode(status).getDescri();
        } else {
            return "其他";
        }
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
