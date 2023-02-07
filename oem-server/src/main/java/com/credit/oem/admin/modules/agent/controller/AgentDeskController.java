package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.UpdatePackageParam;
import com.credit.oem.admin.modules.agent.service.AgentDeskService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * Description:
 * User: zxa
 * Date: 2018-08-24
 * Time: 13:58
 */
@RestController
@RequestMapping("/open/agent/desk")
public class AgentDeskController extends AbstractController {

    @Autowired
    AgentDeskService agentDeskService;

    @SysLog("查看代理商工作台信息")
    @ApiOperation("查看代理商工作台信息")
    @RequiresPermissions("open:agent:desk:getAgentDeskInfo")
    @PostMapping(value = "/getAgentDeskInfo")
    public R getAgentDeskInfo() {
        return agentDeskService.getAgentDeskInfo(getAgentId(), getUserId());
    }


    @RepeatCommitToken
    @ApiOperation("修改代理商预警值")
    @RequiresPermissions("open:agent:desk:updateWarnNumber")
    @PostMapping(value = "/updateWarnNumber")
    public R updateWarnNumber(String warnNumber) {
        if (StringUtils.isBlank(warnNumber)) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "预警条数不能为空");
        }
        long warnNumber2 = new BigDecimal(warnNumber).multiply(BigDecimal.valueOf(10000)).longValue();
        return agentDeskService.updateWarnNumber(getAgentId(), warnNumber2);
    }

//    @RequestMapping(value = "/updateMobile")
//    public R updateMobile(String mobile) {
//        return agentDeskService.updateMobile(getUserId(),mobile);
//    }

    @SysLog("查询手机号")
    @ApiOperation("查询手机号")
    @RequiresPermissions("open:agent:desk:findMobile")
    @RequestMapping(value = "/findMobile")
    public R findMobile() {
        return agentDeskService.findMobile(getUserId());
    }

    @RepeatCommitToken
    @SysLog("更新email")
    @ApiOperation("更新email")
    @RequiresPermissions("open:agent:desk:updateMail")
    @PostMapping(value = "/updateMail")
    public R updateMail(String mail) {
        return agentDeskService.updateMail(getUserId(), mail);
    }

    @SysLog("查看代理商套餐列表")
    @ApiOperation("查看代理商套餐列表")
    @RequiresPermissions("open:agent:desk:findAgentPackage")
    @PostMapping(value = "/findAgentPackage")
    public R findAgentPackage() {
        return agentDeskService.findAgentPackage(getAgentId());
    }

    @RepeatCommitToken
    @SysLog("修改代理商套餐")
    @ApiOperation("修改代理商套餐")
    @RequiresPermissions("open:agent:desk:updateAgentPackage")
    @PostMapping(value = "/updateAgentPackage")
    public R updateAgentPackage(@RequestBody UpdatePackageParam param) {
        return agentDeskService.updateAgentPackage(getAgentId(), param);
    }

    @SysLog("查看管理员工作台信息")
    @ApiOperation("查看管理员工作台信息")
    @RequiresPermissions("open:agent:desk:getAdminDeskInfo")
    @PostMapping(value = "/getAdminDeskInfo")
    public R getAdminDeskInfo() {
        return agentDeskService.getAdminDeskInfo();
    }

}
