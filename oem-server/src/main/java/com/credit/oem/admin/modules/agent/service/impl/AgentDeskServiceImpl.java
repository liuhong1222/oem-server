package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.common.validator.Assert;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.model.param.PackageParam;
import com.credit.oem.admin.modules.agent.model.param.UpdatePackageParam;
import com.credit.oem.admin.modules.agent.service.AgentDeskService;
import com.credit.oem.admin.modules.agent.util.NumberUtil;
import com.credit.oem.admin.modules.sys.dao.SysUserDao;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.google.common.collect.Maps;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserServiceImpl
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
@Service
public class AgentDeskServiceImpl implements AgentDeskService {

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    AgentLevelMapper levelMapper;
    @Autowired
    AgentAccountMapper accountMapper;
    @Autowired
    AgentWebsiteMapper websiteMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    TrdOrderMapper trdOrderMapper;
    @Autowired
    NumberCheckLogMapper numberCheckLogMapper;
    @Autowired
    AgentPackageMapper packageMapper;

    @Autowired
    SysUserService sysUserService;
    @Autowired
    AgentOrderMapper agentOrderMapper;
    @Autowired
    AgentDomainMapper agentDomainMapper;


    @Override
    public R getAgentDeskInfo(Long agentId, Long sysUserId) {
        Assert.isNull(sysUserId, "未登录");
        Assert.isNull(agentId, "不是代理商");
        SysUserEntity sysUserEntity = sysUserDao.selectById(sysUserId);
        Map map = Maps.newHashMap();
        map.put("mobile", sysUserEntity.getMobile());
        map.put("email", sysUserEntity.getEmail());

        AgentLevel agentLevel = levelMapper.queryOneByAgentId(agentId);
        map.put("price", agentLevel != null ? agentLevel.getPrice() : "");
        AgentAccount agentAccount = accountMapper.queryOneByAgentId(agentId);
        BigDecimal emptyBalance = agentAccount != null ? BigDecimal.valueOf(agentAccount.getEmptyBalance()).divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        map.put("emptyBalance", NumberUtil.splitAmountByComma(emptyBalance));
        BigDecimal emptyWarnNumber = agentAccount != null ? BigDecimal.valueOf(agentAccount.getEmptyWarnNumber()).divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        map.put("emptyWarnNumber", NumberUtil.splitAmountByComma(emptyWarnNumber));


        AgentDomain agentDomain=agentDomainMapper.selectByAgentId(agentId);
        map.put("referralLink", agentDomain != null ? agentDomain.getName(): "");
        map.put("creUserCount", agentMapper.countCreUserByAgentId(agentId));
        BigDecimal rechargeSum = trdOrderMapper.getRechargeAmountSumByAgentId(agentId);
        map.put("rechargeSum", NumberUtil.splitAmountByComma(rechargeSum));
        Long rechargeNumberSum = trdOrderMapper.getRechargeNumberSumByAgentId(agentId);
        map.put("rechargeNumberSum", NumberUtil.splitAmountByComma(rechargeNumberSum));
        Long consumSum = numberCheckLogMapper.getConsumSumByAgentId(agentId);
        map.put("consumSum", NumberUtil.splitAmountByComma(consumSum));



        R r = new R();
        r.put("data", map);
        return r;

    }

    @Override
    public R updateWarnNumber(Long agentId, Long warnNumber) {
        AgentAccount agentAccount = accountMapper.queryOneByAgentId(agentId);
        if (agentAccount == null) {
            agentAccount = new AgentAccount();
            agentAccount.setAgentId(agentId);
            agentAccount.setEmptyWarnNumber(warnNumber);
            accountMapper.insertSelective(agentAccount);
        } else {
            AgentAccount updateAccount = new AgentAccount();
            updateAccount.setId(agentAccount.getId());
            updateAccount.setEmptyWarnNumber(warnNumber);
            accountMapper.updateByPrimaryKeySelective(updateAccount);
        }
        return R.ok();
    }

    @Override
    public R updateMobile(Long sysUserId, String mobile) {
        sysUserDao.updateMobileByUserId(sysUserId, mobile);
        return R.ok();
    }

    @Override
    public R findMobile(Long sysUserId) {
        if (sysUserId == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "未登录");
        }
        String mobile = sysUserDao.selectById(sysUserId).getMobile();
        Map map = Maps.newHashMap();
        map.put("mobile", mobile);
        return R.ok(map);
    }

    @Override
    public R updateMail(Long sysUserId, String mail) {
        sysUserDao.updateEmailByUserId(sysUserId, mail);
        return R.ok();
    }

    @Override
    public R findAgentPackage(Long agentId) {
        List<AgentPackage> list = packageMapper.selectByAgentId(agentId);
        List resultList = new ArrayList(4);
        for (AgentPackage agentPackage : list) {
            Map map = Maps.newHashMap();
            map.put("id", agentPackage.getId());
            map.put("packageName", agentPackage.getPackageName());
            BigDecimal number = null;
            if (!agentPackage.getPackageName().startsWith("自定义")) {
                number = BigDecimal.valueOf(agentPackage.getNumber()).divide(BigDecimal.valueOf(10000)).setScale(4, BigDecimal.ROUND_HALF_UP);
            } else {
                number = BigDecimal.valueOf(agentPackage.getNumber());
            }
            map.put("number", number);
            map.put("money", agentPackage.getMoney());
            resultList.add(map);
        }
        return R.ok(resultList);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public R updateAgentPackage(Long agentId, UpdatePackageParam param) {
        List<PackageParam> list = param.getList();
        if (list == null || list.size() != 4) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "缺少必要参数");
        }
        for (PackageParam temp : list) {
            if (temp.getId() == null) {
                throw new RRException("缺少必要参数");
            }
            AgentPackage agentPackage = packageMapper.selectByPrimaryKey(temp.getId());
            if (agentPackage == null || !agentPackage.getAgentId().equals(agentId)) {
                throw new RRException("缺少必要参数");
            }
            AgentPackage updatePackage = new AgentPackage();
            updatePackage.setId(agentPackage.getId());
            int number = 0;
            if (!temp.getPackageName().startsWith("自定义")) {
                number = temp.getNumber().multiply(BigDecimal.valueOf(10000)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            } else {
                number = temp.getNumber().intValue();
                if(temp.getMoney().compareTo(BigDecimal.valueOf(0.0001))<0){
                    throw new RRException("自定义单价不能低于0.0001");
                }
            }

            updatePackage.setNumber(number);
            updatePackage.setMoney(temp.getMoney());
            packageMapper.updateByPrimaryKeySelective(updatePackage);
        }
        return R.ok();
    }

    @Override
    public R getAdminDeskInfo() {
        Long sysUserId = sysUserService.getSysUserId();
        if (!sysUserService.judgeIfAdmin(sysUserId)) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "不是管理员角色");
        }

        SysUserEntity sysUserEntity = sysUserDao.selectById(sysUserId);
        Map map = Maps.newHashMap();
        map.put("mobile", sysUserEntity.getMobile());
        map.put("email", sysUserEntity.getEmail());
        map.put("agentCount", NumberUtil.splitAmountByComma(agentMapper.countAgent()));
        map.put("rechargeSum", NumberUtil.splitAmountByComma(agentOrderMapper.sumMoney()));
        map.put("rechargeNumberSum", NumberUtil.splitAmountByComma(agentOrderMapper.sumNumber()));
        map.put("consumSum", NumberUtil.splitAmountByComma(numberCheckLogMapper.getConsumSum()));
        R r = new R();
        r.put("data", map);
        return r;
    }
}
