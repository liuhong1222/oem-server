package com.credit.oem.admin.modules.agent.model.param;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新建代理商参数
 * @author chenzj
 * @since 2018/8/10
 */
public class AgentInfoSaveParam {

    @ApiModelProperty("代理商名称")
    @NotBlank(message = "代理商名称不能为空")
    private String companyName;

    @ApiModelProperty("代理商简称")
    @NotBlank(message = "代理商简称不能为空")
    private String shortName;

    private String licensePicNo;

    @ApiModelProperty("商户编号")
    @NotBlank(message = "商户编号不能为空")
    private String mchId;

    @ApiModelProperty("营业执照地址")
    @NotBlank(message = "营业执照地址不能为空")
    private String address;

    @ApiModelProperty("法人姓名")
    @NotBlank(message = "法人姓名不能为空")
    private String legalPerson;

    @ApiModelProperty("营业执照编号")
    @NotBlank(message = "营业执照编号不能为空")
    private String licenseNo;

    @ApiModelProperty("营业期限生效日期")
    @NotBlank(message = "营业期限生效日期不能为空")
    private String effectDate;

    @ApiModelProperty("营业期限到期日期")
    @NotBlank(message = "营业期限到期日期不能为空")
    private String expireDate;

    @ApiModelProperty("联系人")
    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @ApiModelProperty("联系人手机号码")
    @NotBlank(message = "联系人手机号码不能为空")
    private String mobile;

    @ApiModelProperty("邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty("职务")
    @NotBlank(message = "职务不能为空")
    private String position;

    private String password;

    @ApiModelProperty("代理商等级")
    @NotNull(message = "代理商等级不能为空")
    private Long levelId;

    @ApiModelProperty("单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @ApiModelProperty("预警条数")
    @NotNull(message = "预警条数不能为空")
    private Long emptyWarnNumber;



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

    public String getLicensePicNo() {
        return licensePicNo;
    }

    public void setLicensePicNo(String licensePicNo) {
        this.licensePicNo = licensePicNo;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
