package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.AgentContactMapper;
import com.credit.oem.admin.modules.agent.dao.AgentSysUserMapper;
import com.credit.oem.admin.modules.agent.entity.AgentContact;
import com.credit.oem.admin.modules.agent.entity.AgentSysUser;
import com.credit.oem.admin.modules.agent.model.data.AgentSysUserData;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentSysUserUpdateParam;
import com.credit.oem.admin.modules.agent.service.AgentSysUserService;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.credit.oem.admin.modules.sys.service.SysUserTokenService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenzj
 * @since 2018/8/17
 */
@Service
public class AgentSysUserServiceImpl implements AgentSysUserService {

    @Autowired
    AgentSysUserMapper agentSysUserMapper;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    AgentContactMapper agentContactMapper;

    @Autowired
    SysUserTokenService sysUserTokenService;

    /**
     * 获取代理商账号列表
     */
    @Override
    public PageInfo list(AgentSysUserParam param, Long sysUserId) {
        //1.根据sysUserId获取AgentSysUser
        AgentSysUser agentSysUser = agentSysUserMapper.queryOneBySysUserId(sysUserId);
        Assert.notNull(agentSysUser, "代理商账号不存在");
        Assert.notNull(agentSysUser.getAgentId(), "代理商账号不存在");
        //2.根据agentId获取AgentSysUser列表
        List<AgentSysUser> agentSysUserList = agentSysUserMapper.queryListByAgentId(agentSysUser.getAgentId());
        List<Long> sysUserIdList = agentSysUserList.stream().map(AgentSysUser::getSysUserId).collect(Collectors.toList());
        //3.根据sysUserId列表和手机号获取AgentSysUserData列表
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<AgentSysUserData> list = agentSysUserMapper.queryAgentSysUserDataListByMobileAndUserIdList(param.getMobile(), sysUserIdList);
        PageInfo<AgentSysUserData> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 新增代理商账号
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void saveAgentSysUser(AgentSysUserSaveParam param, Long sysUserId) {
        //验证密码不能为空
        if (StringUtils.isBlank(param.getPassword())) {
            throw new RRException("登录密码不能为空");
        }
        //根据sysUserId获取agentId
        Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserId);
        Assert.notNull(agentId, "该账号没有关联代理商");
        //验证登录账号是否重复
        //查询该手机号的管理员账号
        SysUserEntity userVerify = sysUserService.queryByUserName(param.getMobile());
        Assert.isNull(userVerify, "此手机号已被注册");
        //查询该手机号的代理商列表
        List<AgentContact> agentContactVerifyList = agentContactMapper.queryListByMobile(param.getMobile());
        if (!CollectionUtils.isEmpty(agentContactVerifyList)) {
            throw new RRException("此手机号代理商已经存在");
        }
        //保存账号信息
        SysUserEntity sysUser = new SysUserEntity();
        sysUser.setUsername(param.getMobile());
        sysUser.setPassword(param.getPassword());
        sysUser.setMobile(param.getMobile());
        sysUser.setRoleIdList(Collections.singletonList(Constant.AGENT_ROLE_ID));
        sysUser.setRealName(param.getRealName());
        sysUser.setStatus(Constant.COMMON_NORMAL_STATUS_VALUE);
        sysUser.setCreateUserId(sysUserId);
        sysUser.setEmail(param.getEmail());
        sysUserService.save(sysUser);
        //保存代理商和管理员账号
        AgentSysUser agentSysUser = new AgentSysUser();
        agentSysUser.setAgentId(agentId);
        agentSysUser.setSysUserId(sysUser.getUserId());
        agentSysUser.setFlag(Constant.SLAVER_SYS_USER_FLAG);
        agentSysUserMapper.insertSelective(agentSysUser);
    }

