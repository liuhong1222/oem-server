package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constant.AgentConstant;
import com.credit.oem.admin.modules.agent.constant.RedisKeys;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.enums.AgentOperationTypeEnum;
import com.credit.oem.admin.modules.agent.enums.AgentOrderRoleTypeEnum;
import com.credit.oem.admin.modules.agent.enums.AgentRechargePayTypeEnum;
import com.credit.oem.admin.modules.agent.model.param.CustRechargeParam;
import com.credit.oem.admin.modules.agent.model.param.CustRefundParam;
import com.credit.oem.admin.modules.agent.service.AgentTrdOrderIdService;
import com.credit.oem.admin.modules.agent.service.CreUserService;
import com.credit.oem.admin.modules.agent.service.CustOrderService;
import com.credit.oem.admin.modules.sys.service.SysConfigService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName OrderServiceImpl
 * author   zhangx
 * date     2018/8/30 13:24
 * description
 */
@Service
public class CustOrderServiceImpl implements CustOrderService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TrdOrderMapper orderMapper;
    @Autowired
    CreUserAccountMapper userAccountMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    ProductPackageMapper packageMapper;
    @Autowired
    CreUserMapper userMapper;
    @Autowired
    CreUserAccountMapper creUserAccountMapper;
    @Autowired
    AgentOrderMapper agentOrderMapper;
    @Autowired
    AgentCreUserMapper agentCreUserMapper;


    @Autowired
    AgentAccountMapper agentAccountMapper;

    @Autowired
    AgentPackageMapper agentPackageMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    AgentTrdOrderIdMapper agentTrdOrderIdMapper;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    AgentTrdOrderIdService agentTrdOrderIdService;
    @Autowired
    CreUserService creUserService;


