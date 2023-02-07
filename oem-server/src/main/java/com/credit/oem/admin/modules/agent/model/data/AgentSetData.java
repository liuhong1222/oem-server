//package com.credit.oem.admin.modules.agent.model.data;
//
//import com.credit.oem.admin.common.annotation.ExcelTitle;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * 代理商查询返回数据
// *
// * @author chenzj
// * @since 2018/8/7
// */
//public class AgentSetData {
//
//    private String agentId;
//    private String agent_no;
//    private String mch_id;
//    private String company_name;
//    private String create_time;
//
//    private String icon_url;
//    private String logo_url;
//    private String sms_sign;
//
//    private String icp_record;
//    private String police_record;
//    private String licence;
//
//    private String hotline;
//    private String qq;
//    private String biz_no;
//
//    private String wxMchid;
//    private String wxcall_url;
//    private String wxcallback_url;
//    private String wxappid;
//    private String wxKey;
//
//    private String aliAppid;
//    private String alicallback_url;
//    private String alicall_url;
//    private String alipublic_key;
//    private String aliprivate_key;
//
//    private String htcompany_name;
//    private String htcompany_address;
//    private String htaccount_no;
//
//    private String htbank_name;
//    private String htpostcode;
//    private String htmobile;
//    private String sign_url;
//    private String seal_url;
//
//    public String getAgentId() {
//        return agentId;
//    }
//
//    public void setAgentId(String agentId) {
//        this.agentId = agentId;
//    }
//
//    public String getAgent_no() {
//        return agent_no;
//    }
//
//    public void setAgent_no(String agent_no) {
//        this.agent_no = agent_no;
//    }
//
//    public String getMch_id() {
//        return mch_id;
//    }
//
//    public void setMch_id(String mch_id) {
//        this.mch_id = mch_id;
//    }
//
//    public String getCompany_name() {
//        return company_name;
//    }
//
//    public void setCompany_name(String company_name) {
//        this.company_name = company_name;
//    }
//
//    public String getCreate_time() {
//        return create_time;
//    }
//
//    public void setCreate_time(String create_time) {
//        this.create_time = create_time;
//    }
//
//    public String getIcon_url() {
//        return icon_url;
//    }
//
//    public void setIcon_url(String icon_url) {
//        this.icon_url = icon_url;
//    }
//
//    public String getLogo_url() {
//        return logo_url;
//    }
//
//    public void setLogo_url(String logo_url) {
//        this.logo_url = logo_url;
//    }
//
//    public String getSms_sign() {
//        return sms_sign;
//    }
//
//    public void setSms_sign(String sms_sign) {
//        this.sms_sign = sms_sign;
//    }
//
//    public String getIcp_record() {
//        return icp_record;
//    }
//
//    public void setIcp_record(String icp_record) {
//        this.icp_record = icp_record;
//    }
//
//    public String getPolice_record() {
//        return police_record;
//    }
//
//    public void setPolice_record(String police_record) {
//        this.police_record = police_record;
//    }
//
//    public String getLicence() {
//        return licence;
//    }
//
//    public void setLicence(String licence) {
//        this.licence = licence;
//    }
//
//    public String getHotline() {
//        return hotline;
//    }
//
//    public void setHotline(String hotline) {
//        this.hotline = hotline;
//    }
//
//    public String getQq() {
//        return qq;
//    }
//
//    public void setQq(String qq) {
//        this.qq = qq;
//    }
//
//    public String getBiz_no() {
//        return biz_no;
//    }
//
//    public void setBiz_no(String biz_no) {
//        this.biz_no = biz_no;
//    }
//
//    public String getWxMchid() {
//        return wxMchid;
//    }
//
//    public void setWxMchid(String wxMchid) {
//        this.wxMchid = wxMchid;
//    }
//
//    public String getWxcall_url() {
//        return wxcall_url;
//    }
//
//    public void setWxcall_url(String wxcall_url) {
//        this.wxcall_url = wxcall_url;
//    }
//
//    public String getWxcallback_url() {
//        return wxcallback_url;
//    }
//
//    public void setWxcallback_url(String wxcallback_url) {
//        this.wxcallback_url = wxcallback_url;
//    }
//
//    public String getWxappid() {
//        return wxappid;
//    }
//
//    public void setWxappid(String wxappid) {
//        this.wxappid = wxappid;
//    }
//
//    public String getWxKey() {
//        return wxKey;
//    }
//
//    public void setWxKey(String wxKey) {
//        this.wxKey = wxKey;
//    }
//
//    public String getAliAppid() {
//        return aliAppid;
//    }
//
//    public void setAliAppid(String aliAppid) {
//        this.aliAppid = aliAppid;
//    }
//
//    public String getAlicallback_url() {
//        return alicallback_url;
//    }
//
//    public void setAlicallback_url(String alicallback_url) {
//        this.alicallback_url = alicallback_url;
//    }
//
//    public String getAlicall_url() {
//        return alicall_url;
//    }
//
//    public void setAlicall_url(String alicall_url) {
//        this.alicall_url = alicall_url;
//    }
//
//    public String getAlipublic_key() {
//        return alipublic_key;
//    }
//
//    public void setAlipublic_key(String alipublic_key) {
//        this.alipublic_key = alipublic_key;
//    }
//
//    public String getAliprivate_key() {
//        return aliprivate_key;
//    }
//
//    public void setAliprivate_key(String aliprivate_key) {
//        this.aliprivate_key = aliprivate_key;
//    }
//
//    public String getHtcompany_name() {
//        return htcompany_name;
//    }
//
//    public void setHtcompany_name(String htcompany_name) {
//        this.htcompany_name = htcompany_name;
//    }
//
//    public String getHtcompany_address() {
//        return htcompany_address;
//    }
//
//    public void setHtcompany_address(String htcompany_address) {
//        this.htcompany_address = htcompany_address;
//    }
//
//    public String getHtaccount_no() {
//        return htaccount_no;
//    }
//
//    public void setHtaccount_no(String htaccount_no) {
//        this.htaccount_no = htaccount_no;
//    }
//
//    public String getHtbank_name() {
//        return htbank_name;
//    }
//
//    public void setHtbank_name(String htbank_name) {
//        this.htbank_name = htbank_name;
//    }
//
//    public String getHtpostcode() {
//        return htpostcode;
//    }
//
//    public void setHtpostcode(String htpostcode) {
//        this.htpostcode = htpostcode;
//    }
//
//    public String getHtmobile() {
//        return htmobile;
//    }
//
//    public void setHtmobile(String htmobile) {
//        this.htmobile = htmobile;
//    }
//
//    public String getSign_url() {
//        return sign_url;
//    }
//
//    public void setSign_url(String sign_url) {
//        this.sign_url = sign_url;
//    }
//
//    public String getSeal_url() {
//        return seal_url;
//    }
//
//    public void setSeal_url(String seal_url) {
//        this.seal_url = seal_url;
//    }
//}
