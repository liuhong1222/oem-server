package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.NumberCheckLog;

import com.credit.oem.admin.modules.agent.model.data.FinanceTotalInfoData;
import com.credit.oem.admin.modules.agent.model.data.FinanceUserConsumeData;
import com.credit.oem.admin.modules.agent.model.param.FinanceRechargeParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;

@Repository
@Mapper
public interface NumberCheckLogMapper extends BaseCreditMapper<NumberCheckLog, String> {


    List<NumberCheckLog> queryListByNotUpdated();

    /**
     * 查询客户消费记录
     */
    List<FinanceUserConsumeData> queryUserConsumeList(FinanceRechargeParam param);

    /**
     * 查询客户消费记录合计信息
     */
    FinanceTotalInfoData queryUserConsumeTotalInfo(FinanceRechargeParam param);

    Long getConsumSumByAgentId(@Param("agentId") Long agentId);

    Long getConsumSum();


}

