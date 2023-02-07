package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AlipayLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AlipayLogMapper extends BaseCreditMapper<AlipayLog, Long> {

}