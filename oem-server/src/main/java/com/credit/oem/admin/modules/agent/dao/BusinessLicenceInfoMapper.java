package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.BusinessLicenceInfo;
import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BusinessLicenceInfoMapper extends BaseCreditMapper<BusinessLicenceInfo, Integer> {

    /**
     * 根据用户id获取公司信息
     */
    BusinessLicenceInfo selectByUserId(Integer userId);

    int updateByIdAndCreUserIdSelective(BusinessLicenceInfo info);

    int updateByPrimaryKeyWithBLOBs(BusinessLicenceInfo info);

    BusinessLicenceInfo queryOneByCreUserId(Long creUserId);


}
