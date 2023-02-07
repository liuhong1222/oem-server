package com.credit.oem.admin.modules.agent.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.constant.AgentConstant;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.AgentAccountMapper;
import com.credit.oem.admin.modules.agent.dao.AgentLevelMapper;
import com.credit.oem.admin.modules.agent.dao.AgentOrderMapper;
import com.credit.oem.admin.modules.agent.dao.AlipayLogMapper;
import com.credit.oem.admin.modules.agent.entity.AgentAccount;
import com.credit.oem.admin.modules.agent.entity.AgentLevel;
import com.credit.oem.admin.modules.agent.entity.AgentOrder;
import com.credit.oem.admin.modules.agent.entity.AlipayLog;
import com.credit.oem.admin.modules.agent.enums.AgentOperationTypeEnum;
import com.credit.oem.admin.modules.agent.enums.AgentOrderRoleTypeEnum;
import com.credit.oem.admin.modules.agent.enums.AgentRechargePayTypeEnum;
import com.credit.oem.admin.modules.agent.model.param.FundRechargeParam;
import com.credit.oem.admin.modules.agent.service.FundService;
import com.credit.oem.admin.modules.sys.service.SysConfigService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.google.common.collect.Maps;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * copyright (C), 2018-2018, 创蓝253
 * fileName FundServiceImpl
 * author   zhangx
 * date     2018/8/17 14:10
 * description
 */
@Service
public class FundServiceImpl implements FundService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${alipay_appid}")
    private String alipayAppid;
    @Value("${alipay_payurl}")
    private String alipayPayurl;
    @Value("${alipay_publickey}")
    private String alipayPublickey;
    @Value("${alipay_privatekey}")
    private String alipayPrivatekey;
    @Value("${alipay_callbackurl}")
    private String alipayCallbackurl;

    @Autowired
    AgentOrderMapper agentOrderMapper;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    AgentLevelMapper agentLevelMapper;
    @Autowired
    AgentAccountMapper agentAccountMapper;
    @Autowired
    AlipayLogMapper alipayLogMapper;

    @Autowired
    SysConfigService sysConfigService;

    @Override
    public R recharge(FundRechargeParam param) {

        String flag=sysConfigService.getValue(AgentConstant.AgentRecharge);
        if(!"true".equals(flag)){
            return R.error(HttpStatus.SC_FORBIDDEN,"暂时关闭充值功能，请联系管理员");
        }

        Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserService.getSysUserId());
        if (agentId == null) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "只有代理商才可调用该接口!");
        }

        int numberScale = param.getNumber().scale();
        if (numberScale > 5) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "条数不正确，请重新输入!");
        }
        BigDecimal numberParam = param.getNumber().multiply(BigDecimal.valueOf(10000)).setScale(0, BigDecimal.ROUND_UP);

        AgentLevel agentLevel = agentLevelMapper.queryOneByAgentId(agentId);
        BigDecimal price = agentLevel.getPrice();
        if (price.compareTo(param.getPrice()) != 0) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "单价参数不对，请确认!");
        }

