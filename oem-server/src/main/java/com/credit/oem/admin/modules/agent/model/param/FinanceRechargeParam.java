package com.credit.oem.admin.modules.agent.model.param;

/**
 * 财务管理查询条件
 * @author chenzj
 * @since 2018/8/13
 */
public class FinanceRechargeParam extends TimesParam {

    /**
     * 代理商id
     */
    private Long agentId;

    /**
     * 代理商名称
     */
    private String companyName;

    /**
     * 交易类型：1支付宝  2银联  3创蓝充值  4管理员充值  5对公转账  6赠送
     */
    private Integer payType;
    
    /**
     * 支付状态：0待处理 1成功  2 失败  3待审核 4 驳回
     */
    private Integer payStatus;

    /**
     * 客户名称
     */
    private String userName;

    /**
     * 客户手机号
     */
    private String userMobile;

    /**
     * 代理商手机号
     */
    private String agentMobile;

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getAgentMobile() {
        return agentMobile;
    }

    public void setAgentMobile(String agentMobile) {
        this.agentMobile = agentMobile;
    }

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
}