//    @Override
//    public R selectRechargeList(RechargeDetailParam param) {
//        if (param.getCurrentPage() != null && param.getPageSize() != null) {
//            PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
//        }
//
//        if (param.getPageSize() == null || param.getCurrentPage() == null) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常");
//        }
//        if (StringUtils.isBlank(param.getMobile())) {
//            param.setMobile(null);
//        } else {
//            param.setMobile("%" + param.getMobile() + "%");
//        }
//
////        logger.info(JSON.toJSONString(param));
//        List<Map> list = orderMapper.selectRechargeList(param);
//        for(Map map:list){
//            if("2".equals(map.get("type"))){//退款
//                map.put("money",((BigDecimal)map.get("money")).multiply(BigDecimal.valueOf(-1)));
//                map.put("number",(BigDecimal.valueOf((Integer)map.get("number"))).multiply(BigDecimal.valueOf(-1)));
//            }
//        }
//        PageInfo<Map> pageInfo = new PageInfo<>(list);
//        return R.ok(pageInfo);
//    }


    @Override
    public R getPackageInfo(Long creUserId, String custName) {
        R result = new R();
        List list = orderMapper.getPackageInfo(creUserId);
        result.put("data", list);
        List<Map> list2 = orderMapper.getAgentAmountByCreUserId(creUserId);
        if (list2.size() == 1) {
            result.put("amount", list2.get(0).get("empty_balance"));
        } else {
            throw new RRException("数据异常");
        }
        result.put("custName", custName);
        return result;
    }

    @Override
    public R getPackageInfoById(Long packageId) {
        AgentPackage agentPackage = agentPackageMapper.selectByPrimaryKey(packageId);
        if (agentPackage == null) {
            throw new RRException("参数异常");
        }
        Map map = Maps.newHashMap();
        map.put("price", agentPackage.getMoney().divide(BigDecimal.valueOf(agentPackage.getNumber()), 5, BigDecimal.ROUND_HALF_UP));
        map.put("number", agentPackage.getNumber());
        map.put("money", agentPackage.getMoney());
        R result = new R();
        result.put("data", map);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R recharge(CustRechargeParam param) {

        String flag = sysConfigService.getValue(AgentConstant.AgentRechargeForCust);
        if (!"true".equals(flag)) {
            return R.error(HttpStatus.SC_FORBIDDEN, "暂时关闭充值功能，请联系管理员");
        }

        if (param.getCreUserId() == null || param.getNumber() == null || param.getAmount() == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常");
        }
        if(param.getNumber()>2100000000){
            return R.error(HttpStatus.SC_BAD_REQUEST, "充值条数过大");
        }

        R r = judgeIfCanFund(param.getCreUserId());
        if (!"0".equals(r.get("code").toString())) {
            return r;
        }

        List<AgentCreUser> list = agentCreUserMapper.selectByCreUserId(param.getCreUserId());
        if (list.size() != 1) {
            throw new RRException("同一个用户不能属于多个代理商");
        }
        
        AgentPackage agentPackage = agentPackageMapper.findOne(list.get(0).getAgentId());
        if (agentPackage == null) {
        	throw new RRException("代理商无套餐记录");
		}

        if (agentPackage != null && agentPackage.getNumber() != null && agentPackage.getNumber().intValue() != 1) {
            if (param.getNumber().intValue() != agentPackage.getNumber().intValue() || agentPackage.getMoney().compareTo(param.getAmount()) != 0) {
                return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常");
            }
        }
        return recharge(param, agentPackage);
    }

    /**
     * 判断是否能充值退款
     */
    private R judgeIfCanFund(Integer creUserId) {
        List<AgentCreUser> list = agentCreUserMapper.selectByCreUserId(creUserId);
        if (list.size() > 1) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常,同一用户不能归属于同一个代理商!");
        }
        if (list.size() != 1) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常,该用户不归属于任何一个代理商!");
        }
        AgentCreUser agentCreUser = list.get(0);
        Long agentId1 = agentCreUser.getAgentId();
        Long agentId2 = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());
        if (agentId2 == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常,非代理商不能操作!");
        }
        if (agentId1 == null || agentId2 == null || agentId1.longValue() != agentId2.longValue()) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常,非该用户代理商不能操作!");
        } else {
            return R.ok();
        }
    }

    /**
     * 退款，减少客户增加供应商
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public R refunds(CustRefundParam param) {
        String flag = sysConfigService.getValue(AgentConstant.AgentRefundForCust);
        if (!"true".equals(flag)) {
            return R.error(HttpStatus.SC_FORBIDDEN, "暂时关闭退款功能，请联系管理员");
        }

        if (param.getCreUserId() == null || param.getNumber() == null || param.getAmount() == null ) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常");
        }

        if (param.getNumber() < 0) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "退款条数不能小于0");
        }
        if (param.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "退款金额不能小于0");
        }
//        if (param.getPrice().compareTo(BigDecimal.ZERO) < 0) {
//            return R.error(HttpStatus.SC_BAD_REQUEST, "退款单价不能小于0");
//        }

        R r = judgeIfCanFund(param.getCreUserId());
        if (!"0".equals(r.get("code").toString())) {
            return r;
        }


        CreUserAccount creUserAccount = creUserAccountMapper.selectNormalByCreUserId(param.getCreUserId());
        if (creUserAccount == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数异常");
        }
        Integer account = creUserAccount.getAccount();
        Integer giveAccount = creUserService.getGiveAccountByCreUserId(param.getCreUserId()).intValue();
        if (account == null || (account - giveAccount) < param.getNumber().intValue()) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "退款条数不能大于客户可退条数");
        }
        TrdOrder order = new TrdOrder();
        order.setCreUserId(param.getCreUserId());
        order.setProductsId(4);
        //获取产品支付套餐信息
        order.setNumber(param.getNumber());
        order.setMoney(param.getAmount());
        order.setPayType("8");
        order.setType(param.getType());
        order.setOrderNo("CLSH_" + System.currentTimeMillis());
        order.setStatus(AgentConstant.TRD_ORDER_STATUS_SUCCEED);
        order.setDeleteStatus("0");
        order.setVersion(0);
        order.setPayTime(new Date());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        int count = orderMapper.insertSelective(order);// useGeneratedKeys="true" keyProperty="id"
        if (count != 1) {
            throw new RuntimeException("insertCount1!=1");
        }

        CreUserAccount addAccount = new CreUserAccount();
        addAccount.setId(creUserAccount.getId());
        addAccount.setAccount(param.getNumber() * -1);

        int insertCount2 = userAccountMapper.addNumber(addAccount);
        if (insertCount2 != 1) {
            throw new RRException("insertCount1!=1");
        }        
        //调整redis里的余额
//        this.updateCreUserAccountRedis(creUserId, orderNo, number);
        List<AgentCreUser> list = agentCreUserMapper.selectByCreUserId(param.getCreUserId());
        if (list.size() != 1) {
            throw new RRException("同一个用户不能属于多个代理商");
        }
        
        insertCount2 = agentAccountMapper.addEmptyBalanceByAgentId(list.get(0).getAgentId(), Long.valueOf(param.getNumber()));
        if (insertCount2 != 1) {
            throw new RRException("insertCount1!=1");
        }
        
        AgentOrder agentOrder = updateAgentAccount(list.get(0).getAgentId(), Long.valueOf(param.getNumber()), param.getRemark(), AgentOperationTypeEnum.REFUND.getCode(), AgentRechargePayTypeEnum.USER_REFUND.getCode(),order.getOrderNo());

        insertAgentOrerLog(agentOrder, order, param.getRemark());

        return R.ok();
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public R recharge(CustRechargeParam param, AgentPackage agentPackage) {
        Integer creUserId = param.getCreUserId();

        List<CreUserAccount> creUserAccountList = userAccountMapper.selectByCreUserId(creUserId);
        CreUserAccount findAccount = null;
        if (creUserAccountList.size() != 1) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "充值失败，用户不存在");
        } else {
            findAccount = creUserAccountList.get(0);
        }

        if(findAccount.getAccount()+param.getNumber() >21_0000_0000){
            return R.error(HttpStatus.SC_BAD_REQUEST, "充值条数过大");
        }
        
        AgentAccount agentAccount = agentAccountMapper.queryOneByAgentId(agentPackage.getAgentId());
        if (agentAccount == null) {
        	return R.error(HttpStatus.SC_BAD_REQUEST, "无代理商余额记录");
		}
        
        if (agentAccount.getEmptyBalance() < param.getNumber()+(param.getFreeNumber()==null?0L:param.getFreeNumber())) {
        	return R.error(HttpStatus.SC_BAD_REQUEST, "代理商余额不足以本次客户的充值");
		}
        
        Integer number = param.getNumber().intValue();
        TrdOrder trdOrder = saveOrderLog(param, agentPackage);

        CreUserAccount addAccount = getRechargeUpdateAccount(findAccount, trdOrder.getOrderNo(), number,param.getFreeNumber()==null?0:param.getFreeNumber().intValue());

        int insertCount2 = userAccountMapper.addNumber(addAccount);
        if (insertCount2 != 1) {
            throw new RRException("数据库异常");
        }
        
        insertCount2 = agentAccountMapper.subEmptyBalanceByAgentId(agentPackage.getAgentId(), param.getNumber()+(param.getFreeNumber()==null?0L:param.getFreeNumber()));
        if (insertCount2 != 1) {
            throw new RRException("数据库异常");
        }
        
        //调整redis里的余额
        this.updateCreUserAccountRedis(creUserId, trdOrder.getOrderNo(), number);

        Long agentId = agentPackage.getAgentId();

        Integer payType = param.getPayType() == null ? null : Integer.valueOf(param.getPayType());
        AgentOrder agentOrder = updateAgentAccount(agentId, number * -1L, param.getRemark(), AgentOperationTypeEnum.USER_RECHARGE.getCode(), payType,trdOrder.getOrderNo());

        insertAgentOrerLog(agentOrder, trdOrder, param.getRemark());
        
        

        return R.ok();
    }
    
    private void insertAgentOrerLog(AgentOrder agentOrder, TrdOrder trdOrder, String remark) {
        AgentTrdOrderId agentTrdOrderId = new AgentTrdOrderId();
        agentTrdOrderId.setAgentId(agentOrder.getAgentId());
        agentTrdOrderId.setAgentOrderId(agentOrder.getId());
        agentTrdOrderId.setTrdOrderId(Long.valueOf(trdOrder.getId()));
        agentTrdOrderId.setRemark(remark);
        agentTrdOrderIdMapper.insertSelective(agentTrdOrderId);
        //增加扩展信息
        agentTrdOrderIdService.updateAgentTrdOrderIdInfo(agentTrdOrderId);
    }

    private AgentOrder updateAgentAccount(Long agentId, Long number, String remark, Integer type, Integer payType,String orderNo) {
        AgentOrder agentOrder = new AgentOrder();
        agentOrder.setAgentId(agentId);
        agentOrder.setNumber(number);
        agentOrder.setOrderNo(orderNo);
        agentOrder.setType(type);
        agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_CHECK);
        agentOrder.setRemark(remark);

        if (sysUserService.judgeIfAdmin(sysUserService.getSysUserId())) {
            agentOrder.setRoleType(AgentOrderRoleTypeEnum.MANAGER.getCode());
        } else {
            agentOrder.setRoleType(AgentOrderRoleTypeEnum.AGENT.getCode());
        }
        // 参数 5代理商给用户支付宝，6代理商给用户对公转账，7代理商给用户赠送，8代理商给用户退款
//        `pay_type` tinyint(4) DEFAULT NULL COMMENT '交易类型：1支付宝  2银联  3创蓝充值  4管理员充值  5对公转账  6赠送  7官网充值扣款  8用户退款',
        if (payType.intValue() == 5) {
            agentOrder.setPayType(AgentRechargePayTypeEnum.ALIPAY.getCode());
        } else if (payType.intValue() == 6) {
            agentOrder.setPayType(AgentRechargePayTypeEnum.CORPORATE_ACCOUNT.getCode());
        } else if (payType.intValue() == 7) {
            agentOrder.setPayType(AgentRechargePayTypeEnum.PRESENT.getCode());
        } else if (payType.intValue() == 8) {
            agentOrder.setPayType(AgentRechargePayTypeEnum.USER_REFUND.getCode());
        }

        agentOrderMapper.insertSelective(agentOrder);
        return agentOrder;
    }

    private TrdOrder saveOrderLog(CustRechargeParam param, AgentPackage agentPackage) {
        TrdOrder order = new TrdOrder();
        order.setCreUserId(param.getCreUserId());
        order.setProductsId(agentPackage.getPackageId().intValue());
        //获取产品支付套餐信息
        order.setNumber(param.getNumber().intValue());
        order.setFreeNumber(param.getFreeNumber()==null?0:param.getFreeNumber().intValue());
        order.setMoney(param.getAmount());
        String orderCode = agentPackage.getOrderCode();
        order.setPayType(param.getPayType());
        order.setType(param.getType());
        order.setOrderNo(orderCode + System.currentTimeMillis());
        order.setStatus(AgentConstant.TRD_ORDER_STATUS_SUCCEED);
        order.setDeleteStatus("0");
        order.setVersion(0);
        order.setPayTime(new Date());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        int count = orderMapper.insertSelective(order);// useGeneratedKeys="true" keyProperty="id"
        if (count != 1) {
            throw new RuntimeException("insertCount1!=1");
        }
        return order;
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
        return addAccount;
    }

//    private void saveOperateLog(CustRechargeParam param, ProductPackage productPackage, Product product, CreUserAccount srcAccount, CreUserAccount updateAccount) {
//        ManagerOperateLog operateLog = new ManagerOperateLog();
//        operateLog.setManagerId(param.getManagerId());
//        operateLog.setUserId(param.getUserId());
//        operateLog.setType(ManagerOperateLogTypeEnum.MANAGER_RECHARGE.getType());
//        operateLog.setCommitData(JSON.toJSONString(updateAccount));
//        operateLog.setOriginalData(JSON.toJSONString(srcAccount));
//
//        ManagerData managerDomain = redisTemplateManagerDomain.opsForValue().get(ManagerConstants.MANAGER_TOKEN_PRE + param.getManagerId());
//        StringBuilder sb = new StringBuilder();
//        sb.append(DateUtil.formateDateToStr(new Date()));
//        sb.append("管理员[");
//        sb.append(managerDomain.getLoginName());
//        sb.append("]");
//        sb.append(managerDomain.getMobile());
//        sb.append("为用户[")
//                .append(param.getMobile())
//                .append("]充值[")
//                .append(product.getProductName())
//                .append("]")
//                .append(productPackage.getPackageName())
//                .append(",条数:")
//                .append(param.getNumber() + ",金额:")
//                .append(param.getAmount());
//        operateLog.setRemark(param.getRemark());
//        operateLog.setExtendedData(sb.toString());
//        operateLog.setTableName(ManagerOperateLogTypeEnum.MANAGER_RECHARGE.getTableName());
//        operateLog.setRelateId(Long.valueOf(srcAccount.getId()));
//
//        logger.info("operateLog:" + operateLog);
//        int insertCount1 = operateLogMapper.insertSelective(operateLog);
//        if (insertCount1 != 1) {
//            throw new RuntimeException("insertCount1!=1");
//        }
//    }


    /***
     * 这个方法从原有项目拉取，orderNo在此处为套餐中的order_code
     * @param userId
     * @param orderNo
     * @param counts
     */
    private void updateCreUserAccountRedis(Integer userId, String orderNo, Integer counts) {
        String keys = "";
        String key = "";
        if (orderNo.substring(0, 4).equals("CLRQ")) {
            // 账号二次清洗充值回调
            keys = RedisKeys.getInstance().getRQAPIcountKeys();
            key = RedisKeys.getInstance().getRQAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLKH")) {
            // 空号API充值回调
            keys = RedisKeys.getInstance().getKHAPIcountKeys();
            key = RedisKeys.getInstance().getKHAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLTC")) {
            // 证件号码一致性校验回调
            keys = RedisKeys.getInstance().getIdCardAuthcountKeys();
            key = RedisKeys.getInstance().getIdCardAuthAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLFC")) {
            // 银行卡四要素认证回调
            keys = RedisKeys.getInstance().getBankAuthcountKeys();
            key = RedisKeys.getInstance().getBankAuthAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLMS")) {
            // 运营商在线号码状态查询回调
            keys = RedisKeys.getInstance().getMsAPIcountKeys();
            key = RedisKeys.getInstance().getMsAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLCT")) {
            // 运营商三要素回调
            keys = RedisKeys.getInstance().getMobileThreeAuthcountKeys();
            key = RedisKeys.getInstance().getMobileThreeAuthAPIcountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLFI")) {
            // 人证比对回调
            keys = RedisKeys.getInstance().getFiApicountKeys();
            key = RedisKeys.getInstance().getFiApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLFF")) {
            // 一比一人脸比对回调
            keys = RedisKeys.getInstance().getFfApicountKeys();
            key = RedisKeys.getInstance().getFfApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLCL")) {
            // 活体检测回调
            keys = RedisKeys.getInstance().getClApicountKeys();
            key = RedisKeys.getInstance().getClApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLIO")) {
            // 身份证ocr回调
            keys = RedisKeys.getInstance().getIdocrApicountKeys();
            key = RedisKeys.getInstance().getIdocrApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLLO")) {
            // 营业执照ocr回调
            keys = RedisKeys.getInstance().getBlocrApicountKeys();
            key = RedisKeys.getInstance().getBlocrApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLBO")) {
            //银行卡ocr回调
            keys = RedisKeys.getInstance().getBocrApicountKeys();
            key = RedisKeys.getInstance().getBocrApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("CLDO")) {
            // 驾驶证ocr回调
            keys = RedisKeys.getInstance().getDocrApicountKeys();
            key = RedisKeys.getInstance().getDocrApicountKey(userId.toString());
        } else if (orderNo.substring(0, 4).equals("FCJX")) {
            // 银行卡鉴权精细版回调
            keys = RedisKeys.getInstance().getBankAuthcountKeys();
            key = RedisKeys.getInstance().getBankAuthAPIcountKey(userId.toString());
        }

        if (StringUtils.isNotBlank(keys)) {
            String redisKeys = redisTemplate.opsForValue().get(keys) == null ? null : redisTemplate.opsForValue().get(keys);
            if (StringUtils.isNotBlank(redisKeys)) {
                if (redisKeys.contains(userId.toString() + ",")) {
                    String redisKey = redisTemplate.opsForValue().get(key) == null ? "0" : redisTemplate.opsForValue().get(key);
                    if (StringUtils.isNotBlank(redisKey)) {
//                        Integer tempCounts = Integer.parseInt(redisKey) + counts;
                        redisTemplate.opsForValue().increment(key, counts);
                    }
                }
            }
        }
    }
}
