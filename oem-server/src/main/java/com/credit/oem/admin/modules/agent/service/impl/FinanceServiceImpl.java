package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constant.AgentConstant;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.AgentOrderMapper;
import com.credit.oem.admin.modules.agent.dao.AgentSysUserMapper;
import com.credit.oem.admin.modules.agent.dao.CreUserAccountMapper;
import com.credit.oem.admin.modules.agent.dao.NumberCheckLogMapper;
import com.credit.oem.admin.modules.agent.dao.TrdOrderMapper;
import com.credit.oem.admin.modules.agent.entity.AgentOrder;
import com.credit.oem.admin.modules.agent.entity.AgentSysUser;
import com.credit.oem.admin.modules.agent.entity.CreUserAccount;
import com.credit.oem.admin.modules.agent.entity.TrdOrder;
import com.credit.oem.admin.modules.agent.enums.AgentRechargePayTypeEnum;
import com.credit.oem.admin.modules.agent.enums.CreUserRechargePayTypeEnum;
import com.credit.oem.admin.modules.agent.enums.RechargeStatusEnum;
import com.credit.oem.admin.modules.agent.model.data.*;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import com.credit.oem.admin.modules.agent.service.AgentSysUserService;
import com.credit.oem.admin.modules.agent.service.FinanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzj
 * @since 2018/8/13
 */
