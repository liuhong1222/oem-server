package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 业务原因新建
 */
@Repository
@Mapper
public interface AuthInfoMapper {

    List<Map> selectListByAllCustParam(CustInfoParam param);

    List<Map> selectListByComponyCustInfoParam(CustInfoParam param);

    List<Map> selectListByPersonCustInfoParam(CustInfoParam param);

    List<Map> selectListByOtherCustParam(CustInfoParam param);

    List<Map> selectListCustParam(CustInfoParam param);

}