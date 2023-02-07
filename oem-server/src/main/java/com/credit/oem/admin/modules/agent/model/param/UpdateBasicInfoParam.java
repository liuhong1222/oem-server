package com.credit.oem.admin.modules.agent.model.param;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName UpdateBasicInfoParam
 * author   zhangx
 * date     2018/8/14 15:21
 * description 设置里面的基本信息
 */
public class UpdateBasicInfoParam {
    private Long agentId;
    private String logo_url;
    private String icon_url;
    private String sign_url;
    private String seal_url;
    private String sms_sign;
    private String name;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getSign_url() {
        return sign_url;
    }

    public void setSign_url(String sign_url) {
        this.sign_url = sign_url;
    }

    public String getSeal_url() {
        return seal_url;
    }

    public void setSeal_url(String seal_url) {
        this.seal_url = seal_url;
    }

    public String getSms_sign() {
        return sms_sign;
    }

    public void setSms_sign(String sms_sign) {
        this.sms_sign = sms_sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
