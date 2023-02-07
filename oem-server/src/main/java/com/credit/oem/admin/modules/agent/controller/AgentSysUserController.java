package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.ExcelExportUtil;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.model.data.AgentSysUserData;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserUpdateParam;
import com.credit.oem.admin.modules.agent.service.AgentSysUserService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/17
 */
@RestController
@RequestMapping("/open/agent/agentSysUser")
@Validated
@Api("代理商账号操作")
public class AgentSysUserController extends AbstractController {

    @Autowired
    AgentSysUserService agentSysUserService;

    @Autowired
    ExcelExportErrorService excelExportErrorService;

    @SysLog("查询代理商账号列表")
    @GetMapping("/list")
    @ApiOperation("查询代理商账号列表")
    @RequiresPermissions("sys:agentSysUser:list")
    public R list(AgentSysUserParam param) {
        return R.ok(agentSysUserService.list(param, getUserId()));
    }

    @GetMapping("/list/export")
    @ApiOperation("导出代理商账号")
    @RequiresPermissions("sys:agentSysUser:list:export")
    public void listExport(AgentSysUserParam param, HttpServletResponse response) throws IOException {
        try {
            param.setCurrentPage(1);
            param.setPageSize(Constant.MAX_PAGE_SIZE);
            PageInfo pageInfo = agentSysUserService.list(param, getUserId());
            List<AgentSysUserData> list = pageInfo.getList();
            String excelFileName = "代理商账号列表";
            ExcelExportUtil.exportList(excelFileName, list, AgentSysUserData.class, response);
        } catch (Exception e) {
            excelExportErrorService.error(response, e);
        }
    }

    @SysLog("新增代理商账号")
    @RepeatCommitToken
    @PostMapping("/save")
    @ApiOperation("新增代理商账号")
    @RequiresPermissions("sys:agentSysUser:save")
    public R saveAgentSysUser(@Valid AgentSysUserSaveParam param) {
        agentSysUserService.saveAgentSysUser(param, getUserId());
        return R.ok();
    }

    @SysLog("修改代理商账号")
    @RepeatCommitToken
    @PostMapping("/update")
    @ApiOperation("修改代理商账号")
    @RequiresPermissions("sys:agentSysUser:update")
    public R updateAgentSysUser(@Valid AgentSysUserUpdateParam param) {
        agentSysUserService.updateAgentSysUser(param, getUserId());
        return R.ok();
    }

    @SysLog("删除代理商账号")
    @RepeatCommitToken
    @PostMapping("/delete")
    @ApiOperation("删除代理商账号")
    @RequiresPermissions("sys:agentSysUser:delete")
    public R deleteAgentSysUser(@Valid Long userId) {
        agentSysUserService.deleteAgentSysUser(userId, getUserId());
        return R.ok();
    }


}
