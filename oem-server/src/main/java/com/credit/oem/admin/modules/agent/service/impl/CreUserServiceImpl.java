package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.CreUserAccountMapper;
import com.credit.oem.admin.modules.agent.dao.CreUserMapper;
import com.credit.oem.admin.modules.agent.dao.TrdOrderMapper;
import com.credit.oem.admin.modules.agent.entity.CreUser;
import com.credit.oem.admin.modules.agent.entity.CreUserAccount;
import com.credit.oem.admin.modules.agent.service.CreUserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserServiceImpl
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
@Service
public class CreUserServiceImpl implements CreUserService {

    @Autowired
    CreUserMapper creUserMapper;
    @Autowired
    CreUserAccountMapper creUserAccountMapper;

    @Autowired
    TrdOrderMapper trdOrderMapper;

    @Override
    public R getCustInfo(String custName, Integer creUserId) {
        CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId);
        if (creUser == null) {
            return R.error("不存在该用户!");
        }
        CreUserAccount creUserAccount = creUserAccountMapper.selectNormalByCreUserId(creUserId);
        Map map = Maps.newHashMap();
        Integer account = creUserAccount.getAccount();
        if (account == null) {
            account = 0;
        }
        map.put("mobile", creUser.getUserPhone());
        map.put("account", account);
        map.put("custName", custName);

        Integer refundableAccount = account - getGiveAccountByCreUserId(creUserId).intValue();
        if (refundableAccount < 0) {
            refundableAccount = 0;
        }
        map.put("refundableAccount", refundableAccount);

        R result = new R();
        result.put("data", map);
        return result;
    }

    @Override
    public Long getGiveAccountByCreUserId(Integer creUserId) {
        Long giveAccount = trdOrderMapper.getGiveAmountSumByCreUserId(Long.valueOf(creUserId));
        if (giveAccount == null) {
            giveAccount = 0L;
        }
        return giveAccount;
    }


}
