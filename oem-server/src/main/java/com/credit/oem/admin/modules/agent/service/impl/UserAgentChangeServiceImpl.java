package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.model.data.UserAgentChangeData;
import com.credit.oem.admin.modules.agent.model.param.UserAgentChangeParam;
import com.credit.oem.admin.modules.agent.service.UserAgentChangeService;
import com.credit.oem.admin.modules.agent.util.ParamCheckUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenzj
 * @since 2018/8/11
 */
@Service
public class UserAgentChangeServiceImpl implements UserAgentChangeService {

    @Autowired
    UserAgentChangeMapper userAgentChangeMapper;
    @Autowired
    AgentCreUserMapper agentCreUserMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CreUserMapper creUserMapper;
    @Autowired
    IdCardInfoMapper idCardInfoMapper;
    @Autowired
    BusinessLicenceInfoMapper businessLicenceInfoMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public R changeAgent(Integer creUserId, String outAgentName, String inAgentName, String remark) {
        ParamCheckUtil.notNull(creUserId, "creUserId不能为null");
        ParamCheckUtil.notNull(outAgentName, "转出代理商不能为null");
        ParamCheckUtil.notNull(inAgentName, "转入代理商不能为null");
        ParamCheckUtil.notNull(remark, "备注不能为null");
        if(outAgentName.equals(inAgentName)){
            throw new RRException("不能转入当前代理商！");
        }

        Agent inAgent = agentMapper.queryOneByCompanyName(inAgentName);
        ParamCheckUtil.notNull(inAgent, "转入代理商名称不对!");

        CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId);
        ParamCheckUtil.notNull(remark, "userId不能为null");

        Agent outAgent = agentMapper.queryOneByCreUserId(creUserId);
        ParamCheckUtil.isNotEquals(outAgent.getCompanyName(), outAgentName, "转出代理商错误");
        int count1 = agentCreUserMapper.deleteByCreUserId(creUserId);
        if (count1 != 1) {
            throw new RRException("系统故障，刷新页面重试!");
        }

        AgentCreUser agentCreUser = new AgentCreUser();
        agentCreUser.setAgentId(inAgent.getId());
        agentCreUser.setCreUserId(Long.valueOf(creUserId));
        agentCreUser.setCreateTime(new Date());
        int count2 = agentCreUserMapper.insert(agentCreUser);
        if (count2 != 1) {
            throw new RRException("系统故障，刷新页面重试!");
        }

        UserAgentChange userAgentChange = new UserAgentChange();
        userAgentChange.setCreateTime(new Date());
        userAgentChange.setInAgentId(inAgent.getId());
//        userAgentChange.setInAgentName(inAgent.getCompanyName());
        userAgentChange.setOutAgentId(outAgent.getId());
//        userAgentChange.setOutAgentName(outAgent.getCompanyName());
        userAgentChange.setRemark(remark);
        userAgentChange.setCreuserid(creUserId);
        userAgentChange.setMobile(creUser.getUserPhone());
        userAgentChange.setRegisterTime(creUser.getCreateTime());

        int count3 = userAgentChangeMapper.insertSelective(userAgentChange);
        if (count3 != 1) {
            throw new RRException("系统故障，刷新页面重试!");
        }
        return R.ok();
    }

    @Override
    public R changeAgentList(UserAgentChangeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<UserAgentChangeData> list = userAgentChangeMapper.selectListByParam(param);
        PageInfo<UserAgentChangeData> pageInfo = new PageInfo(list);

        list = list.stream().filter(item -> item != null).map((userAgentChangeData) -> {
            Integer creUserId = userAgentChangeData.getCreUserId();
            if (creUserId != null) {
                CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId);
                if (creUser != null && creUser.getUserType() != null && creUser.getIsAuth() != null && creUser.getIsAuth().intValue() != 0) {
                    Integer creUserType = creUser.getUserType();
                    String custName = "";

                    if (creUserType.equals(0)) {
                        //user_type=0 个人信息查询 idcardinfo
                        IdCardInfo idCardInfo = idCardInfoMapper.queryOneByCreUserId(Long.valueOf(creUserId));
                        if (idCardInfo != null) {
                            custName = idCardInfo.getUsername();
                        }
                    } else if (creUserType.equals(1)) {
                        //user_type=1 企业信息 businesslicenceinfo
                        BusinessLicenceInfo businessLicenceInfo = businessLicenceInfoMapper.queryOneByCreUserId(Long.valueOf(creUserId));
                        if (businessLicenceInfo != null) {
                            custName = businessLicenceInfo.getName();
                        }
                    }
                    userAgentChangeData.setCustName(custName);
                }
            }
            return userAgentChangeData;
        }).collect(Collectors.toList());
        pageInfo.setList(list);

        return R.ok(pageInfo);
    }

    @Override
    public R findCompanyName(String inAgentName) {
        PageHelper.startPage(1, 10);
        if (inAgentName == null) {
            inAgentName = "";
        }
        List<String> list = agentMapper.queryCompanyNameList(inAgentName);
        PageInfo<UserAgentChangeData> pageInfo = new PageInfo(list);
        return R.ok(pageInfo);
    }
}
