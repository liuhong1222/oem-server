package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.IdCardInfo;
import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface IdCardInfoMapper extends BaseCreditMapper<IdCardInfo, Integer> {

    IdCardInfo queryOneByCreUserId(Long creUserId);

}
