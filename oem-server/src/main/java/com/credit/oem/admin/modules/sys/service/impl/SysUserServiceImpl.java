package com.credit.oem.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.credit.oem.admin.common.utils.Constant;
import com.credit.oem.admin.common.utils.PageUtils;
import com.credit.oem.admin.common.utils.Query;
import com.credit.oem.admin.modules.agent.constant.AgentConstant;
import com.credit.oem.admin.modules.agent.dao.AgentMapper;
import com.credit.oem.admin.modules.agent.dao.AgentSysUserMapper;
import com.credit.oem.admin.modules.agent.entity.Agent;
import com.credit.oem.admin.modules.sys.dao.SysUserDao;
import com.credit.oem.admin.modules.sys.dao.SysUserRoleDao;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.entity.SysUserRoleEntity;
import com.credit.oem.admin.modules.sys.service.SysRoleService;
import com.credit.oem.admin.modules.sys.service.SysUserRoleService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    SysUserDao sysUserDao;

    @Autowired
    AgentMapper agentMapper;

    @Autowired
    AgentSysUserMapper agentSysUserMapper;

    @Autowired
    SysUserRoleDao sysUserRoleDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        Long createUserId = (Long) params.get("createUserId");
        List<SysUserRoleEntity> sysUserRoleEntityList = sysUserRoleDao.selectList(new EntityWrapper<SysUserRoleEntity>().eq("role_id", "1"));
        List<Long> userIdList = sysUserRoleEntityList.stream().map(SysUserRoleEntity::getUserId).collect(Collectors.toList());
        Page<SysUserEntity> page = this.selectPage(
                new Query<SysUserEntity>(params).getPage(),
                new EntityWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .eq(createUserId != null, "create_user_id", createUserId).in("user_id", userIdList)
        );
        return new PageUtils(page);
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        this.insert(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        this.updateById(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    public void deleteBatch(Long[] userId) {
        this.deleteBatchIds(Arrays.asList(userId));
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    /**
     * 根据userId，更新用户名和手机号
     */
    @Override
    public void updateUserNameAndMobileByUserId(Long userId, String userName, String mobile) {
        sysUserDao.updateUserNameAndMobileByUserId(userId, userName, mobile);
    }


    /**
     * 根据userId，更新代理商副账号信息
     */
    @Override
    public void updateSlaverSysUserByUserId(Long userId, String userName, String mobile, String realName,String email) {
        sysUserDao.updateSlaverSysUserByUserId(userId, userName, mobile, realName,email);
    }

    /**
     * 更新密码
     */
    @Override
    public void updatePasswordByUserId(Long userId, String password) {
        sysUserDao.updatePasswordByUserId(userId, password);
    }

    @Override
    public void updateEmailByUserId(Long userId, String email) {
        sysUserDao.updateEmailByUserId(userId, email);
    }

    @Override
    public void updateRealNameAndEmailByUserId(Long userId, String realName, String email) {
        sysUserDao.updateRealNameAndEmailByUserId(userId, realName, email);
    }

    /**
     * 检查角色是否越权  TODO 判断是否越权
     */
    private void checkRole(SysUserEntity user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

//		//查询用户创建的角色列表
//		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());
//
//		//判断是否越权
//		if(!roleIdList.containsAll(user.getRoleIdList())){
//			throw new RRException("新增用户所选角色，不是本人创建");
//		}
    }

    @Override
    public Long getSysUserId() {
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUserEntity == null) {
            throw new AuthorizationException();
        }
        return sysUserEntity.getUserId();
    }

    @Override
    public Long selectAgentIdBySysUserId(Long sysUserId) {
        if (sysUserId == null) {
            return null;
        }
        Agent agent = agentMapper.selectAgentBySysUserId(sysUserId);
        if (agent != null) {
            return agent.getId();
        }
        return null;
    }

    @Override
    public boolean judgeIfAdmin(Long sysUserId) {
        if (sysUserId == null) {
            return false;
        }
        List<Long> list = sysUserRoleDao.queryRoleIdList(sysUserId);
        for (Long roleId : list) {
            if (AgentConstant.Admin_Role_Id.equals(roleId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 禁用账号
     */
    @Override
    public void disableByUserId(Long sysUserId) {
        sysUserDao.disableByUserId(sysUserId);
    }

    /**
     * 启用账号
     */
    @Override
    public void enableByUserId(Long sysUserId) {
        sysUserDao.enableByUserId(sysUserId);
    }

}
