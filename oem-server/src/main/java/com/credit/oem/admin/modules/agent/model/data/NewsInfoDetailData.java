package com.credit.oem.admin.modules.agent.model.data;

/**
 * @author chenzj
 * @since 2018/10/8
 */
public class NewsInfoDetailData {

    /**
     * 新闻id
     */
    private String id;


    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻内容
     */
    private String message;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
