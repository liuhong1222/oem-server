package com.credit.oem.admin.modules.agent.model.data;


import com.credit.oem.admin.common.annotation.ExcelField;

/**
 * 客户转代理商记录返回数据
 */
public class UserAgentChangeData {

    @ExcelField(value = "手机号码", order = 1)
    private String mobile;
    @ExcelField(value = "客户名称", order = 2)
    private String custName;
    @ExcelField(value = "注册时间", order = 3)
    private String registerTime;
    @ExcelField(value = "转入代理商", order = 4)
    private String inAgentName;
    @ExcelField(value = "转出代理商", order = 5)
    private String outAgentName;
    @ExcelField(value = "操作时间", order = 6)
    private String createTime;
    @ExcelField(value = "备注", order = 7)
    private String remark;
    private Integer creUserId;

    public Integer getCreUserId() {
        return creUserId;
    }

    public void setCreUserId(Integer creUserId) {
        this.creUserId = creUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getInAgentName() {
        return inAgentName;
    }

    public void setInAgentName(String inAgentName) {
        this.inAgentName = inAgentName;
    }

    public String getOutAgentName() {
        return outAgentName;
    }

    public void setOutAgentName(String outAgentName) {
        this.outAgentName = outAgentName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
