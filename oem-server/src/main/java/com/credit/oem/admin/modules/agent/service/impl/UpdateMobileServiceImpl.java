package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.AgentContactMapper;
import com.credit.oem.admin.modules.agent.dao.AgentMapper;
import com.credit.oem.admin.modules.agent.dao.AgentSysUserMapper;
import com.credit.oem.admin.modules.agent.dao.SmsLogMapper;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.model.param.UpdateSysUserMobileParam;
import com.credit.oem.admin.modules.agent.service.UpdateMobileService;
import com.credit.oem.admin.modules.agent.sms.util.AdminSmsUtil;
import com.credit.oem.admin.modules.sys.dao.SysUserDao;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName SmsServiceImpl
 * author   zhangx
 * date     2018/8/16 13:49
 * description
 */
@Service
public class UpdateMobileServiceImpl implements UpdateMobileService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SmsLogMapper smsLogMapper;

    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentSysUserMapper agentSysUserMapper;

    @Autowired
    AgentContactMapper agentContactMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R sendVerifyCode(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "手机号不正确");
        }
        String regExp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobile);
        if (!m.find()) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "手机号不正确");
        }
        SmsLog lastSmsLog = smsLogMapper.selectOneLatestVerifyCodeByMobile(mobile);
        if (lastSmsLog != null) {
            Date now = new Date();
            Date createTime = lastSmsLog.getCreateTime();
            if (now.getTime() - createTime.getTime() < 60 * 1000) {//1分钟1条
//                return R.error(HttpStatus.SC_BAD_REQUEST, "验证码已发送，请稍等~");
                return R.ok("验证码已发送，请注意查收，有效期5分钟");
            }
        }

        String code = AdminSmsUtil.generateCode(5);
        String msg = "你的验证码:" + code + ",有效期5分钟";
        SmsLog smsLog = new SmsLog();
        smsLog.setSysUserId(sysUserService.getSysUserId());
        smsLog.setMessage(code);
        smsLog.setSmsType(1);
        smsLog.setMobile(mobile);
        smsLog.setCreateTime(new Date());

        int count = smsLogMapper.insertSelective(smsLog);
        if (count == 1) {
            Boolean flag = AdminSmsUtil.sendAdminSmsByMobile(mobile, msg);
            if (flag) {
                return R.ok("验证码已发送，请注意查收，有效期5分钟");
            }
        }
        return R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "验证码发送失败，请重试");
    }

    /**
     * 更新3张表，4个字段，o_sys_user(username,mobile)  o_agent_contact  o_agent
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateMobile(UpdateSysUserMobileParam param) {

        if (param.getNewCode().length() != 5 || param.getOldCode().length() != 5) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "验证码不对~");
        }

        SmsLog oldSms = smsLogMapper.selectOneLatestVerifyCodeByMobile(param.getOldMobile());
        SmsLog newSms = smsLogMapper.selectOneLatestVerifyCodeByMobile(param.getNewMobile());

        String oldCode = param.getOldCode();
        String newCode = param.getNewCode();
        Date now = new Date();
        if (oldSms == null || now.getTime() - oldSms.getCreateTime().getTime() > 10 * 60 * 1000) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "验证码已过期，旧手机号请重发验证码");
        }
        if (newSms == null || now.getTime() - newSms.getCreateTime().getTime() > 10 * 60 * 1000) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "验证码已过期，新手机号请重发验证码");
        }

        if (!oldCode.equals(oldSms.getMessage())) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "验证码不对，请确认");
        }
        if (!newCode.equals(newSms.getMessage())) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "验证码不对，请确认");
        }
        Long sysUserId = sysUserService.getSysUserId();
        Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserId);

        SysUserEntity sysUserEntity = sysUserDao.selectById(sysUserId);
        if (!sysUserEntity.getMobile().equals(param.getOldMobile())) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "旧手机号不对，请刷新重试");
        }

        SysUserEntity checkUser = sysUserDao.queryByUserMobile(param.getNewMobile());
        if (checkUser != null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "新手机号已存在，请检查手机号");
        }

        //更新账号
        int count1 = sysUserDao.updateUserNameAndMobileByUserId(sysUserId, param.getNewMobile(), param.getNewMobile());
        if (agentId != null) {
            AgentSysUser agentSysUser = agentSysUserMapper.queryMasterSysUserByAgentId(agentId);
            //判断是否主账号
            if (agentSysUser != null && agentSysUser.getSysUserId().longValue() == sysUserId.longValue() && agentSysUser.getFlag().intValue() == 0) {
                //如果是主账号，更新agent和联系人
                Agent agent = new Agent();
                agent.setId(agentId);
                agent.setMobile(param.getNewMobile());
                int count2 = agentMapper.updateByPrimaryKeySelective(agent);
                int count3 = agentContactMapper.updateMobileByAgentId(agentId, param.getNewMobile());
                if ((count1 + count2 + count3) != 3) {
                    throw new RRException("更新失败，请重试");
                }
            }
        }
        if (count1 != 1) {
            throw new RRException("更新失败，请重试");
        }
        return R.ok("修改成功");
    }

    @Override
    public R checkMobile(String mobile) {
        List<SysUserEntity> list = sysUserDao.queryListByUserMobile(mobile);
        if (list != null && list.size() > 0) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "新手机号已存在，请检查手机号");
        }
        return R.ok();
    }


}
