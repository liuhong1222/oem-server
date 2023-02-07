package com.credit.oem.admin.modules.agent.model.param;


import java.io.Serializable;

/**
 * author zhangx
 * date  2018/5/21 17:01
 */
public class CustInfoParam extends PageParam implements Serializable {

    private static final long serialVersionUID = 4033496595333595777L;
    private String mobile;
    private String startTimeStr;
    private String endTimeStr;
    private String agentName;
    private String custName;
    /**-1,全部,0-个人  1-企业,2其他*/
    private String custType;
    /**1管理员，2代理商后台添加*/
    private Integer adminType;
    private Long sysUserId;
    private Long agentId;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getAdminType() {
        return adminType;
    }

    public void setAdminType(Integer adminType) {
        this.adminType = adminType;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }
}
