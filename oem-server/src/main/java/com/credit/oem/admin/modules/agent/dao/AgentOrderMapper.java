package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentOrder;
import com.credit.oem.admin.modules.agent.model.data.AgentRechargeTotalData;
import com.credit.oem.admin.modules.agent.model.data.FinanceAgentRechargeData;
import com.credit.oem.admin.modules.agent.model.data.FinanceMyRechargeData;
import com.credit.oem.admin.modules.agent.model.data.FinanceTotalInfoData;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface AgentOrderMapper extends BaseCreditMapper<AgentOrder, Long> {

    /**
     * 代理商总充值信息
     */
    List<AgentRechargeTotalData> queryAgentRechargeTotalDataList(@Param("agentIdList") List<Long> agentIdList);

    /**
     * 查询代理商充值记录
     */
    List<FinanceAgentRechargeData> queryFinanceAgentRechargeDataList(FinanceRechargeParam param);

    /**
     * 查询代理商充值记录合计信息
     */
    FinanceTotalInfoData queryFinanceAgentRechargeTotalInfo(FinanceRechargeParam param);

    /**
     * 查询我的充值记录
     */
    List<FinanceMyRechargeData> queryFinanceMyRechargeDataList(FinanceRechargeParam param);

    /**
     * 查询我的充值记录合计信息
     */
    FinanceTotalInfoData queryFinanceMyRechargeTotalInfo(FinanceRechargeParam param);

    /**
     * 代理商充值总金额统计
     */
    BigDecimal sumMoney();

    /**
     * 代理商充值总条数统计
     */
    Long sumNumber();

    /**
     * 统计能升级的代理商数
     */
    Long countAgentCanUpgrade();

    List<AgentOrder> findOrderByOrderNoAndStatus(@Param("orderNo") String orderNo, @Param("status") Integer status);

    int updateByIdAndVersionSelective(AgentOrder agentOrder);

    AgentOrder findOrderByOrderNo(@Param("orderNo") String orderNo);

}