@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    AgentOrderMapper agentOrderMapper;

    @Autowired
    AgentSysUserMapper agentSysUserMapper;

    @Autowired
    TrdOrderMapper trdOrderMapper;

    @Autowired
    NumberCheckLogMapper numberCheckLogMapper;

    @Autowired
    AgentSysUserService agentSysUserService;
    
    @Autowired
    CreUserAccountMapper userAccountMapper;

    /**
     * 组装合计信息
     */
    private Map generateTotalMap(FinanceTotalInfoData totalInfoData,boolean hasMoneyData){
        Map<String, Object> totalMap = new HashMap<>();
        //总体条数
        Long number = 0L;
        //总金额
        BigDecimal money = BigDecimal.ZERO;
        if (totalInfoData != null) {
            number = totalInfoData.getNumber();
            money = totalInfoData.getMoney();
        }
        totalMap.put("number", number);
        if (hasMoneyData){
            totalMap.put("money", money);
        }
        return totalMap;
    }

    /**
     * 查询代理商充值记录列表
     */
    @Override
    public PageInfo agentRechargeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceAgentRechargeData> list = agentOrderMapper.queryFinanceAgentRechargeDataList(param);
        PageInfoWithTotalInfo<FinanceAgentRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //组装入账方式
        pageInfo.getList().forEach(e->e.setPayTypeName(AgentRechargePayTypeEnum.getDescri(e.getPayType())));
        //组装合计信息
        pageInfo.setTotalInfo(generateTotalMap(agentOrderMapper.queryFinanceAgentRechargeTotalInfo(param),true));
        return pageInfo;
    }

    /**
     * 查询我的充值记录列表
     */
    @Override
    public PageInfo myRechargeList(FinanceRechargeParam param, Long sysUserId) {
        //查找并验证代理商主账号信息
        AgentSysUser agentSysUser = agentSysUserService.findAndVerifyMasterAgentSysUser(sysUserId);
        param.setAgentId(agentSysUser.getAgentId());
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceMyRechargeData> list = agentOrderMapper.queryFinanceMyRechargeDataList(param);
        PageInfoWithTotalInfo<FinanceMyRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //组装入账方式
        pageInfo.getList().forEach(e->e.setPayTypeName(AgentRechargePayTypeEnum.getDescri(e.getPayType())));
        //组装合计信息
        pageInfo.setTotalInfo(generateTotalMap(agentOrderMapper.queryFinanceMyRechargeTotalInfo(param),true));
        return pageInfo;
    }

    /**
     * 查询客户充值记录列表
     */
    @Override
    public PageInfo userRechargeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRechargeData> list = trdOrderMapper.queryUserRechargeListExcludeGiftAmount(param);
        PageInfoWithTotalInfo<FinanceUserRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //组装入账方式
        pageInfo.getList().forEach(e -> e.setPayTypeName(CreUserRechargePayTypeEnum.getDescri(e.getPayType())));
        //组装审核状态
        pageInfo.getList().forEach(e -> e.setStatusName(RechargeStatusEnum.getDescri(e.getStatus())));
        //组装合计信息
        pageInfo.setTotalInfo(generateTotalMap(trdOrderMapper.queryUserRechargeTotalInfoExcludeGiftAmount(param),true));
        return pageInfo;
    }
    
    /**
     * 查询入账审核列表
     */
    @Override
	public PageInfo userRechargeCheckList(FinanceRechargeParam param) {
    	PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRechargeCheckData> list = trdOrderMapper.queryUserRechargeCheckList(param);
        PageInfoWithTotalInfo<FinanceUserRechargeCheckData> pageInfo = new PageInfoWithTotalInfo<>(list);        
        return pageInfo;
	}

    /**
     * 查询客户退款记录列表
     */
    @Override
    public PageInfo userRefundList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRefundData> list = trdOrderMapper.queryUserRefundList(param);
        PageInfoWithTotalInfo<FinanceUserRefundData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //组装入账方式
        pageInfo.getList().forEach(e -> e.setPayTypeName(CreUserRechargePayTypeEnum.getDescri(e.getPayType())));
        //组装合计信息
        pageInfo.setTotalInfo(generateTotalMap(trdOrderMapper.queryUserRefundTotalInfo(param),true));
        return pageInfo;
    }


    /**
     * 查询客户消耗记录列表
     */
    @Override
    public PageInfo userConsumeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserConsumeData> list = numberCheckLogMapper.queryUserConsumeList(param);
        PageInfoWithTotalInfo<FinanceUserConsumeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //组装合计信息
        pageInfo.setTotalInfo(generateTotalMap(numberCheckLogMapper.queryUserConsumeTotalInfo(param),false));
        return pageInfo;
    }

	@Override
	public R checkRecharge(String orderId) {
		RechargeInfoData info = trdOrderMapper.getRechargeInfoData(orderId);
		if(info == null) {
			R.error("审核失败，该订单不存在或已删除");
		}
		return R.ok(info);
	}

	@Transactional
	@Override
	public R checkSureRecharge(String orderId, String checkStatus,String remark) {
		TrdOrder resultOrder = new TrdOrder();		
		TrdOrder trdOrder = trdOrderMapper.queryOneWithValidPayTimeById(Integer.parseInt(orderId));
		if(trdOrder == null) {
			return R.error("审核失败，该订单不存在或已删除");
		}
		
		resultOrder.setId(trdOrder.getId());
		resultOrder.setUpdateTime(new Date());
		resultOrder.setVersion(trdOrder.getVersion()+1);
		if(Constant.CHECK_RESULT_REFUSED.equals(checkStatus)) {
			resultOrder.setStatus(AgentConstant.TRD_ORDER_STATUS_REFUSE);
			int count1= trdOrderMapper.updateByPrimaryKeySelective(resultOrder);
			if(count1 != 1) {
				throw new RRException("审核失败，数据库异常");
			}
			AgentOrder tempAgentOrder = agentOrderMapper.findOrderByOrderNo(trdOrder.getOrderNo());
			if(tempAgentOrder == null) {
				return R.error("审核失败，该订单不存在或已删除");
			}
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setId(tempAgentOrder.getId());
			agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_RESUFED);
			agentOrder.setUpdateTime(new Date());
			agentOrder.setRemark(remark);
			int count2 = agentOrderMapper.updateByPrimaryKeySelective(agentOrder);
			if(count2 != 1) {
				throw new RRException("审核失败，数据库异常");
			}
		}else {
			resultOrder.setStatus(AgentConstant.TRD_ORDER_STATUS_SUCCEED);
			int count1= trdOrderMapper.updateByPrimaryKeySelective(resultOrder);
			if(count1 != 1) {
				throw new RRException("审核失败，数据库异常");
			}
			
			List<CreUserAccount> creUserAccountList = userAccountMapper.selectByCreUserId(trdOrder.getCreUserId());
	        CreUserAccount findAccount = null;
	        if (creUserAccountList.size() != 1) {
	        	throw new RRException("审核失败，数据库异常");
	        } else {
	            findAccount = creUserAccountList.get(0);
	        }
			
			CreUserAccount addAccount = getRechargeUpdateAccount(findAccount, trdOrder.getOrderNo(), trdOrder.getNumber(),trdOrder.getFreeNumber()==null?0:trdOrder.getFreeNumber().intValue());
	        int insertCount2 = userAccountMapper.addNumber(addAccount);
	        if (insertCount2 != 1) {
	            throw new RRException("审核失败，数据库异常");
	        }
	        AgentOrder tempAgentOrder = agentOrderMapper.findOrderByOrderNo(trdOrder.getOrderNo());
			if(tempAgentOrder == null) {
				return R.error("审核失败，该订单不存在或已删除");
			}
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setId(tempAgentOrder.getId());
			agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_SUCCESS);
			agentOrder.setUpdateTime(new Date());
			agentOrder.setRemark(remark);
			int count2 = agentOrderMapper.updateByPrimaryKeySelective(agentOrder);
			if(count2 != 1) {
				throw new RRException("审核失败，数据库异常");
			}
		}
		
		
		return R.ok();
	}
	
	private CreUserAccount getRechargeUpdateAccount(CreUserAccount findAccount, String orderNo, Integer number,Integer freeNumber) {
        CreUserAccount addAccount = new CreUserAccount();
        addAccount.setId(findAccount.getId());
        addAccount.setCreUserId(findAccount.getCreUserId());

        if (orderNo.substring(0, 4).equals("CLRQ")) {
            // 账号二次清洗充值回调
            addAccount.setRqAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLSH")) {
            // 空号检测充值回调
            addAccount.setAccount(number+freeNumber);
        } else if (orderNo.substring(0, 4).equals("CLKH")) {
            // 空号API充值回调
            addAccount.setApiAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLTC")) {
            // 证件号码一致性校验回调
            addAccount.setTcAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFC")) {
            // 银行卡四要素认证回调
            addAccount.setFcAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLMS")) {
            // 运营商在线号码状态查询回调
            addAccount.setMsAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLCT")) {
            // 运营商三要素回调
            addAccount.setCtAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFI")) {
            // 人证比对回调
            addAccount.setFiAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFF")) {
            // 一比一人脸比对回调
            addAccount.setFfAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLCL")) {
            // 活体检测回调
            addAccount.setClAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLIO")) {
            // 身份证ocr回调
            addAccount.setIdocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLLO")) {
            // 营业执照ocr回调
            addAccount.setBlocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLBO")) {
            //银行卡ocr回调
            addAccount.setBocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLDO")) {
            // 驾驶证ocr回调
            addAccount.setDocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("FCJX")) {
            //银行卡鉴权精细回调
            addAccount.setFcAccount(number);
        }
        System.out.println("充值的总条数为：" + addAccount.getAccount() + ", 其中赠送的条数为：" + freeNumber);
        return addAccount;
    }
}
