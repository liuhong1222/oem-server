package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelUpdateParam;
import com.credit.oem.admin.modules.agent.service.AgentBasicLevelService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author chenzj
 * @since 2018/8/11
 */
@RestController
@RequestMapping("/open/agent/level")
@Validated
public class AgentBasicLevelController {

    @Autowired
    AgentBasicLevelService agentBasicLevelService;

    @SysLog("查询代理商级别列表")
    @GetMapping("/list")
    @ApiOperation("查询代理商级别列表")
    @RequiresPermissions("user:level:info:list")
    public R list() {
        return R.ok(agentBasicLevelService.list());
    }

    @SysLog("新增代理商级别")
    @RepeatCommitToken
    @PostMapping("/save")
    @ApiOperation("新增代理商级别")
    @RequiresPermissions("user:level:save")
    public R saveBasicLevel(@Valid AgentBasicLevelSaveParam param) {
        agentBasicLevelService.saveBasicLevel(param);
        return R.ok();
    }

    @SysLog("修改代理商级别")
    @RepeatCommitToken
    @PostMapping("/update")
    @ApiOperation("修改代理商级别")
    @RequiresPermissions("user:level:update")
    public R updateBasicLevel(@Valid AgentBasicLevelUpdateParam param) {
        agentBasicLevelService.updateBasicLevel(param);
        return R.ok();
    }

    @SysLog("删除代理商级别")
    @RepeatCommitToken
    @PostMapping("/delete")
    @ApiOperation("删除代理商级别")
    @RequiresPermissions("user:level:delete")
    public R deleteBasicLevel(@Valid Long id) {
        agentBasicLevelService.deleteBasicLevel(id);
        return R.ok();
    }

    @SysLog("查看代理商级别")
    @GetMapping("/detail")
    @ApiOperation("查看代理商级别")
    @RequiresPermissions("user:level:info")
    public R detail(@Valid Long id) {
        return R.ok(agentBasicLevelService.detail(id));
    }


}
