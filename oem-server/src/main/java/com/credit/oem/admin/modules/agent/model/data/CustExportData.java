package com.credit.oem.admin.modules.agent.model.data;

import com.credit.oem.admin.common.annotation.ExcelField;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CustExportData
 * date     2018/8/20 17:25
 * description 导出表格时使用
 */
public class CustExportData {

    @ExcelField(value = "充值总条数", order = 8)
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ExcelField(value = "用户类型", order = 2)
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @ExcelField(value = "注册时间", order = 6)
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @ExcelField(value = "充值总计", order = 7)
    private String money;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    @ExcelField(value = "代理商名称", order = 5)
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @ExcelField(value = "手机号", order = 3)
    private String userPhone;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    private String id;

    @ExcelField(value = "客户名称", order = 4)
    private String custName;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @ExcelField(value = "客户编号", order = 1)
    private String creUserId;

    public String getCreUserId() {
        return creUserId;
    }

    public void setCreUserId(String creUserId) {
        this.creUserId = creUserId;
    }

    @ExcelField(value = "剩余条数", order = 9)
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
