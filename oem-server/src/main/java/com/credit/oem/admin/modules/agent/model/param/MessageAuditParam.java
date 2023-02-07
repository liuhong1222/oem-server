package com.credit.oem.admin.modules.agent.model.param;

import javax.validation.constraints.NotNull;

/**
 * 消息审核参数
 *
 * @author chenzj
 * @since 2018/10/10
 */
public class MessageAuditParam {
    /**
     * 消息id
     */
    @NotNull(message = "消息id不能为空")
    private Long agentMessageId;

    /**
     * 审核状态 0-发布待审核  1-已审核 2-修改待审核  3-已驳回  -1删除
     */
    @NotNull(message = "审核状态不能为空")
    private Integer auditState;

    /**
     * 审核备注信息
     */
    private String auditRemark;

    public Long getAgentMessageId() {
        return agentMessageId;
    }

    public void setAgentMessageId(Long agentMessageId) {
        this.agentMessageId = agentMessageId;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }
}
