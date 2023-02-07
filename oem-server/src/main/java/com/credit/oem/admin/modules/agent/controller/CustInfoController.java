package com.credit.oem.admin.modules.agent.controller;


import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.ExcelExportUtil;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.model.data.CustExportData;
import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import com.credit.oem.admin.modules.agent.service.AuthInfoService;
import com.credit.oem.admin.modules.agent.service.ExcelExportErrorService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author zhangx
 * date  2018/8/31 2:17
 */
@RestController
@RequestMapping("/open/agent/cust")
public class CustInfoController {
    @Autowired
    AuthInfoService authInfoService;

    @Autowired
    ExcelExportErrorService excelExportErrorService;

    @SysLog("查询客户列表")
    @ApiOperation("查询客户列表")
    @RequiresPermissions("user:user:info:list")
    @RequestMapping(value = "/custList")
    public R custList(CustInfoParam param) {
        return authInfoService.custList2(param);
    }

    @ApiOperation("导出客户列表")
    @RequiresPermissions("user:user:info:list:export")
    @RequestMapping(value = "/custListExport")
    public void custListExport(CustInfoParam param, HttpServletResponse response) throws IOException {
        try {
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            R result = authInfoService.custList2(param);

            List<CustExportData> list = new ArrayList<>();
            PageInfo<Map> pageInfo = (PageInfo<Map>) result.get("data");
            List<Map> mapList = pageInfo.getList();

            for (Map map : mapList) {
                CustExportData custExportData = new CustExportData();
                custExportData.setNumber(map.get("number") != null ? map.get("number").toString() : "");
                custExportData.setAccount(map.get("account") != null ? map.get("account").toString() : "");
                custExportData.setCompanyName(map.get("company_name") != null ? map.get("company_name").toString() : "");
                custExportData.setCreateTime(map.get("create_time") != null ? map.get("create_time").toString() : "");
                custExportData.setCreUserId(map.get("creUserId") != null ? map.get("creUserId").toString() : "");
                custExportData.setCustName(map.get("custName") != null ? map.get("custName").toString() : "");
                custExportData.setMoney(map.get("money") != null ? map.get("money").toString() : "");
                custExportData.setUserPhone(map.get("user_phone") != null ? map.get("user_phone").toString() : "");
                custExportData.setUserType(map.get("userType") != null ? map.get("userType").toString() : "");
                list.add(custExportData);
            }
            ExcelExportUtil.exportList("", list, CustExportData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("查看个人客户详情")
    @ApiOperation("查看个人客户详情")
    @RequiresPermissions("user:indv:info")
    @RequestMapping(value = "/findPersonById")
    public R findPersonById(@RequestParam("id") String idStr, Integer creUserId) {
        Integer id = null;
        if (idStr == null || "null".equalsIgnoreCase(idStr) || "".equalsIgnoreCase(idStr)||"undefined".equals(idStr)) {
            idStr = null;
        } else {
            id = Integer.valueOf(idStr);
        }

        return authInfoService.findPersonCustById(id, creUserId);
    }

    @SysLog("查看公司客户详情")
    @ApiOperation("查看公司客户详情")
    @RequiresPermissions("user:co:info")
    @RequestMapping(value = "/findCompanyById")
    public R findCompanyById(@RequestParam("id") String idStr, Integer creUserId) {
        Integer id = null;
        if (idStr == null || "null".equalsIgnoreCase(idStr) || "".equalsIgnoreCase(idStr)||"undefined".equals(idStr)) {
            idStr = null;
        } else {
            id = Integer.valueOf(idStr);
        }
        return authInfoService.findCompanyCustById(id, creUserId);
    }
    
    @SysLog("新增客户")
    @ApiOperation("新增客户")
    @RequiresPermissions("user:user:info:createCust")
    @RequestMapping(value = "/createCust")
    public R createCust(String mobile,String password) {
        return authInfoService.createCust(mobile, password);
    }
}
