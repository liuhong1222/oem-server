package com.credit.oem.admin.modules.agent.model.data;

import java.util.List;

/**
 * @author chenzj
 * @since 2018/10/16
 */
public class MessageInfoDetailData {

    /**
     * 消息id
     */
    private Long id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息类型名称
     */
    private String typeName;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 用户列表
     */
    private List<String> mobileList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public List<String> getMobileList() {
        return mobileList;
    }

    public void setMobileList(List<String> mobileList) {
        this.mobileList = mobileList;
    }
}
