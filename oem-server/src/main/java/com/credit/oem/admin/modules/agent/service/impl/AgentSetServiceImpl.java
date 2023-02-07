package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.enums.AgentAuditStateEnum;
import com.credit.oem.admin.modules.agent.model.param.AgentSetListParam;
import com.credit.oem.admin.modules.agent.model.param.UpdateBasicInfoParam;
import com.credit.oem.admin.modules.agent.service.AgentSetService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName CreUserServiceImpl
 * author   zhangx
 * date     2018/8/8 11:07
 * description
 */
@Service
public class AgentSetServiceImpl implements AgentSetService {

    @Autowired
    AgentSetMapper agentSetMapper;

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentWebsiteMapper websiteMapper;
    @Autowired
    AgentDomainMapper domainMapper;
    @Autowired
    AgentCustServiceMapper custServiceMapper;
    @Autowired
    AgentWeixinpayMapper weixinpayMapper;
    @Autowired
    AgentAlipayMapper alipayMapper;
    @Autowired
    AgentContractMapper contractMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    AgentAuditRecordMapper agentAuditRecordMapper;
    @Autowired
    AgentWxLoginMapper agentWxLoginMapper;

    @Override
    public R agentSetList(AgentSetListParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<Map> list = agentSetMapper.agentSetList(param);
        PageInfo<Map> pageInfo = new PageInfo(list);
        return R.ok(pageInfo);
    }

    @Override
    public R delAgent(Long agentId) {
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
//        if (agent.getStatus().equals(Constant.COMMON_DISABLED_STATUS_VALUE)) {
//            throw new RRException("该代理商目前已是删除状态");
//        }
        agentMapper.updateDeleteStatusById(agentId, Constant.COMMON_DELETED_STATUS_VALUE);
        return R.ok();
    }

    @Override
    public R findBasicInfo(Long agentId) {
        if (agentId == null) {
            agentId = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());
        }
        List<Map> mapList = agentSetMapper.findBasicInfo(agentId);

//        AgentWeixinpay weixinpay = weixinpayMapper.selectByAgentId(agentId);
        AgentWxLogin agentWxLogin = agentWxLoginMapper.selectByAgentId(agentId);
        String setStatus = "";
        if (agentWxLogin != null) {
            setStatus = "true";
        } else {
            setStatus = "false";
        }

        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        if (agent.getAuditState().equals(AgentAuditStateEnum.REJECTED.getCode())) {
            setStatus = "false";
        }
        if (agent.getDeleteFlag() != null && agent.getDeleteFlag().intValue() == 2) {
            setStatus = "true";
            List<Map> mapList2 = new ArrayList<>();
            Map map2 = Maps.newHashMap();
            map2.put("agentId", agentId);
            map2.put("setStatus", setStatus);
            mapList2.add(map2);

            return R.ok(mapList2);
        } else {
            for (Map map : mapList) {
                map.put("setStatus", setStatus);
            }
            return R.ok(mapList);
        }


    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public R updateBasicInfo(UpdateBasicInfoParam param) {
        if (param.getAgentId() == null) {
            throw new RRException("缺少必要参数");
        }
        //
        AgentWebsite agentWebsite = websiteMapper.selectByAgentId(param.getAgentId());
        if (agentWebsite == null) {
            agentWebsite = new AgentWebsite();
            agentWebsite.setAgentId(param.getAgentId());
            if (StringUtils.isNotBlank(param.getIcon_url())) {
                agentWebsite.setIconUrl(param.getIcon_url());
            }
            if (StringUtils.isNotBlank(param.getLogo_url())) {
                agentWebsite.setLogoUrl(param.getLogo_url());
            }

            agentWebsite.setSmsSign(param.getSms_sign());
            agentWebsite.setCreateTime(new Date());
            websiteMapper.insertSelective(agentWebsite);
        } else {
            AgentWebsite updateWebsite = new AgentWebsite();
            updateWebsite.setId(agentWebsite.getId());

            if (StringUtils.isNotBlank(param.getIcon_url())) {
                updateWebsite.setIconUrl(param.getIcon_url());
            }
            if (StringUtils.isNotBlank(param.getLogo_url())) {
                updateWebsite.setLogoUrl(param.getLogo_url());
            }

            updateWebsite.setSmsSign(param.getSms_sign());
            updateWebsite.setUpdateTime(new Date());
            websiteMapper.updateByPrimaryKeySelective(updateWebsite);
        }

        AgentDomain agentDomain = domainMapper.selectByAgentId(param.getAgentId());
        if (agentDomain == null) {
            agentDomain = new AgentDomain();
            agentDomain.setAgentId(param.getAgentId());
            agentDomain.setName(param.getName());
            agentDomain.setCreateTime(new Date());
            domainMapper.insertSelective(agentDomain);
        } else {
            AgentDomain updateDomain = new AgentDomain();
            updateDomain.setId(agentDomain.getId());
            updateDomain.setName(param.getName());
            updateDomain.setUpdateTime(new Date());
            domainMapper.updateByPrimaryKeySelective(updateDomain);
        }

        AgentContract agentContract = contractMapper.selectByAgentId(param.getAgentId());
        if (agentContract == null) {
            agentContract = new AgentContract();
            agentContract.setAgentId(param.getAgentId());

            if (StringUtils.isNotBlank(param.getSign_url())) {
                agentContract.setSignUrl(param.getSign_url());
            }
            if (StringUtils.isNotBlank(param.getSeal_url())) {
                agentContract.setSealUrl(param.getSeal_url());
            }

            agentContract.setCreateTime(new Date());
            contractMapper.insertSelective(agentContract);
        } else {
            AgentContract updateCon = new AgentContract();
            updateCon.setId(agentContract.getId());

            if (StringUtils.isNotBlank(param.getSign_url())) {
                updateCon.setSignUrl(param.getSign_url());
            }
            if (StringUtils.isNotBlank(param.getSeal_url())) {
                updateCon.setSealUrl(param.getSeal_url());
            }

            updateCon.setUpdateTime(new Date());
            contractMapper.updateByPrimaryKeySelective(updateCon);
        }
        return R.ok();
    }

