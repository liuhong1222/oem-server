package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.DateConverter;
import com.credit.oem.admin.common.utils.DateUtils;
import com.credit.oem.admin.common.utils.ExcelExportUtil;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.model.data.*;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import com.credit.oem.admin.modules.agent.service.ExcelExportErrorService;
import com.credit.oem.admin.modules.agent.service.FinanceService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author chenzj
 * @since 2018/8/13
 */
@RestController
@RequestMapping("/open/agent/finance")
@Validated
@Api("财务管理操作")
public class FinanceController extends AbstractController {

    @Autowired
    FinanceService financeService;

    @Autowired
    ExcelExportErrorService excelExportErrorService;

    @SysLog("查询代理商充值记录列表")
    @GetMapping("/agent/recharge/list")
    @ApiOperation("查询代理商充值记录列表")
    @RequiresPermissions("finance:agent:recharge:list")
    public R agentRechargeList(@Valid FinanceRechargeParam param) {
        //拼接开始时间、结束时间
        param.appendTimeString();
        return R.ok(financeService.agentRechargeList(param));
    }

    @GetMapping("/agent/recharge/list/export")
    @ApiOperation("导出代理商充值记录列表")
    @RequiresPermissions("finance:agent:recharge:list:export")
    public void agentRechargeListExport(@Valid FinanceRechargeParam param, HttpServletResponse response) throws IOException {
        //拼接开始时间、结束时间
        try {
            param.appendTimeString();
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = financeService.agentRechargeList(param);
            String excelFileName = "代理商充值记录";
            ExcelExportUtil.exportList(excelFileName, pageInfo.getList(), FinanceAgentRechargeData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("查询我的充值记录列表")
    @GetMapping("/my/recharge/list")
    @ApiOperation("查询我的充值记录列表")
    @RequiresPermissions("finance:my:recharge:list")
    public R myRechargeList(@Valid FinanceRechargeParam param) {
        //拼接开始时间、结束时间
        param.appendTimeString();
        return R.ok(financeService.myRechargeList(param, getUserId()));
    }

    @GetMapping("/my/recharge/list/export")
    @ApiOperation("导出我的充值记录列表")
    @RequiresPermissions("finance:my:recharge:list:export")
    public void myRechargeListExport(@Valid FinanceRechargeParam param, HttpServletResponse response) throws IOException {
        //拼接开始时间、结束时间
        try {
            param.appendTimeString();
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = financeService.myRechargeList(param, getUserId());
            String excelFileName = "我的充值记录";
            ExcelExportUtil.exportList(excelFileName, pageInfo.getList(), FinanceMyRechargeData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("查询客户充值记录列表")
    @GetMapping("/user/recharge/list")
    @ApiOperation("查询客户充值记录列表")
    @RequiresPermissions("finance:user:recharge:list")
    public R userRechargeList(@Valid FinanceRechargeParam param) {
        //拼接开始时间、结束时间
        param.appendTimeString();
        param.setAgentId(getAgentId());
        return R.ok(financeService.userRechargeList(param));
    }
    
    @SysLog("查询入账审核列表")
    @GetMapping("/user/rechargeCheck/list")
    @ApiOperation("查询入账审核列表")
    @RequiresPermissions("finance:user:rechargeCheck:list")
    public R userRechargeCheckList(@Valid FinanceRechargeParam param) {
        return R.ok(financeService.userRechargeCheckList(param));
    }
    
    @SysLog("入账审核列表审核")
    @GetMapping("/user/rechargeCheck/list/check")
    @ApiOperation("入账审核列表审核")
    @RequiresPermissions("finance:user:rechargeCheck:list:check")
    public R checkRecharge(String orderId) {
        return financeService.checkRecharge(orderId);
    }
    
    @SysLog("入账审核列表审核确定")
    @GetMapping("/user/rechargeCheck/list/check/checkSure")
    @ApiOperation("入账审核列表审核确定")
    @RequiresPermissions("finance:user:rechargeCheck:list:check:checkSure")
    public R checkSureRecharge(String orderId,String checkStatus,String remark) {
        return financeService.checkSureRecharge(orderId,checkStatus,remark);
    }

    @GetMapping("/user/recharge/list/export")
    @ApiOperation("导出客户充值记录列表")
    @RequiresPermissions("finance:user:recharge:list:export")
    public void userRechargeListExport(@Valid FinanceRechargeParam param, HttpServletResponse response) throws IOException {
        //拼接开始时间、结束时间
        try {
            param.appendTimeString();
            param.setAgentId(getAgentId());
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = financeService.userRechargeList(param);
            String excelFileName = "客户充值记录";
            ExcelExportUtil.exportList(excelFileName, pageInfo.getList(), FinanceUserRechargeData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("查询客户退款记录列表")
    @GetMapping("/user/refund/list")
    @ApiOperation("查询客户退款记录列表")
    @RequiresPermissions("finance:user:refund:list")
    public R userRefundList(@Valid FinanceRechargeParam param) {
        //拼接开始时间、结束时间
        param.appendTimeString();
        param.setAgentId(getAgentId());
        return R.ok(financeService.userRefundList(param));
    }

    @GetMapping("/user/refund/list/export")
    @ApiOperation("导出客户退款记录列表")
    @RequiresPermissions("finance:user:refund:list:export")
    public void userRefundListExport(@Valid FinanceRechargeParam param, HttpServletResponse response) throws IOException {
        //拼接开始时间、结束时间
        try {
            param.appendTimeString();
            param.setAgentId(getAgentId());
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = financeService.userRefundList(param);
            String excelFileName = "客户退款记录";
            ExcelExportUtil.exportList(excelFileName, pageInfo.getList(), FinanceUserRefundData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("查询客户消耗记录列表")
    @GetMapping("/user/consume/list")
    @ApiOperation("查询客户消耗记录列表")
    @RequiresPermissions("finance:user:consume:list")
    public R userConsumeList(@Valid FinanceRechargeParam param) {
        //拼接开始时间、结束时间
        param.appendTimeInt();
        param.setAgentId(getAgentId());
        return R.ok(financeService.userConsumeList(param));
    }

    @GetMapping("/user/consume/list/export")
    @ApiOperation("导出客户消耗记录列表")
    @RequiresPermissions("finance:user:consume:list:export")
    public void userConsumeListExport(@Valid FinanceRechargeParam param, HttpServletResponse response) throws IOException {
        //拼接开始时间、结束时间
        try {
            param.appendTimeInt();
            if (StringUtils.isBlank(param.getStartTime()) && StringUtils.isBlank(param.getEndTime())) {
                throw new RRException("请选择开始时间和结束时间");
            }
            DateConverter dateConverter = new DateConverter();
            if (DateUtils.differentDays(dateConverter.convert(param.getStartTime()), dateConverter.convert(param.getEndTime())) > 31) {
                throw new RRException("一次最多导1个月的数据");
            }
            param.setAgentId(getAgentId());
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = financeService.userConsumeList(param);
            String excelFileName = "客户消耗记录";
            ExcelExportUtil.exportList(excelFileName, pageInfo.getList(), FinanceUserConsumeData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }


}
