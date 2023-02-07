package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserService
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
public interface CreUserService {

    /**
     * 获取客户的手机号，剩余条数，custName客户名称
     */
    R getCustInfo(String custName, Integer creUserId);

    Long getGiveAccountByCreUserId(Integer creUserId);
}