    @Override
    public R findDomainInfo(Long agentId) {
        return R.ok(domainMapper.selectByAgentId(agentId));
    }

    @Override
    public R updateDomainInfo(AgentDomain param) {
        param.setId(null);
        AgentDomain agentDomain = domainMapper.selectByAgentId(param.getAgentId());
        if (agentDomain == null) {
            domainMapper.insertSelective(param);
        } else {
            param.setId(agentDomain.getId());
            domainMapper.updateByPrimaryKeySelective(param);
        }
        return R.ok();
    }

    @Override
    public R findCustService(Long agentId) {
        return R.ok(custServiceMapper.selectByAgentId(agentId));
    }

    @Override
    public R updateCustService(AgentCustService param) {
        param.setId(null);

        AgentCustService agentCustService = custServiceMapper.selectByAgentId(param.getAgentId());
        byte meiqiaStatus = 0;//可用
        if (StringUtils.isNotBlank(param.getMeiqiaEntid())) {
            List<AgentCustService> oldCustList = custServiceMapper.selectByMeiqiaEntid(param.getMeiqiaEntid());
            if (oldCustList.size() == 0) {
                param.setMeiqiaStatus(meiqiaStatus);
            } else if (oldCustList.size() == 1) {
                if (param.getAgentId() != null && oldCustList.get(0).getAgentId().longValue() == param.getAgentId().longValue()) {
                    param.setMeiqiaStatus(meiqiaStatus);
                }else{
                    return R.error("该美洽ID已被使用");
                }
            } else {
                return R.error("该美洽ID已被使用");
            }

        }

        if (agentCustService == null) {
            custServiceMapper.insertSelective(param);
        } else {
            param.setId(agentCustService.getId());
            custServiceMapper.updateByPrimaryKeySelective(param);
        }
        return R.ok();
    }

    @Override
    public R findContract(Long agentId) {
        return R.ok(contractMapper.selectByAgentId(agentId));
    }

    @Override
    public R updateContract(AgentContract param) {
        param.setId(null);
        AgentContract agentContract = contractMapper.selectByAgentId(param.getAgentId());
        if (agentContract == null) {
            contractMapper.insertSelective(param);
        } else {
            param.setId(agentContract.getId());
            contractMapper.updateByPrimaryKeySelective(param);
        }

        return R.ok();
    }

    @Override
    public R findAlipay(Long agentId) {
        return R.ok(alipayMapper.selectByAgentId(agentId));
    }

    @Override
    public R updateAlipay(AgentAlipay param) {
        param.setId(null);
        AgentAlipay agentAlipay = alipayMapper.selectByAgentId(param.getAgentId());
        if (agentAlipay == null) {
            alipayMapper.insertSelective(param);
        } else {
            param.setId(agentAlipay.getId());
            alipayMapper.updateByPrimaryKeySelective(param);
        }

        return R.ok();
    }

