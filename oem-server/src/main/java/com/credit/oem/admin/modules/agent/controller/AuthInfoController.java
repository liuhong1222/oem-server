//package com.credit.oem.admin.modules.agent.controller;
//
//
//import com.credit.oem.admin.common.utils.R;
//import com.credit.oem.admin.modules.agent.entity.BusinessLicenceInfo;
//import com.credit.oem.admin.modules.agent.entity.IdCardInfo;
//import com.credit.oem.admin.modules.agent.model.param.CustInfoParam;
//import com.credit.oem.admin.modules.agent.service.AuthInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * author zhangx
// * date  2018/7/31 2:17
// */
//@RestController
//@RequestMapping("/open/agent/auth")
//public class AuthInfoController {
//    @Autowired
//    AuthInfoService authInfoService;
//
//    /**
//     * 查询产品列表
//     *
//     * @return
//     */
//    @RequestMapping(value = "/personCustList")
//    public R personCustList(CustInfoParam param) {
//        return authInfoService.personCustList(param);
//    }
//
//    @RequestMapping(value = "/companyCustList")
//    public R companyCustList(CustInfoParam param) {
//        return authInfoService.companyCustList(param);
//    }
//
//    @RequestMapping(value = "/custList")
//    public R custList(CustInfoParam param) {
//        return authInfoService.custList(param);
//    }
//
//
//    @RequestMapping(value = "/findPersonCustById")
//    public R findPersonCustById(Integer id){
//        return authInfoService.findPersonCustById(id);
//    }
//
//    @RequestMapping(value = "/findCompanyCustById")
//    public R findCompanyCustById(Integer id){
//        return authInfoService.findCompanyCustById(id);
//    }
//
//
//    @RequestMapping(value = "/updatePersonStatus", method = RequestMethod.POST)
//    public R updatePersonStatus(Integer userId, Integer status, String comment, IdCardInfo idCardInfo) {
////        try {
////            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////            if(!StringUtils.isNotBlank(effectDate)){
////                idCardInfo.setEffectdate(formatter.parse(effectDate));
////            }
////            if(!StringUtils.isNotBlank(expireDate)){
////                idCardInfo.setExpiredate(formatter.parse(expireDate));
////            }
////        }catch (Exception e){
////            return R.error(HttpStatus.SC_BAD_REQUEST,"时间格式不对");
////        }
//        return authInfoService.updateUserStatus(userId, status, comment, idCardInfo);
//    }
//
//    @RequestMapping(value = "/updateCompanyStatus", method = RequestMethod.POST)
//    public R updateCompanyStatus(Integer userId, Integer status, String comment, BusinessLicenceInfo businessLicenceInfo) {
////        try {
////            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////            if(!StringUtils.isNotBlank(effectDate)){
////                businessLicenceInfo.setEffectdate(formatter.parse(effectDate));
////            }
////            if(!StringUtils.isNotBlank(expireDate)){
////                businessLicenceInfo.setExpiredate(formatter.parse(expireDate));
////            }
////        }catch (Exception e){
////            return R.error(HttpStatus.SC_BAD_REQUEST,"时间格式不对");
////        }
//        return authInfoService.updateUserStatus(userId, status, comment, businessLicenceInfo);
//    }
//
//
////    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
////    public R updateUserStatus(Integer userId, Integer status,String comment) {
////        return authInfoService.updateUserStatus(userId, status,comment);
////    }
//
//
//}
