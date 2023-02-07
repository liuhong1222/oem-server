package com.credit.oem.admin.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.credit.oem.admin.common.utils.PageUtils;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	/**
	 * 根据userId，更新用户名和手机号
	 */
	void updateUserNameAndMobileByUserId(Long userId, String userName, String mobile);

	/**
	 * 根据userId，更新代理商副账号
	 */
	void updateSlaverSysUserByUserId(Long userId, String userName, String mobile,String realName,String email);

	/**
	 * 更新密码
	 */
	void updatePasswordByUserId(Long userId, String password);

	/**
	 * 更新邮箱
	 */
	void updateEmailByUserId(Long userId, String email);

    /**
     * 更新真实姓名和邮箱
     */
    void updateRealNameAndEmailByUserId(Long userId, String realName,String email);

    Long getSysUserId();

    /**
     * 登录后获取,只适用于代理商的登录账号
     */
    Long selectAgentIdBySysUserId(Long sysUserId);

    /**
     * 根据登录状态判断是否是管理员
     */
    boolean judgeIfAdmin(Long sysUserId);

	/**
	 * 禁用账号
	 */
	void disableByUserId(Long sysUserId);

    /**
     * 启用账号
     */
    void enableByUserId(Long sysUserId);

}
