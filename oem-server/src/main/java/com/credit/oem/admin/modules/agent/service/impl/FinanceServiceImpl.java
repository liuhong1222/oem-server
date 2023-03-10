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
     * ??????????????????
     */
    private Map generateTotalMap(FinanceTotalInfoData totalInfoData,boolean hasMoneyData){
        Map<String, Object> totalMap = new HashMap<>();
        //????????????
        Long number = 0L;
        //?????????
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
     * ?????????????????????????????????
     */
    @Override
    public PageInfo agentRechargeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceAgentRechargeData> list = agentOrderMapper.queryFinanceAgentRechargeDataList(param);
        PageInfoWithTotalInfo<FinanceAgentRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //??????????????????
        pageInfo.getList().forEach(e->e.setPayTypeName(AgentRechargePayTypeEnum.getDescri(e.getPayType())));
        //??????????????????
        pageInfo.setTotalInfo(generateTotalMap(agentOrderMapper.queryFinanceAgentRechargeTotalInfo(param),true));
        return pageInfo;
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public PageInfo myRechargeList(FinanceRechargeParam param, Long sysUserId) {
        //???????????????????????????????????????
        AgentSysUser agentSysUser = agentSysUserService.findAndVerifyMasterAgentSysUser(sysUserId);
        param.setAgentId(agentSysUser.getAgentId());
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceMyRechargeData> list = agentOrderMapper.queryFinanceMyRechargeDataList(param);
        PageInfoWithTotalInfo<FinanceMyRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //??????????????????
        pageInfo.getList().forEach(e->e.setPayTypeName(AgentRechargePayTypeEnum.getDescri(e.getPayType())));
        //??????????????????
        pageInfo.setTotalInfo(generateTotalMap(agentOrderMapper.queryFinanceMyRechargeTotalInfo(param),true));
        return pageInfo;
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public PageInfo userRechargeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRechargeData> list = trdOrderMapper.queryUserRechargeListExcludeGiftAmount(param);
        PageInfoWithTotalInfo<FinanceUserRechargeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //??????????????????
        pageInfo.getList().forEach(e -> e.setPayTypeName(CreUserRechargePayTypeEnum.getDescri(e.getPayType())));
        //??????????????????
        pageInfo.getList().forEach(e -> e.setStatusName(RechargeStatusEnum.getDescri(e.getStatus())));
        //??????????????????
        pageInfo.setTotalInfo(generateTotalMap(trdOrderMapper.queryUserRechargeTotalInfoExcludeGiftAmount(param),true));
        return pageInfo;
    }
    
    /**
     * ????????????????????????
     */
    @Override
	public PageInfo userRechargeCheckList(FinanceRechargeParam param) {
    	PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRechargeCheckData> list = trdOrderMapper.queryUserRechargeCheckList(param);
        PageInfoWithTotalInfo<FinanceUserRechargeCheckData> pageInfo = new PageInfoWithTotalInfo<>(list);        
        return pageInfo;
	}

    /**
     * ??????????????????????????????
     */
    @Override
    public PageInfo userRefundList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserRefundData> list = trdOrderMapper.queryUserRefundList(param);
        PageInfoWithTotalInfo<FinanceUserRefundData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //??????????????????
        pageInfo.getList().forEach(e -> e.setPayTypeName(CreUserRechargePayTypeEnum.getDescri(e.getPayType())));
        //??????????????????
        pageInfo.setTotalInfo(generateTotalMap(trdOrderMapper.queryUserRefundTotalInfo(param),true));
        return pageInfo;
    }


    /**
     * ??????????????????????????????
     */
    @Override
    public PageInfo userConsumeList(FinanceRechargeParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<FinanceUserConsumeData> list = numberCheckLogMapper.queryUserConsumeList(param);
        PageInfoWithTotalInfo<FinanceUserConsumeData> pageInfo = new PageInfoWithTotalInfo<>(list);
        //??????????????????
        pageInfo.setTotalInfo(generateTotalMap(numberCheckLogMapper.queryUserConsumeTotalInfo(param),false));
        return pageInfo;
    }

	@Override
	public R checkRecharge(String orderId) {
		RechargeInfoData info = trdOrderMapper.getRechargeInfoData(orderId);
		if(info == null) {
			R.error("?????????????????????????????????????????????");
		}
		return R.ok(info);
	}

	@Transactional
	@Override
	public R checkSureRecharge(String orderId, String checkStatus,String remark) {
		TrdOrder resultOrder = new TrdOrder();		
		TrdOrder trdOrder = trdOrderMapper.queryOneWithValidPayTimeById(Integer.parseInt(orderId));
		if(trdOrder == null) {
			return R.error("?????????????????????????????????????????????");
		}
		
		resultOrder.setId(trdOrder.getId());
		resultOrder.setUpdateTime(new Date());
		resultOrder.setVersion(trdOrder.getVersion()+1);
		if(Constant.CHECK_RESULT_REFUSED.equals(checkStatus)) {
			resultOrder.setStatus(AgentConstant.TRD_ORDER_STATUS_REFUSE);
			int count1= trdOrderMapper.updateByPrimaryKeySelective(resultOrder);
			if(count1 != 1) {
				throw new RRException("??????????????????????????????");
			}
			AgentOrder tempAgentOrder = agentOrderMapper.findOrderByOrderNo(trdOrder.getOrderNo());
			if(tempAgentOrder == null) {
				return R.error("?????????????????????????????????????????????");
			}
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setId(tempAgentOrder.getId());
			agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_RESUFED);
			agentOrder.setUpdateTime(new Date());
			agentOrder.setRemark(remark);
			int count2 = agentOrderMapper.updateByPrimaryKeySelective(agentOrder);
			if(count2 != 1) {
				throw new RRException("??????????????????????????????");
			}
		}else {
			resultOrder.setStatus(AgentConstant.TRD_ORDER_STATUS_SUCCEED);
			int count1= trdOrderMapper.updateByPrimaryKeySelective(resultOrder);
			if(count1 != 1) {
				throw new RRException("??????????????????????????????");
			}
			
			List<CreUserAccount> creUserAccountList = userAccountMapper.selectByCreUserId(trdOrder.getCreUserId());
	        CreUserAccount findAccount = null;
	        if (creUserAccountList.size() != 1) {
	        	throw new RRException("??????????????????????????????");
	        } else {
	            findAccount = creUserAccountList.get(0);
	        }
			
			CreUserAccount addAccount = getRechargeUpdateAccount(findAccount, trdOrder.getOrderNo(), trdOrder.getNumber(),trdOrder.getFreeNumber()==null?0:trdOrder.getFreeNumber().intValue());
	        int insertCount2 = userAccountMapper.addNumber(addAccount);
	        if (insertCount2 != 1) {
	            throw new RRException("??????????????????????????????");
	        }
	        AgentOrder tempAgentOrder = agentOrderMapper.findOrderByOrderNo(trdOrder.getOrderNo());
			if(tempAgentOrder == null) {
				return R.error("?????????????????????????????????????????????");
			}
			AgentOrder agentOrder = new AgentOrder();
			agentOrder.setId(tempAgentOrder.getId());
			agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_SUCCESS);
			agentOrder.setUpdateTime(new Date());
			agentOrder.setRemark(remark);
			int count2 = agentOrderMapper.updateByPrimaryKeySelective(agentOrder);
			if(count2 != 1) {
				throw new RRException("??????????????????????????????");
			}
		}
		
		
		return R.ok();
	}
	
	private CreUserAccount getRechargeUpdateAccount(CreUserAccount findAccount, String orderNo, Integer number,Integer freeNumber) {
        CreUserAccount addAccount = new CreUserAccount();
        addAccount.setId(findAccount.getId());
        addAccount.setCreUserId(findAccount.getCreUserId());

        if (orderNo.substring(0, 4).equals("CLRQ")) {
            // ??????????????????????????????
            addAccount.setRqAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLSH")) {
            // ????????????????????????
            addAccount.setAccount(number+freeNumber);
        } else if (orderNo.substring(0, 4).equals("CLKH")) {
            // ??????API????????????
            addAccount.setApiAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLTC")) {
            // ?????????????????????????????????
            addAccount.setTcAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFC")) {
            // ??????????????????????????????
            addAccount.setFcAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLMS")) {
            // ???????????????????????????????????????
            addAccount.setMsAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLCT")) {
            // ????????????????????????
            addAccount.setCtAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFI")) {
            // ??????????????????
            addAccount.setFiAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLFF")) {
            // ???????????????????????????
            addAccount.setFfAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLCL")) {
            // ??????????????????
            addAccount.setClAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLIO")) {
            // ?????????ocr??????
            addAccount.setIdocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLLO")) {
            // ????????????ocr??????
            addAccount.setBlocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLBO")) {
            //?????????ocr??????
            addAccount.setBocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("CLDO")) {
            // ?????????ocr??????
            addAccount.setDocrAccount(number);
        } else if (orderNo.substring(0, 4).equals("FCJX")) {
            //???????????????????????????
            addAccount.setFcAccount(number);
        }
        System.out.println("????????????????????????" + addAccount.getAccount() + ", ???????????????????????????" + freeNumber);
        return addAccount;
    }
}