//        if(param.getMoney().scale()!=0){
//            return R.error(HttpStatus.SC_BAD_REQUEST, "金额不对，请确认!");
//        }

        long number = numberParam.longValue();
        BigDecimal money = param.getMoney().multiply(BigDecimal.valueOf(10000));

        long number2 = money.divide(price, 0, BigDecimal.ROUND_UP).longValue();
        if (number != number2) {
            return R.error(HttpStatus.SC_BAD_REQUEST, "参数不对，请检查");
        }

        money = money.setScale(2, BigDecimal.ROUND_UP);

        String orderNo = "O_CLSH_" + UUID.randomUUID().toString().replaceAll("-", "");
        AgentOrder agentOrder = new AgentOrder();
        agentOrder.setOrderNo(orderNo);
        agentOrder.setAgentId(agentId);
        agentOrder.setPrice(price);
        agentOrder.setNumber(number);
        agentOrder.setMoney(money);
        agentOrder.setPayType(AgentRechargePayTypeEnum.ALIPAY.getCode());
        agentOrder.setRemark(param.getRemark());
        agentOrder.setType(AgentOperationTypeEnum.AGENT_RECHARGE.getCode());
        agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_TODO);
        agentOrder.setRoleType(AgentOrderRoleTypeEnum.AGENT.getCode());
        agentOrderMapper.insertSelective(agentOrder);


        // 向支付宝发送请求 生成二维码
        AlipayClient alipayClient = new DefaultAlipayClient(alipayPayurl, alipayAppid, alipayPrivatekey, "json", "utf-8", alipayPublickey, "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest(); // 创建API对应的request类
        String subject = "空号检测产品";
        String storeId = "数据";
        request.setBizContent("{" + "    \"out_trade_no\":\"" + orderNo + "\"," + "    \"total_amount\":\"" + money + "\","
                + "    \"subject\":\"" + subject + "\"," + "    \"store_id\":\"" + storeId + "\","
                + "    \"timeout_express\":\"90m\"}");// 设置业务参数
        request.setNotifyUrl(alipayCallbackurl);

        String requestData = JSON.toJSONString(request);
//        logger.info("AlipayClient request param:" +requestData );

        AlipayLog alipayLog = new AlipayLog();
        alipayLog.setOrderNo(orderNo);
        alipayLog.setRequestData(requestData);
        alipayLog.setType(AgentConstant.ALIPAY_LOG_TYPE_PRECREATE);

        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(request);

            alipayLog.setResponseData(JSON.toJSONString(response));

            if (response.isSuccess()) {
                alipayLog.setSuccessFlag(AgentConstant.ALIPAY_STATUS_SUCCEED);

                JSONObject responseBody = JSONObject.parseObject(response.getBody());
                JSONObject json = JSONObject.parseObject(responseBody.get("alipay_trade_precreate_response").toString());
                if (json.get("code").equals("10000")) {
                    //返回结果
                    Map map = Maps.newHashMap();
                    map.put("payUrl", json.get("qr_code").toString());
                    map.put("orderNo", orderNo);

                    R result = new R();
                    result.put("data", map);
                    return result;
                }
            } else {
                alipayLog.setSuccessFlag(AgentConstant.ALIPAY_STATUS_FAIL);

                logger.error("申请支付异常：【" + response.getBody() + "】");
                return R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getSubMsg());
            }

        } catch (Exception e) {
            alipayLog.setSuccessFlag(AgentConstant.ALIPAY_STATUS_FAIL);

            logger.error("申请支付系统异常", e);
            logger.error("申请支付系统异常：【" + e.getMessage() + "】");
            return R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "申请支付异常,请重试");
        } finally {
            alipayLogMapper.insertSelective(alipayLog);
        }
        return R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "申请支付异常,请重试");
    }

    @Override
    public String rechargeCallBack(Map<String, String> param) throws AlipayApiException {
        String orderNo = param.get("out_trade_no");
        String tradNo = param.get("trade_no");
        String status = param.get("trade_status");
//        String sign = param.get("sign");
        String tradeStatus = param.get("trade_status");

        boolean signCheck = AlipaySignature.rsaCheckV1(param, alipayPublickey, "utf-8", "RSA2");
        if (!signCheck) {
            logger.error("支付宝验签失败,param:" + param);
            return "false";
        }
        AlipayLog alipayLog = new AlipayLog();
        alipayLog.setResponseData(JSON.toJSONString(param));
        alipayLog.setOrderNo(orderNo);
        alipayLog.setType(AgentConstant.ALIPAY_LOG_TYPE_CALLBACK1);
        alipayLogMapper.insertSelective(alipayLog);

        boolean dealFlag = dealRecharge(orderNo, tradNo, tradeStatus, param);
        AlipayLog alipayLog2 = new AlipayLog();
        alipayLog2.setId(alipayLog.getId());
        if (dealFlag) {
            alipayLog2.setSuccessFlag(AgentConstant.ALIPAY_STATUS_SUCCEED);
            return "success";
        } else {
            alipayLog2.setSuccessFlag(AgentConstant.ALIPAY_STATUS_FAIL);
        }
        alipayLogMapper.updateByPrimaryKeySelective(alipayLog2);

        return "success1";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dealRecharge(String orderNo, String tradNo, String tradeStatus, Map<String, String> param) {
        AgentOrder findAgentOrder = agentOrderMapper.findOrderByOrderNo(orderNo);
        if (findAgentOrder == null) {
            logger.error("充值回调异常,未找到orderNo" + orderNo);
            return false;
        }

        BigDecimal money = findAgentOrder.getMoney();
        BigDecimal totalAmount = new BigDecimal(param.get("total_amount").toString());
        if (totalAmount == null || money.compareTo(totalAmount) != 0) {
            logger.error("充值回调异常,充值金额与数据库不一致 " + orderNo);
            return false;
        }

        if (findAgentOrder.getStatus().intValue() == Constant.AGENT_ORDER_STATUS_SUCCESS.intValue()) {
            logger.error("充值回调异常,该orderNo可能已被正常处理" + orderNo);
            return true;
        }

        if (findAgentOrder.getStatus().intValue() != Constant.AGENT_ORDER_STATUS_TODO.intValue()) {
            logger.error("充值回调异常,状态不对orderNo" + orderNo);
            return false;
        }

        String appId = param.get("app_id");
        if (!alipayAppid.equals(appId)) {
            logger.error("充值回调异常,appid与现有服务不一致 " + orderNo);
            return false;
        }

        AgentOrder agentOrder = findAgentOrder;
        AgentOrder updateOrder = new AgentOrder();
        updateOrder.setId(agentOrder.getId());

        updateOrder.setTradeNo(tradNo);
        updateOrder.setPayTime(new Date());
        updateOrder.setVersion(agentOrder.getVersion());
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            updateOrder.setStatus(Constant.AGENT_ORDER_STATUS_SUCCESS);
            int count1 = agentOrderMapper.updateByIdAndVersionSelective(updateOrder);

            AgentAccount agentAccount = agentAccountMapper.queryOneByAgentId(agentOrder.getAgentId());
            if (agentAccount.getVersion() == null) {
                agentAccount.setVersion(0);
            }
            int count2 = agentAccountMapper.addEmptyBalanceByAgentIdAndVersion(agentOrder.getAgentId(), agentOrder.getNumber(), agentAccount.getVersion());

            if ((count1 + count2) != 2) {
                logger.error("充值回调异常:" + orderNo + "," + tradeStatus + "," + tradNo);
                throw new RRException("充值回调异常更新数据异常");
            }
        } else {
            updateOrder.setStatus(Constant.AGENT_ORDER_STATUS_FAILURE);
            int count1 = agentOrderMapper.updateByIdAndVersionSelective(updateOrder);
            if (count1 != 1) {
                throw new RRException("充值回调异常更新数据异常");
            }
        }
        return true;
    }

    @Override
    public R findOrderStatus(String orderNo) {
        Map map = Maps.newHashMap();
        map.put("orderStatus", "Unkown");
        if (!StringUtils.isEmpty(orderNo)) {
            AgentOrder agentOrder = agentOrderMapper.findOrderByOrderNo(orderNo);
            if (agentOrder == null || agentOrder.getId() == null) {
                map.put("orderStatus", "NotExist");
            } else {
                int status = agentOrder.getStatus();
                if (status == Constant.AGENT_ORDER_STATUS_TODO.intValue()) {
                    map.put("orderStatus", "WaitForDeal");
                } else if (status == Constant.AGENT_ORDER_STATUS_SUCCESS.intValue()) {
                    map.put("orderStatus", "Success");
                } else if (status == Constant.AGENT_ORDER_STATUS_FAILURE.intValue()) {
                    map.put("orderStatus", "Fail");
                } else {
                    map.put("orderStatus", "Unkown");
                }
            }
        }

        return R.ok((Object) map);
    }

}

