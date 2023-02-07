package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.DateUtils;
import com.credit.oem.admin.common.utils.MD5Util;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.AgentCreUser;
import com.credit.oem.admin.modules.agent.entity.ApiAccountInfo;
import com.credit.oem.admin.modules.agent.entity.BusinessLicenceInfo;
import com.credit.oem.admin.modules.agent.entity.CreUser;
import com.credit.oem.admin.modules.agent.entity.CreUserAccount;
import com.credit.oem.admin.modules.agent.entity.IdCardInfo;
import com.credit.oem.admin.modules.agent.entity.TrdOrder;
import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import com.credit.oem.admin.modules.agent.service.AuthInfoService;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author chenzj
 * @since 2018/5/23
 */

@Service
public class AuthInfoServiceImpl implements AuthInfoService {

    @Autowired
    IdCardInfoMapper cardInfoMapper;
    @Autowired
    BusinessLicenceInfoMapper businessLicenceInfoMapper;
    @Autowired
    CreUserMapper creUserMapper;
    
    @Autowired
    CreUserAccountMapper creUserAccountMapper;
    
    @Autowired
    TrdOrderMapper trdOrderMapper;
    
    @Autowired
    AgentCreUserMapper agentCreUserMapper;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    StatisMapper statisMapper;

    @Autowired
    AuthInfoMapper authInfoMapper;

    //      `is_auth` int(1) DEFAULT '0' COMMENT '是否认证  0-未认证 1-已提交审核 2- 审核驳回 3-已认证',
    @Override
    public R custList(CustInfoParam param) {
        boolean isAdmin=sysUserService.judgeIfAdmin(sysUserService.getSysUserId());
//     custType   -1,全部,0-个人  1-企业,2其他
        List<Map> list = null;
        initParam(param);
        if ("0".equals(param.getCustType())) {
            list = authInfoMapper.selectListByPersonCustInfoParam(param);
        } else if ("1".equals(param.getCustType())) {
            list = authInfoMapper.selectListByComponyCustInfoParam(param);
        } else if ("2".equals(param.getCustType())) {
            list = authInfoMapper.selectListByOtherCustParam(param);
        } else if ("-1".equals(param.getCustType())) {
            list = authInfoMapper.selectListByAllCustParam(param);
        } else {
            list = authInfoMapper.selectListByAllCustParam(param);
        }
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        setSumMoneyNumber(pageInfo);
        Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());

        for (Map map : list) {
            Long agentId2 = (Long) map.get("agentId");
            map.put("canRefundFlag", "false");
            if (agentId != null && agentId2 != null && agentId.longValue() == agentId2.longValue()) {
                map.put("canRefundFlag", "true");
            }
            map.put("isAdmin",""+isAdmin);

            map.put("number",map.get("number") != null ? map.get("number").toString() : "0");
            map.put("money",map.get("money") != null ? map.get("money").toString() : "0.00");
        }

