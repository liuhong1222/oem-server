package com.credit.oem.admin.modules.agent.service;


import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
import org.springframework.stereotype.Service;

/**
 * copyright (C), 2018-2018, 创蓝253
 *
 * @author zhangx
 * @fileName ManagerService
 * @date 2018/5/18 19:27
 * @description
 */
@Service
public interface AuthInfoService {

    R custList(CustInfoParam param);

    R custList2(CustInfoParam param);

    R findPersonCustById(Integer id, Integer creUserId);

    R findCompanyCustById(Integer id, Integer creUserId);
    
    R createCust(String mobile,String password);

//    R updatePerson(Integer userId, IdCardInfo idCardInfo, String mail);
//
//    R updateCompany(Integer userId, BusinessLicenceInfo businessLicenceInfo);

//    R updateUserStatus(Integer userId, Integer status, String comment);
//
//    R updateUserStatus(Integer userId, Integer status, String comment, IdCardInfo idCardInfo);
//
//    R updateUserStatus(Integer userId, Integer status, String comment, BusinessLicenceInfo businessLicenceInfo);


}
