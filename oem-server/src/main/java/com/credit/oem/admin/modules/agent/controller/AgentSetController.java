package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.model.param.AgentSetListParam;
import com.credit.oem.admin.modules.agent.model.param.UpdateBasicInfoParam;
import com.credit.oem.admin.modules.agent.service.AgentSetService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description:
 * User: zxa
 * Date: 2018-08-24
 * Time: 13:58
 */
@RestController
@RequestMapping("/open/agent/set")
@Validated
@Api("客户管理")
public class AgentSetController extends AbstractController {

    @Autowired
    AgentSetService agentSetService;

    @SysLog("查看代理商列表")
    @ApiOperation("查看代理商列表")
    @RequiresPermissions("open:agent:set:agentSetList")
    @RequestMapping(value = "/agentSetList")
    public R agentSetList(AgentSetListParam param) {
        return agentSetService.agentSetList(param);
    }

    @RepeatCommitToken
    @SysLog("删除代理商")
    @ApiOperation("删除代理商")
    @RequiresPermissions("open:agent:set:delAgent")
    @RequestMapping(value = "/delAgent")
    public R delAgent(Long agentId) {
        return agentSetService.delAgent(agentId);
    }

    @SysLog("查找基本信息")
    @ApiOperation("查找基本信息")
    @RequiresPermissions("open:agent:set:findBasicInfo")
    @RequestMapping(value = "/findBasicInfo")
    public R findBasicInfo(Long agentId) {
        return agentSetService.findBasicInfo(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新基本信息")
    @RequiresPermissions("open:agent:set:updateBasicInfo")
    @RequestMapping(value = "/updateBasicInfo")
    public R updateBasicInfo(UpdateBasicInfoParam param) {
        return agentSetService.updateBasicInfo(param);
    }


    @RequiresPermissions("open:agent:set:findDomainInfo")
    @RequestMapping(value = "/findDomainInfo")
    public R findDomainInfo(Long agentId) {
        return agentSetService.findDomainInfo(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新域名备案信息")
    @RequiresPermissions("open:agent:set:updateDomainInfo")
    @RequestMapping(value = "/updateDomainInfo")
    public R updateDomainInfo(AgentDomain param) {
        return agentSetService.updateDomainInfo(param);
    }


    @RequiresPermissions("open:agent:set:findCustService")
    @RequestMapping(value = "/findCustService")
    public R findCustService(Long agentId) {
        return agentSetService.findCustService(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新客服信息")
    @RequiresPermissions("open:agent:set:updateCustService")
    @RequestMapping(value = "/updateCustService")
    public R updateCustService(AgentCustService param) {
        return agentSetService.updateCustService(param);
    }


    @RequiresPermissions("open:agent:set:findContract")
    @RequestMapping(value = "/findContract")
    public R findContract(Long agentId) {
        return agentSetService.findContract(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新合同信息")
    @RequiresPermissions("open:agent:set:updateContract")
    @RequestMapping(value = "/updateContract")
    public R updateContract(AgentContract param) {
        return agentSetService.updateContract(param);
    }

    @RequiresPermissions("open:agent:set:findAlipay")
    @RequestMapping(value = "/findAlipay")
    public R findAlipay(Long agentId) {
        return agentSetService.findAlipay(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新支付宝信息")
    @RequiresPermissions("open:agent:set:updateAlipay")
    @RequestMapping(value = "/updateAlipay")
    public R updateAlipay(AgentAlipay param) {
        return agentSetService.updateAlipay(param);
    }

    @RequiresPermissions("open:agent:set:findWeixinpay")
    @RequestMapping(value = "/findWeixinpay")
    public R findWeixinpay(Long agentId) {
        return agentSetService.findWeixinpay(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新微信信息")
    @RequiresPermissions("open:agent:set:updateWeixinpay")
    @RequestMapping(value = "/updateWeixinpay")
    public R updateWeixinpay(AgentWeixinpay param) {
        return agentSetService.updateWeixinpay(param, getUserId());
    }

    @RepeatCommitToken
    @SysLog("审核通过")
    @RequiresPermissions("open:agent:set:audit:approved")
    @RequestMapping(value = "/audit/approved")
    public R auditApproved(Long agentId) {
        return agentSetService.auditApproved(agentId, getUserId());
    }

    @RepeatCommitToken
    @SysLog("审核驳回")
    @RequiresPermissions("open:agent:set:audit:rejected")
    @RequestMapping(value = "/audit/rejected")
    public R auditRejected(Long agentId, String remark) {
        return agentSetService.auditRejected(agentId, getUserId(), remark);
    }

    /**
     * 查询审核状态
     */
    @RequestMapping(value = "/audit/state")
    public R auditState() {
        return agentSetService.auditState(getAgentId());
    }

    @RequiresPermissions("open:agent:set:findWxLogin")
    @RequestMapping(value = "/findWxLogin")
    public R findWxLogin(Long agentId) {
        return agentSetService.findWxLogin(agentId);
    }

    @RepeatCommitToken
    @SysLog("更新微信登录信息")
    @RequiresPermissions("open:agent:set:updateWxLogin")
    @RequestMapping(value = "/updateWxLogin")
    public R updateWxLogin(AgentWxLogin param) {
        return agentSetService.updateWxLogin(param);
    }

}