        return R.ok(pageInfo);
    }

    @Override
    public R custList2(CustInfoParam param) {
         List<Map> list = null;         
         //获取agentId
         Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());
         param.setAgentId(agentId);
         initParam(param);
         list = authInfoMapper.selectListCustParam(param);
         PageInfo<Map> pageInfo = new PageInfo<Map>(list);
         setSumMoneyNumber(pageInfo);
         return R.ok(pageInfo);
    }


    @Override
    public R findPersonCustById(Integer id, Integer creUserId) {
        IdCardInfo idCardInfo = new IdCardInfo();
        if (id != null) {
            idCardInfo = cardInfoMapper.selectByPrimaryKey(id);
        }
        CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId);
        Map map = Maps.newHashMap();
        map.put("idCardInfo", idCardInfo);
        map.put("mail", creUser == null ? "" : creUser.getUserEmail());
        map.put("mobile", creUser.getUserPhone());
        map.put("creUserId", creUser.getId());
        return R.ok(map);
    }

    @Override
    public R findCompanyCustById(Integer id, Integer creUserId) {
        BusinessLicenceInfo businessLicenceInfo = new BusinessLicenceInfo();
        if (id != null) {
            businessLicenceInfo = businessLicenceInfoMapper.selectByPrimaryKey(id);
        }
        CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId);
        Map map = Maps.newHashMap();
        map.put("businessLicenceInfo", businessLicenceInfo);
        map.put("mail", creUser == null ? "" : creUser.getUserEmail());
        map.put("mobile", creUser.getUserPhone());
        map.put("creUserId", creUser.getId());
        return R.ok(map);
    }

    public void initParam(CustInfoParam param) {
        boolean isAdmin = sysUserService.judgeIfAdmin(sysUserService.getSysUserId());
        /*1管理员，2代理商*/
        if (isAdmin) {
            param.setAdminType(1);
            //设置默认查询时间
            if (StringUtils.isBlank(param.getStartTimeStr())) {
                param.setStartTimeStr(DateUtils.format(DateUtils.addDateDays(new Date(), -7)));
            }
            if (StringUtils.isBlank(param.getEndTimeStr())) {
                param.setEndTimeStr(DateUtils.format(new Date()));
            }            
        } else {
            param.setAdminType(2);
        }
        
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        param.setSysUserId(sysUserEntity.getUserId());
        PageHelper.startPage((param.getCurrentPage()==0||param.getCurrentPage()==null)?1:param.getCurrentPage(), 
        		(param.getPageSize()==0||param.getPageSize()==null)?1:param.getPageSize());
    }

	public void setSumMoneyNumber(PageInfo<Map> pageInfo) {
        List<Map> list = pageInfo.getList();
        for (Map map : list) {
            Map searchMap = Maps.newHashMap();
            searchMap.put("creUserId", map.get("creUserId"));
            searchMap.put("productId", "1");
            Map sumMap = statisMapper.getUserRechargeMoneyAndNumber(searchMap);
            if (sumMap != null) {
                map.putAll(sumMap);
                map.put("creUserId", searchMap.get("creUserId"));
            }
        }
    }

	@Transactional
	@Override
	public R createCust(String mobile, String password) {
		Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());
		int existsCount = creUserMapper.getCreUserByUserName(mobile);
		if(existsCount > 0) {
			return R.error("账号已存在，请重新输入");
		}
		
		CreUser creUser = new CreUser();
		creUser.setStaffId(12);
		creUser.setLastLoginIp(null);
		creUser.setLastLoginTime(new Date());
		creUser.setUserPhone(mobile);
		creUser.setDeleteStatus(0);
		creUser.setVersion(0);
		creUser.setCreateUser(12);
		creUser.setCreateTime(new Date());
		creUser.setUpdateBy(12);
		creUser.setUpdateTime(new Date());
		int counts = creUserMapper.saveCreUser(creUser);
		if(counts < 1) {
			return R.error("数据库异常");
		}
		
		CreUserAccount creUserAccount = new CreUserAccount();
		creUserAccount.setCreUserId(creUser.getId());
		creUserAccount.setAccount(5000);
		creUserAccount.setDeleteStatus("0");
		creUserAccount.setVersion(0);
		creUserAccount.setCreateTime(new Date());
		creUserAccount.setUpdateTime(new Date());
		counts = creUserAccountMapper.saveCreUserAccount(creUserAccount);
		if(counts < 1) {
			throw new RRException("数据库异常");
		}
		
		String salt = String.valueOf(System.currentTimeMillis());
		ApiAccountInfo apiAccountInfo = new ApiAccountInfo();
		apiAccountInfo.setCreUserId(creUser.getId());
		apiAccountInfo.setUserName(mobile);
		apiAccountInfo.setPassword(MD5Util.MD5(password+salt).toUpperCase());
		apiAccountInfo.setBdIp(null);
		apiAccountInfo.setResultPwd(null);
		apiAccountInfo.setSalt(salt);
		apiAccountInfo.setFlag(0);
		apiAccountInfo.setCreateTime(new Date());
		apiAccountInfo.setUpdateTime(new Date());
		counts = creUserAccountMapper.saveApiAccountInfo(apiAccountInfo);
		if(counts < 1) {
			throw new RRException("数据库异常");
		}
		
		AgentCreUser agentCreUser = new AgentCreUser();
		agentCreUser.setAgentId(agentId);
		agentCreUser.setCreUserId(Long.valueOf(creUser.getId()));
		agentCreUser.setCreateTime(new Date());
		agentCreUser.setUpdateTime(new Date());
		counts = agentCreUserMapper.saveAgentCreUser(agentCreUser);
		if(counts < 1) {
			throw new RRException("数据库异常");
		}
		
		//保存注册赠送记录
		TrdOrder trdOrder = new TrdOrder();
		trdOrder.setCreUserId(creUser.getId());
		trdOrder.setOrderNo("CLSH_"+String.valueOf(System.currentTimeMillis())+(new Random().nextInt(89)+10));
		trdOrder.setProductsId(4);
		trdOrder.setNumber(5000);
		trdOrder.setMoney(BigDecimal.ZERO);
		trdOrder.setPayType("10");
		trdOrder.setPayTime(new Date());
		trdOrder.setType("1");
		trdOrder.setStatus("1");
		trdOrder.setDeleteStatus("0");
		trdOrder.setVersion(0);
		trdOrder.setCreateTime(new Date());
		trdOrder.setUpdateTime(new Date());
		counts = trdOrderMapper.saveTrdOrder(trdOrder);
		if(counts < 1) {
			throw new RRException("数据库异常");
		}
		
		return R.ok();
	}
    

    //    @Override
