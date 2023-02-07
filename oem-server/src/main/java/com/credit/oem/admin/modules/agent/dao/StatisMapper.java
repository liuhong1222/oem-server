package com.credit.oem.admin.modules.agent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 没有实体类，转为统计查询使用
 */
@Repository
@Mapper
public interface StatisMapper {

    Map getUserRechargeMoneyAndNumber(Map map);

    List<Map> getUserRechargeMoneyAndNumber2(@Param("list") List list);

}
