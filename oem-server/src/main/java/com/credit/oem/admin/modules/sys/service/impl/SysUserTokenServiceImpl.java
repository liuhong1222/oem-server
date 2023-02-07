package com.credit.oem.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.AgentMapper;
import com.credit.oem.admin.modules.agent.entity.Agent;
import com.credit.oem.admin.modules.sys.dao.SysUserTokenDao;
import com.credit.oem.admin.modules.sys.entity.SysUserTokenEntity;
import com.credit.oem.admin.modules.sys.oauth2.TokenGenerator;
import com.credit.oem.admin.modules.sys.service.SysUserRoleService;
import com.credit.oem.admin.modules.sys.service.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
    //12小时后过期
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    AgentMapper agentMapper;


    @Override
    public R createToken(long userId) {

        Agent agent = agentMapper.selectAgentBySysUserId(userId);
        if (agent != null && agent.getStatus() != null) {
            if (agent.getStatus().intValue() == 0) {
                return R.error("代理商账号已被禁用");
            }
//			if(agent.getStatus().intValue()==2){
//				return R.error("代理商账号已被删除");
//			}

        }

        //生成一个token
        String token = TokenGenerator.generateValue();

        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

        //判断是否生成过token
        SysUserTokenEntity tokenEntity = this.selectById(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //保存token
            this.insert(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            this.updateById(tokenEntity);
        }
        //获取用户角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        R r = R.ok().put("token", token).put("expire", EXPIRE).put("roleIdList", roleIdList);

        return r;
    }

    @Override
    public void logout(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //修改token
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        this.updateById(tokenEntity);
    }
}