    /**
     * 修改代理商账号
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void updateAgentSysUser(AgentSysUserUpdateParam param, Long sysUserId) {
        //根据sysUserId获取agentId
        Long sysUserAgentId = sysUserService.selectAgentIdBySysUserId(sysUserId);
        Assert.notNull(sysUserAgentId, "该账号没有关联代理商");
        //判断是否为同一代理商 即判断update的账号agentId和sysUserId对应agentId是否一致
        Long toUpdateAgentId = sysUserService.selectAgentIdBySysUserId(param.getUserId());
        Assert.notNull(toUpdateAgentId, "账号不存在");
        if (!toUpdateAgentId.equals(sysUserAgentId)) {
            throw new RRException("只能修改所属的账号");
        }
        //判断账号是否为副账号
        AgentSysUser agentSysUser = agentSysUserMapper.queryOneBySysUserId(param.getUserId());
        Assert.notNull(agentSysUser, "代理商关联关系不存在");
        if (!agentSysUser.getFlag().equals(Constant.SLAVER_SYS_USER_FLAG)) {
            throw new RRException("主账号不能修改");
        }

        //验证登录账号是否重复
        //查询该手机号的管理员账号
        SysUserEntity userVerify = sysUserService.queryByUserName(param.getMobile());
        if (userVerify != null && !param.getUserId().equals(userVerify.getUserId())) {
            throw new RRException("此手机号已被注册");
        }
        //查询该手机号的代理商列表
        List<AgentContact> agentContactVerifyList = agentContactMapper.queryListByMobile(param.getMobile());
        if (!CollectionUtils.isEmpty(agentContactVerifyList)) {
            throw new RRException("此手机号代理商已经存在");
        }
        sysUserService.updateSlaverSysUserByUserId(param.getUserId(), param.getMobile(), param.getMobile(),param.getRealName(),param.getEmail());
        //更新登录密码
        if (StringUtils.isNotBlank(param.getPassword())) {
            SysUserEntity sysUserEntity = sysUserService.selectById(param.getUserId());
            String password = new Sha256Hash(param.getPassword(), sysUserEntity.getSalt()).toHex();
            sysUserService.updatePasswordByUserId(param.getUserId(), password);
        }
    }

    /**
     * 删除代理商账号
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void deleteAgentSysUser(Long deleteSysUserId, Long sysUserId) {
        //根据sysUserId获取agentId
        Long sysUserAgentId = sysUserService.selectAgentIdBySysUserId(sysUserId);
        Assert.notNull(sysUserAgentId, "该账号没有关联代理商");
        //判断是否为同一代理商 即判断update的账号agentId和sysUserId对应agentId是否一致
        Long toDeleteAgentId = sysUserService.selectAgentIdBySysUserId(deleteSysUserId);
        Assert.notNull(toDeleteAgentId, "账号不存在");
        if (!toDeleteAgentId.equals(sysUserAgentId)) {
            throw new RRException("只能删除所属的账号");
        }
        //判断账号是否为副账号
        AgentSysUser agentSysUser = agentSysUserMapper.queryOneBySysUserId(deleteSysUserId);
        Assert.notNull(agentSysUser, "代理商关联关系不存在");
        if (!agentSysUser.getFlag().equals(Constant.SLAVER_SYS_USER_FLAG)) {
            throw new RRException("主账号不能删除");
        }
        Long[] sysUserIds = {deleteSysUserId};
        sysUserService.deleteBatch(sysUserIds);
        agentSysUserMapper.deleteByPrimaryKey(agentSysUser.getId());
        //登出该账号
        sysUserTokenService.logout(deleteSysUserId);
    }


    /**
     * 查找并验证代理商主账号信息
     */
    @Override
    public AgentSysUser findAndVerifyMasterAgentSysUser(Long sysUserId) {
        //根据代理商id 获取管理员主账号
        AgentSysUser agentSysUser = agentSysUserMapper.queryOneBySysUserId(sysUserId);
        Assert.notNull(agentSysUser, "无权访问此模块，请联系管理员");
        Assert.notNull(agentSysUser.getAgentId(), "无权访问此模块，请联系管理员");
        return agentSysUser;
    }


}
