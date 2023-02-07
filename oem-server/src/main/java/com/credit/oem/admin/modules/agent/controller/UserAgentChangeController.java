package com.credit.oem.admin.modules.agent.controller;


import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.ExcelExportUtil;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoData;
import com.credit.oem.admin.modules.agent.model.data.UserAgentChangeData;
import com.credit.oem.admin.modules.agent.model.param.UserAgentChangeParam;
import com.credit.oem.admin.modules.agent.service.UserAgentChangeService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * copyright (C), 2018-2018, 创蓝253
 *
 * @author zhangx
 * @fileName ManagerController
 * @description tt
 */
@RestController
@RequestMapping("/open/agent/user")
public class UserAgentChangeController extends AbstractController {

    @Autowired
    UserAgentChangeService userAgentChangeService;

    @SysLog("转换代理商")
    @RequiresPermissions("open:agent:user:changeAgent")
    @RepeatCommitToken
    @RequestMapping(value = "/changeAgent", method = RequestMethod.POST)
    public R changeAgent(Integer creUserId, String outAgentName, String inAgentName, String remark) {
        return userAgentChangeService.changeAgent(creUserId, outAgentName, inAgentName, remark);
    }


    @RequiresPermissions("open:agent:user:changeAgentList")
    @RequestMapping(value = "/changeAgentList", method = RequestMethod.POST)
    public R changeAgentList(UserAgentChangeParam param) {
        param.initParam();
        return userAgentChangeService.changeAgentList(param);
    }

    @RequiresPermissions("open:agent:user:changeAgentListExport")
    @RequestMapping(value = "/changeAgentListExport", method = RequestMethod.GET)
    public void changeAgentListExport(UserAgentChangeParam param, HttpServletResponse response) {
        param.initParam();
        param.setCurrentPage(1);
        param.setPageSize(Constant.MAX_PAGE_SIZE);
        R result = userAgentChangeService.changeAgentList(param);
        PageInfo pageInfo = (PageInfo) result.get("data");
        List<AgentInfoData> list = pageInfo.getList();
        String excelFileName = "客户转代理商记录";
        try {
            ExcelExportUtil.exportList(excelFileName, list, UserAgentChangeData.class, response);
        } catch (InvocationTargetException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        }
    }

    @RepeatCommitToken
    @RequestMapping(value = "/findCompanyName", method = RequestMethod.GET)
    public R findCompanyName(String inAgentName) {
        return userAgentChangeService.findCompanyName(inAgentName);
    }


}
