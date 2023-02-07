package com.credit.oem.admin.modules.agent.model.data;

import java.math.BigDecimal;

/**
 * @author chenzj
 * @since 2018/8/10
 */
public class AgentInfoDetailData {
    /**
     * 代理商id
     */
    private Long agentId;
    /**
     * 代理商名称
     */
    private String companyName;

    /**
     * 代理商名称
     */
    private String shortName;

    /**
     * 营业执照图片Url
     */
    private String licenseUrl;

    /**
     * 代理商编号
     */
    private String agentNo;

    /**
     * 商户编号
     */
    private String mchId;

    /**
     * 营业执照地址
     */
    private String address;

    /**
     * 法人姓名
     */
    private String legalPerson;

    /**
     * 营业执照编号
     */
    private String licenseNo;

    /**
     * 营业期限生效日期
     */
    private String effectDate;

    /**
     * 营业期限到期日期
     */
    private String expireDate;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职务
     */
    private String position;

    /**
     * 代理商等级
     */
    private Long levelId;

    /**
     * 单价
     */
    private BigDecimal price;


    /**
     * 空号预警条数
     */
    private Long emptyWarnNumber;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getEmptyWarnNumber() {
        return emptyWarnNumber;
    }

    public void setEmptyWarnNumber(Long emptyWarnNumber) {
        this.emptyWarnNumber = emptyWarnNumber;
    }
}
