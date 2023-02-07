package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.modules.agent.dao.AgentOrderMapper;
import com.credit.oem.admin.modules.agent.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenzj
 * @since 2018/8/17
 */
@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    AgentOrderMapper agentOrderMapper;

    /**
     * 统计能升级的代理商数量
     */
    @Override
    public Long countAgentCanUpgrade() {
        Long count = agentOrderMapper.countAgentCanUpgrade();
        if (count==null) {
            count = 0L;
        }
        return count;
    }
}
