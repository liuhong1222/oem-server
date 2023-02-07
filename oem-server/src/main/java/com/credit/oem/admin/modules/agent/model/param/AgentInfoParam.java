package com.credit.oem.admin.modules.agent.model.param;

/**
 * 代理商查询参数
 * @author chenzj
 * @since 2018/8/7
 */
public class AgentInfoParam extends TimesParam {
    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 代理商名称
     */
    private String companyName;

    /**
     * 代理商状态  0-删除  1-正常
     */
    private Integer status;

    /**
     * 手机号
     */
    private String mobile;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
