package com.credit.oem.admin.modules.agent.model.param;

import javax.validation.constraints.NotBlank;

/**
 * 新闻列表查询参数
 *
 * @author chenzj
 * @since 2018/10/8
 */
public class NewsSaveParam {
    /**
     * 新闻id
     */
    private String newsId;

    /**
     * 新闻标题
     */
    @NotBlank(message = "新闻标题不能为空")
    private String title;

    /**
     * 新闻内容带标签
     */
    @NotBlank(message = "新闻内容不能为空")
    private String message;
    
    /**
     * 新闻内容不带标签
     */
    @NotBlank(message = "新闻内容不能为空")
    private String newsContent;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
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

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	
}
