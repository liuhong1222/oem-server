package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.UserAgentChange;
import com.credit.oem.admin.modules.agent.model.data.UserAgentChangeData;
import com.credit.oem.admin.modules.agent.model.param.UserAgentChangeParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserAgentChangeMapper extends BaseCreditMapper<UserAgentChange, Integer> {

    List<UserAgentChangeData> selectListByParam(UserAgentChangeParam param);

}