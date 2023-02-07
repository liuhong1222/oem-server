package com.credit.oem.admin.modules.agent.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName("message")
public class Message {
    private String id;

    private String customerId;

    private Date createdate;

    private String title;

    private String type;

    private String isread;

    private Integer flag;

    private String createby;

    private Date createtime;

    private String lastrepair;

    private Date lasttime;

    private String message;

    private Long agentId;

    private Long agentMessageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread == null ? null : isread.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby == null ? null : createby.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getLastrepair() {
        return lastrepair;
    }

    public void setLastrepair(String lastrepair) {
        this.lastrepair = lastrepair == null ? null : lastrepair.trim();
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getAgentMessageId() {
        return agentMessageId;
    }

    public void setAgentMessageId(Long agentMessageId) {
        this.agentMessageId = agentMessageId;
    }
}
