package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.modules.agent.dao.AgentBasicLevelMapper;
import com.credit.oem.admin.modules.agent.entity.AgentBasicLevel;
import com.credit.oem.admin.modules.agent.model.data.AgentBasicLevelData;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelUpdateParam;
import com.credit.oem.admin.modules.agent.service.AgentBasicLevelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/11
 */
@Service
public class AgentBasicLevelServiceImpl implements AgentBasicLevelService {

    @Autowired
    AgentBasicLevelMapper agentBasicLevelMapper;

    /**
     * 查询代理商级别列表
     */
    @Override
    public List<AgentBasicLevelData> list() {
        List<AgentBasicLevelData> result = new ArrayList<>();
        List<AgentBasicLevel> agentBasicLevelList = agentBasicLevelMapper.selectListByNotDeleted();
        for (AgentBasicLevel agentBasicLevel : agentBasicLevelList) {
            AgentBasicLevelData agentBasicLevelData = new AgentBasicLevelData();
            BeanUtils.copyProperties(agentBasicLevel, agentBasicLevelData);
            result.add(agentBasicLevelData);
        }
        return result;
    }

    /**
     * 新增代理商级别
     */
    @Override
    public void saveBasicLevel(AgentBasicLevelSaveParam param) {
        //验证参数
        verifyParams(param.getLevel(), param.getPrice(), param.getEmptyWarnNumber(), param.getMinRecharge(), param.getMaxRecharge());
        //验证级别是否已经存在
        AgentBasicLevel agentBasicLevelVerifyLevel = agentBasicLevelMapper.queryOneByLevel(param.getLevel());
        Assert.isNull(agentBasicLevelVerifyLevel, "此级别数已存在，不能重复添加");
        AgentBasicLevel agentBasicLevel = new AgentBasicLevel();
        BeanUtils.copyProperties(param, agentBasicLevel);
        agentBasicLevelMapper.insertSelective(agentBasicLevel);
    }

    private void verifyParams(Integer level, BigDecimal price, Long emptyWarnNumber,
                              BigDecimal minRecharge, BigDecimal maxRecharge) {
        //验证级别数
        if (level.compareTo(0) < 1) {
            throw new RRException("级别数必须大于0");
        }
        //验证单价
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new RRException("单价必须大于0");
        }
        //验证预警条数
        if (emptyWarnNumber.compareTo(0L) < 0) {
            throw new RRException("预警条数必须大于0");
        }
        //验证最小充值金额
        if (minRecharge.compareTo(BigDecimal.ZERO) < 1) {
            throw new RRException("最小充值金额必须大于0");
        }
        //验证最大充值金额
        if (maxRecharge.compareTo(BigDecimal.ZERO) < 1) {
            throw new RRException("最大充值金额必须大于0");
        }
    }

    /**
     * 修改代理商级别
     */
    @Override
    public void updateBasicLevel(AgentBasicLevelUpdateParam param) {
        //验证参数
        verifyParams(param.getLevel(), param.getPrice(), param.getEmptyWarnNumber(), param.getMinRecharge(), param.getMaxRecharge());
        //验证id是否存在
        AgentBasicLevel agentBasicLevelVerifyId = agentBasicLevelMapper.selectByPrimaryKey(param.getId());
        Assert.notNull(agentBasicLevelVerifyId,"记录不存在");
        //验证级别是否已经存在
        AgentBasicLevel agentBasicLevelVerifyLevel = agentBasicLevelMapper.queryOneByLevel(param.getLevel());
        if (agentBasicLevelVerifyLevel != null && !agentBasicLevelVerifyLevel.getId().equals(param.getId())) {
            throw new RRException("此级别数已存在");
        }
        AgentBasicLevel agentBasicLevel = new AgentBasicLevel();
        BeanUtils.copyProperties(param, agentBasicLevel);
        agentBasicLevelMapper.updateByPrimaryKeySelective(agentBasicLevel);
    }

    /**
     * 删除代理商级别
     */
    @Override
    public void deleteBasicLevel(Long id) {
        //验证id是否存在
        AgentBasicLevel agentBasicLevelVerifyId = agentBasicLevelMapper.selectByPrimaryKey(id);
        Assert.notNull(agentBasicLevelVerifyId,"记录不存在");
        agentBasicLevelMapper.deleteOneById(id);
    }

    /**
     * 查看代理商级别
     */
    @Override
    public AgentBasicLevelData detail(Long id) {
        AgentBasicLevel agentBasicLevel = agentBasicLevelMapper.selectByPrimaryKey(id);
        Assert.notNull(agentBasicLevel,"记录不存在");
        AgentBasicLevelData result = new AgentBasicLevelData();
        BeanUtils.copyProperties(agentBasicLevel, result);
        return result;
    }


}
