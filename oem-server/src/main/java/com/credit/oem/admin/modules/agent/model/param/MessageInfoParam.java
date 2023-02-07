package com.credit.oem.admin.modules.agent.model.param;

/**
 * 消息列表查询参数
 *
 * @author chenzj
 * @since 2018/10/16
 */
public class MessageInfoParam extends PageParam {
    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 代理商名称
     */
    private String agentName;

    /**
     * 审核状态 0-发布待审核  1-已审核 2-修改待审核  3-已驳回  -1删除
     */
    private Integer auditState;

    /**
     * 代理商手机号
     */
    private String agentMobile;

    /**
     * 消息类型 1-系统消息 2-活动通知 3-故障通知 4-更新通知
     */
    private String type;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
