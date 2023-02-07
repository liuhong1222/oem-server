package com.credit.oem.admin.modules.agent.model.param;

import org.apache.commons.lang.StringUtils;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName UserAgentChangeParam
 * author   zhangx
 * date     2018/10/11 9:41
 * description
 */
public class UserAgentChangeParam extends TimesParam {

    private String mobile;
    private String inAgentName;
    private String outAgentName;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public void initParam(){
        super.appendTimeString();
        if (StringUtils.isNotBlank(this.mobile)){
            this.mobile = "%"+mobile+"%";
        }
        if (StringUtils.isNotBlank(this.inAgentName)){
            this.inAgentName = "%"+inAgentName+"%";
        }
        if (StringUtils.isNotBlank(this.outAgentName)){
            this.outAgentName = "%"+outAgentName+"%";
        }
        if(this.getPageSize()==null){
            this.setPageSize(10);
        }
        if(this.getCurrentPage()==null){
            this.setCurrentPage(1);
        }
    }

}
