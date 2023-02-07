package com.credit.oem.admin.modules.agent.model.data;

import com.credit.oem.admin.common.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商查询返回数据
 *
 * @author chenzj
 * @since 2018/8/7
 */
public class AgentInfoData {
    /**
     * 代理商id
     */
    private Long agentId;
    /**
     * 代理商编号 自增000001
     */
    @ExcelField(value = "代理商编号", order = 1)
    private String agentNo;
    /**
     * 商户编号
     */
    @ExcelField(value = "商户编号", order = 2)
    private String mchId;

    /**
     * 代理商公司名称
     */
    @ExcelField(value = "代理商名称", order = 3)
    private String companyName;

    /**
     * 代理商公司简称
     */
    @ExcelField(value = "代理商简称", order = 4)
    private String shortName;

    /**
     * 代理商状态
     */
    private Integer status;

    /**
     * 代理商状态名
     */
    @ExcelField(value = "代理商状态", order = 5)
    private String statusName;

    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间", order = 6)
    private Date createTime;

    /**
     * 能否升级
     */
    private Boolean canUpgrade;

    /**
     * 能否升级
     */
    @ExcelField(value = "能否升级", order = 7)
    private String canUpgradeName;

    /**
     * 代理等级数
     */
    private Long levelId;

    /**
     * 代理等级
     */
    @ExcelField(value = "代理等级", order = 8)
    private String levelName;

    /**
     * 充值总计(元)
     */
    @ExcelField(value = "充值总计(元)", order = 9)
    private BigDecimal totalRechargeMoney;

    /**
     * 充值总条数
     */
    @ExcelField(value = "充值总条数", order = 10)
    private Long totalRechargeNumber;

    /**
     * 剩余条数
     */
    @ExcelField(value = "剩余条数", order = 11)
    private Long emptyBalance;

    /**
     * 空号预警条数
     */
    @ExcelField(value = "预警条数", order = 12)
    private Long emptyWarnNumber;

    /**
     * 联系手机
     */
    @ExcelField(value = "联系手机", order = 13)
    private String mobile;

    /**
     * 单价
     */
    private BigDecimal price;


    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(Boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    public String getCanUpgradeName() {
        return canUpgradeName;
    }

    public void setCanUpgradeName(String canUpgradeName) {
        this.canUpgradeName = canUpgradeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getTotalRechargeMoney() {
        return totalRechargeMoney;
    }

    public void setTotalRechargeMoney(BigDecimal totalRechargeMoney) {
        this.totalRechargeMoney = totalRechargeMoney;
    }

    public Long getTotalRechargeNumber() {
        return totalRechargeNumber;
    }

    public void setTotalRechargeNumber(Long totalRechargeNumber) {
        this.totalRechargeNumber = totalRechargeNumber;
    }

    public Long getEmptyBalance() {
        return emptyBalance;
    }

    public void setEmptyBalance(Long emptyBalance) {
        this.emptyBalance = emptyBalance;
    }

    public Long getEmptyWarnNumber() {
        return emptyWarnNumber;
    }

    public void setEmptyWarnNumber(Long emptyWarnNumber) {
        this.emptyWarnNumber = emptyWarnNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
