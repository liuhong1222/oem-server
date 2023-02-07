package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.service.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzj
 * @since 2018/8/17
 */
@RestController
@RequestMapping("/open/agent/stats")
@Validated
@Api("代理商统计")
public class StatsController {

    @Autowired
    StatsService statsService;

    @SysLog("统计能升级的代理商数量")
    @GetMapping("/agentCanUpgrade/count")
    @ApiOperation("统计能升级的代理商数量")
    @RequiresPermissions("stats:agentCanUpgrade:count")
    public R countAgentCanUpgrade() {
        Long countAgentCanUpgrade = statsService.countAgentCanUpgrade();
        Map<String, Long> map = new HashMap<>();
        map.put("countAgentCanUpgrade", countAgentCanUpgrade);
        return R.ok(map);
    }

}
