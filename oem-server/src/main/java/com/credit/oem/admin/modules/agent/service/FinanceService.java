package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import com.github.pagehelper.PageInfo;

/**
 * @author chenzj
 * @since 2018/8/13
 */
public interface FinanceService {

    PageInfo agentRechargeList(FinanceRechargeParam param);

    PageInfo myRechargeList(FinanceRechargeParam param,Long sysUserId);

    PageInfo userRechargeList(FinanceRechargeParam param);

    PageInfo userRefundList(FinanceRechargeParam param);

    PageInfo userConsumeList(FinanceRechargeParam param);
    
    PageInfo userRechargeCheckList(FinanceRechargeParam param);

    R checkRecharge(String orderId);
    
    R checkSureRecharge(String orderId,String checkStatus,String remark);
}