    @Override
    public R findWeixinpay(Long agentId) {
        return R.ok(weixinpayMapper.selectByAgentId(agentId));
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public R updateWeixinpay(AgentWeixinpay param, Long sysUserId) {
        param.setId(null);
        AgentWeixinpay agentWeixinpay = weixinpayMapper.selectByAgentId(param.getAgentId());
        if (agentWeixinpay == null) {
            weixinpayMapper.insertSelective(param);
        } else {
            param.setId(agentWeixinpay.getId());
            weixinpayMapper.updateByPrimaryKeySelective(param);
        }

        return R.ok();
    }

    @Override
    public R findWxLogin(Long agentId) {
        if (agentId == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "不是代理商!");
        }
        return R.ok(agentWxLoginMapper.selectByAgentId(agentId));
    }

    @Override
    public R updateWxLogin(AgentWxLogin param) {
        AgentWxLogin agentWxLogin = agentWxLoginMapper.selectByAgentId(param.getAgentId());
        if (agentWxLogin == null) {
            agentWxLoginMapper.insertSelective(param);
        } else {
            param.setId(agentWxLogin.getId());
            agentWxLoginMapper.updateByPrimaryKeySelective(param);
        }
        //代理商更新成待审核状态
        Agent agent = new Agent();
        agent.setId(param.getAgentId());
        agent.setAuditState(AgentAuditStateEnum.IN_REVIEW.getCode());
        int updated = agentMapper.updateByPrimaryKeySelective(agent);
        if (updated < 1) {
            throw new RRException("代理商审核状态更新失败");
        }
        AgentAuditRecord agentAuditRecord = new AgentAuditRecord();
        agentAuditRecord.setAgentId(param.getAgentId());
        agentAuditRecord.setAuditState(AgentAuditStateEnum.IN_REVIEW.getCode());
        agentAuditRecord.setSysUserId(sysUserService.getSysUserId());
        agentAuditRecordMapper.insertSelective(agentAuditRecord);
        return R.ok();
    }


    /**
     * 审核通过
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public R auditApproved(Long agentId, Long sysUserId) {
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        if (!agent.getAuditState().equals(AgentAuditStateEnum.IN_REVIEW.getCode())) {
            throw new RRException("该代理商当前为非待审核状态");
        }
        int updated = agentMapper.updateAuditStateById(agentId, AgentAuditStateEnum.APPROVED.getCode());
        if (updated < 1) {
            throw new RRException("该代理商当前为非待审核状态");
        }

        AgentAuditRecord agentAuditRecord = new AgentAuditRecord();
        agentAuditRecord.setAgentId(agentId);
        agentAuditRecord.setAuditState(AgentAuditStateEnum.APPROVED.getCode());
        agentAuditRecord.setSysUserId(sysUserId);
        agentAuditRecordMapper.insertSelective(agentAuditRecord);
        return R.ok();
    }

    /**
     * 审核驳回
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public R auditRejected(Long agentId, Long sysUserId, String remark) {
        if (agentId == null || StringUtils.isBlank(remark)) {
            throw new RRException("缺少必要参数");
        }
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        if (!agent.getAuditState().equals(AgentAuditStateEnum.IN_REVIEW.getCode())) {
            throw new RRException("该代理商当前为非待审核状态");
        }
        int updated = agentMapper.updateAuditStateById(agentId, AgentAuditStateEnum.REJECTED.getCode());
        if (updated < 1) {
            throw new RRException("该代理商当前为非待审核状态");
        }
        AgentAuditRecord agentAuditRecord = new AgentAuditRecord();
        agentAuditRecord.setAgentId(agentId);
        agentAuditRecord.setAuditState(AgentAuditStateEnum.REJECTED.getCode());
        agentAuditRecord.setSysUserId(sysUserId);
        agentAuditRecord.setRemark(remark);
        agentAuditRecordMapper.insertSelective(agentAuditRecord);
        return R.ok();
    }

    /**
     * 查询审核状态
     */
    @Override
    public R auditState(Long agentId) {
        if (agentId == null) {
            throw new RRException("缺少必要参数");
        }
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        Map<String, Object> map = new HashMap<>();
        map.put("auditState", agent.getAuditState());
        String remark = "";
        if (agent.getAuditState().equals(AgentAuditStateEnum.REJECTED.getCode())) {
            //已驳回，则查询驳回原因
            AgentAuditRecord agentAuditRecord = agentAuditRecordMapper.queryLastOneOfRejectedByAgentId(agentId);
            if (agentAuditRecord != null) {
                remark = agentAuditRecord.getRemark();
            }
        }
        map.put("remark", remark);
        return R.ok(map);
    }


}
