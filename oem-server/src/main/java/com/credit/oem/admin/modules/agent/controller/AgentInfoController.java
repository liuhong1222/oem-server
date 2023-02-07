package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.ExcelExportUtil;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoData;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoUpdateParam;
import com.credit.oem.admin.modules.agent.model.param.AgentRechargeParam;
import com.credit.oem.admin.modules.agent.service.AgentInfoService;
import com.credit.oem.admin.modules.agent.service.ExcelExportErrorService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/8
 */
@RestController
@RequestMapping("/open/agent/agentInfo")
@Validated
@Api("代理商信息操作")
public class AgentInfoController extends AbstractController {

    @Autowired
    AgentInfoService agentInfoService;

    @Autowired
    ExcelExportErrorService excelExportErrorService;


    @SysLog("查询代理商列表")
    @GetMapping("/list")
    @ApiOperation("查询代理商列表")
    @RequiresPermissions("user:agent:info:list")
    public R list(AgentInfoParam param) {
        //拼接开始时间、结束时间
        param.appendTimeString();
        return R.ok(agentInfoService.list(param));
    }

    @GetMapping("/list/export")
    @ApiOperation("导出代理商列表")
    @RequiresPermissions("user:agent:info:list:export")
    public void listExport(AgentInfoParam param, HttpServletResponse response) throws IOException {
        try {
            param.appendTimeString();
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = agentInfoService.list(param);
            List<AgentInfoData> list = pageInfo.getList();
            String excelFileName = "代理商列表";
            ExcelExportUtil.exportList(excelFileName, list, AgentInfoData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @PostMapping("/license/upload")
    @ApiOperation("上传营业执照")
    @RequiresPermissions("user:agent:license:upload")
    public R uploadLicense(@Valid MultipartFile file) throws Exception {
        long fileSize = file.getSize();
        if (fileSize <= 0) {
            return R.error("上传失败:文件为空");
        } else if (fileSize > (50 * 1024 * 1024)) {
            return R.error("上传失败:文件大小不能超过50M");
        }
        return R.ok(agentInfoService.uploadLicense(getUserId(), file));
    }

    @SysLog("新增代理商")
    @RepeatCommitToken
    @PostMapping("/save")
    @ApiOperation("新增代理商")
    @RequiresPermissions("user:agent:save")
    public R saveAgent(@Valid AgentInfoSaveParam param) {
        agentInfoService.saveAgent(param, getUserId());
        return R.ok();
    }

    @SysLog("查询代理商详情")
    @GetMapping("/detail")
    @ApiOperation("查询代理商详情")
    @RequiresPermissions("user:agent:info")
    public R detail(@Valid Long agentId) {
        return R.ok(agentInfoService.detail(agentId));
    }

    @SysLog("修改代理商")
    @PostMapping("/update")
    @ApiOperation("修改代理商")
    @RepeatCommitToken
    @RequiresPermissions("user:agent:update")
    public R updateAgent(@Valid AgentInfoUpdateParam param) {
        agentInfoService.updateAgent(param);
        return R.ok();
    }

    @SysLog("禁用代理商")
    @RepeatCommitToken
    @PostMapping("/pause")
    @ApiOperation("禁用代理商")
    @RequiresPermissions("user:agent:pause")
    public R pauseAgent(@Valid Long agentId) {
        agentInfoService.pauseAgent(agentId);
        return R.ok();
    }

    @SysLog("启用代理商")
    @RepeatCommitToken
    @PostMapping("/resume")
    @ApiOperation("启用代理商")
    @RequiresPermissions("user:agent:resume")
    public R resumeAgent(@Valid Long agentId) {
        agentInfoService.resumeAgent(agentId);
        return R.ok();
    }

    @SysLog("理商充值")
    @RepeatCommitToken
    @PostMapping("/recharge")
    @ApiOperation("理商充值")
    @RequiresPermissions("user:agent:recharge")
    public R recharge(AgentRechargeParam param) {
        agentInfoService.recharge(param);
        return R.ok();
    }


}
