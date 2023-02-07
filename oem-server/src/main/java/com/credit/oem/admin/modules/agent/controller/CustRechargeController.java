package com.credit.oem.admin.modules.agent.controller;


import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.CustRechargeParam;
import com.credit.oem.admin.modules.agent.model.param.CustRefundParam;
import com.credit.oem.admin.modules.agent.service.CreUserService;
import com.credit.oem.admin.modules.agent.service.CustOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName UserRechargeManager
 * author   zhangx
 * date     2018/5/29 18:39
 * description
 */
@RestController
@RequestMapping("/open/agent/cust")
public class CustRechargeController {

    @Autowired
    CustOrderService orderService;
    @Autowired
    CreUserService creUserService;

    @RequiresPermissions("open:agent:cust:getPackageInfo")
    @RequestMapping(value = "/getPackageInfo")
    public R getPackageInfo(Long creUserId, String custName) {
        return orderService.getPackageInfo(creUserId, custName);
    }

    @RequiresPermissions("open:agent:cust:getPackageInfoById")
    @RequestMapping(value = "/getPackageInfoById")
    public R getPackageInfoById(Long id) {
        return orderService.getPackageInfoById(id);
    }

    @RepeatCommitToken
    @SysLog("代理商给客户充值")
    @RequiresPermissions("open:agent:cust:recharge")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public R recharge(CustRechargeParam param) {
        param.setType("1");//trd_order中的type充值
        return orderService.recharge(param);
    }

    @RequiresPermissions("open:agent:cust:getCustInfo")
    @RequestMapping(value = "/getCustInfo", method = RequestMethod.POST)
    public R getCustInfo(String custName, Integer creUserId) {
        return creUserService.getCustInfo(custName, creUserId);
    }


    @RepeatCommitToken
    @SysLog("代理商给客户退款")
    @RequiresPermissions("open:agent:cust:refunds")
    @RequestMapping(value = "/refunds", method = RequestMethod.POST)
    public R refunds(CustRefundParam param) {
        param.setType("2");//trd_order中的type退款
        return orderService.refunds(param);
    }




}
