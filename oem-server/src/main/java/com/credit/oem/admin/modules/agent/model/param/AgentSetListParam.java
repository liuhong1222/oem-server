package com.credit.oem.admin.modules.agent.model.param;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName AgentSetListParam
 * author   zhangx
 * date     2018/8/14 13:44
 * description
 */
public class AgentSetListParam extends PageParam {

    private String startTimeStr;
    private String endTimeStr;
    private String agentName;
    private Integer auditState;
    private String agentMobile;

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

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }
}
