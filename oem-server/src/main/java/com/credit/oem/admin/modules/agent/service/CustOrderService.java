package com.credit.oem.admin.modules.agent.service;


import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.CustRechargeParam;
import com.credit.oem.admin.modules.agent.model.param.CustRefundParam;

/**
 * copyright (C), 2018-2018, 创蓝253
 *
 * @author zhangx
 * @fileName ManagerService
 * @date 2018/5/18 19:27
 * @description
 */
public interface CustOrderService {
    //    R selectRechargeList(RechargeDetailParam param);
    R getPackageInfo(Long creUserId, String custName);

    R getPackageInfoById(Long packageId);

    R recharge(CustRechargeParam param);

    R refunds(CustRefundParam param);

}
