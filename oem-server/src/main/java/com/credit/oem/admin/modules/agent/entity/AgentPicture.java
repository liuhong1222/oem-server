package com.credit.oem.admin.modules.agent.entity;

import java.util.Date;

/**
 * 代理商系统图片
 */
public class AgentPicture {
    /**
     * 主键
     */
    private Long id;
    /**
     * 图片编号
     */
    private String picNo;
    /**
     * 图片路径
     */
    private String picPath;
    /**
     * 图片url
     */
    private String picUrl;
    /**
     * 图片用途类型
     */
    private Integer type;
    /**
     * 上传的管理员id
     */
    private Long sysUserId;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicNo() {
        return picNo;
    }

    public void setPicNo(String picNo) {
        this.picNo = picNo == null ? null : picNo.trim();
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? null : picPath.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
