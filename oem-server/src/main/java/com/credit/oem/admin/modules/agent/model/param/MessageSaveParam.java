package com.credit.oem.admin.modules.agent.model.param;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 消息列表查询参数
 *
 * @author chenzj
 * @since 2018/10/8
 */
public class MessageSaveParam {
    //选择类型 1全部
    public final static String SELECT_TYPE_ALL = "1";

    /**
     * 消息id
     */
    private Long agentMessageId;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    private String title;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息内容不能为空")
    private String message;

    /**
     * 消息类型 1-系统消息 2-活动通知 3-故障通知 4-更新通知
     */
    @NotBlank(message = "消息类型不能为空")
    private String type;

    /**
     * 选择类型 1全部，2 部分用户
     */
    private String selectType;

    /**
     * 用户列表
     */
    private String userIdList;


    public Long getAgentMessageId() {
        return agentMessageId;
    }

    public void setAgentMessageId(Long agentMessageId) {
        this.agentMessageId = agentMessageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType == null ? null : selectType.trim();
    }

    public String getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(String userIdList) {
        this.userIdList = userIdList;
    }
}