//    public R updatePerson(Integer userId, IdCardInfo idCardInfo, String mail) {
//        CreUser creUser = new CreUser();
//        creUser.setId(idCardInfo.getCreUserId());
//        creUser.setUserEmail(mail);
//        creUserMapper.updateByPrimaryKeySelective(creUser);
//        return updateUserStatus(userId, AgentConstant.AUTH_STATUS_PASS, "客户列表修改状态1111", idCardInfo);
//    }
//
//    @Override
//    public R updateCompany(Integer userId, BusinessLicenceInfo businessLicenceInfo) {
//        return updateUserStatus(userId, AgentConstant.AUTH_STATUS_PASS, "客户列表修改状态1111", businessLicenceInfo);
//

//    @Override
//    public R updateUserStatus(Integer userId, Integer status, String comment) {
//        if (userId == null || status == null) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "参数不对");
//        }
//        CreUser creUser = new CreUser();
//        creUser.setId(userId);
//        creUser.setIsAuth(status);
//        creUser.setAuthMemo(comment);
//        creUserMapper.updateByPrimaryKeySelective(creUser);
//        return R.ok();
//    }
//
//    @Override
//    public R updateUserStatus(Integer userId, Integer status, String comment, IdCardInfo idCardInfo) {
//        if (comment == null || comment.length() < 10) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "备注信息不能少于10个字");
//        }
//
//        if (idCardInfo.getId() == null) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "缺少必要参数id");
//        }
//
//        int count = 0;
//        if (idCardInfo.getId() == null) {
//            idCardInfo.setCreUserId(userId);
//            count = cardInfoMapper.insertSelective(idCardInfo);
//        } else {
//            count = cardInfoMapper.updateByPrimaryKeySelective(idCardInfo);
//        }
//
//        if (count != 1) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "检测参数，稍后重试");
//        }
//        return updateUserStatus(userId, status, comment);
//    }
//
//    @Override
//    public R updateUserStatus(Integer userId, Integer status, String comment, BusinessLicenceInfo businessLicenceInfo) {
//        if (comment == null || comment.length() < 10) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "备注信息不能少于10个字");
//        }
////        if (businessLicenceInfo.getId() == null) {
////            return R.error(HttpStatus.SC_BAD_REQUEST, "缺少必要参数id");
////        }
//
//        int count = 0;
//        if (businessLicenceInfo.getId() == null) {
//            businessLicenceInfo.setCreUserId(userId);
//            count = businessLicenceInfoMapper.insertSelective(businessLicenceInfo);
//        } else {
//            count = businessLicenceInfoMapper.updateByPrimaryKeySelective(businessLicenceInfo);
//        }
//
//        if (count != 1) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "检测参数，稍后重试");
//        }
//        return updateUserStatus(userId, status, comment);
//    }
}
