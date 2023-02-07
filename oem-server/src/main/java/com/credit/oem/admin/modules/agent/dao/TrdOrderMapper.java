package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.TrdOrder;
import com.credit.oem.admin.modules.agent.model.data.FinanceTotalInfoData;
import com.credit.oem.admin.modules.agent.model.data.FinanceUserRechargeCheckData;
import com.credit.oem.admin.modules.agent.model.data.FinanceUserRechargeData;
import com.credit.oem.admin.modules.agent.model.data.FinanceUserRefundData;
import com.credit.oem.admin.modules.agent.model.data.RechargeInfoData;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import com.credit.oem.admin.modules.agent.model.param.RechargeDetailParam;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TrdOrderMapper extends BaseCreditMapper<TrdOrder, Integer> {

    TrdOrder queryOneWithValidPayTimeById(@Param("id")Integer id);

    /**
     * 获取今日入账金额
     */
    BigDecimal sumMoney(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    List<Map> selectRechargeList(RechargeDetailParam param);

    List<Map> getPackageInfo(@Param("creUserId") Long creUserId);

    List<Map> getAgentAmountByCreUserId(@Param("creUserId") Long creUserId);

    /**
     * 客户充值记录
     */
    List<FinanceUserRechargeData> queryUserRechargeList(FinanceRechargeParam param);

    /**
     * 客户充值记录，不包括赠送金额
     */
    List<FinanceUserRechargeData> queryUserRechargeListExcludeGiftAmount(FinanceRechargeParam param);
    
    List<FinanceUserRechargeCheckData> queryUserRechargeCheckList(FinanceRechargeParam param);

    /**
     * 客户充值记录合计信息
     */
    FinanceTotalInfoData queryUserRechargeTotalInfo(FinanceRechargeParam param);

    /**
     * 客户充值记录合计信息，不包括赠送金额
     */
    FinanceTotalInfoData queryUserRechargeTotalInfoExcludeGiftAmount(FinanceRechargeParam param);

    /**
     * 客户退款记录
     */
    List<FinanceUserRefundData> queryUserRefundList(FinanceRechargeParam param);

    /**
     * 客户退款记录合计信息
     */
    FinanceTotalInfoData queryUserRefundTotalInfo(FinanceRechargeParam param);


    BigDecimal getRechargeAmountSumByAgentId(@Param("agentId") Long agentId);

  	Long getRechargeNumberSumByAgentId(@Param("agentId") Long agentId);

    /**获取赠送条数*/
    Long getGiveAmountSumByCreUserId(@Param("creUserId") Long creUserId);

    /**期望该表以后取代trd_order，获取未同步的数据,1000行*/
    List<TrdOrder> selectSyncTrdOrderList();
    
    RechargeInfoData getRechargeInfoData(String orderId);
    
    int saveTrdOrder(TrdOrder trdOrder);

}
