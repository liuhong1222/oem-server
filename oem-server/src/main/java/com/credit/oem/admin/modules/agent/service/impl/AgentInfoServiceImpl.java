package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.modules.agent.constant.AgentConstant;
import com.credit.oem.admin.modules.agent.constants.Constant;
import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.enums.AgentAuditStateEnum;
import com.credit.oem.admin.modules.agent.enums.AgentPictureTypeEnum;
import com.credit.oem.admin.modules.agent.enums.AgentRechargePayTypeEnum;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoData;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoDetailData;
import com.credit.oem.admin.modules.agent.model.data.AgentRechargeTotalData;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoUpdateParam;
import com.credit.oem.admin.modules.agent.model.param.AgentRechargeParam;
import com.credit.oem.admin.modules.agent.service.AgentInfoService;
import com.credit.oem.admin.modules.agent.service.OcrService;
import com.credit.oem.admin.modules.agent.service.UploadPictureService;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.service.SysConfigService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.credit.oem.admin.modules.sys.service.SysUserTokenService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenzj
 * @since 2018/8/8
 */
@Service
public class AgentInfoServiceImpl implements AgentInfoService {
    @Autowired
    AgentMapper agentMapper;

    @Autowired
    AgentBasicLevelMapper agentBasicLevelMapper;

    @Autowired
    UploadPictureService uploadPictureService;

    @Autowired
    AgentPictureMapper agentPictureMapper;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    AgentContactMapper agentContactMapper;

    @Autowired
    AgentLevelMapper agentLevelMapper;

    @Autowired
    AgentAccountMapper agentAccountMapper;

    @Autowired
    AgentSysUserMapper agentSysUserMapper;

    @Autowired
    ProductPackageMapper productPackageMapper;

    @Autowired
    AgentPackageMapper agentPackageMapper;

    @Autowired
    AgentOrderMapper agentOrderMapper;

    @Autowired
    SysUserTokenService sysUserTokenService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    OcrService ocrService;

    /**
     * 代理商状态转成状态名
     */
    private static String convertToAgentStatusName(Integer status) {
        if (status.equals(1)) {
            return Constant.AGENT_STATUS_ENABLE;
        } else {
            return Constant.AGENT_STATUS_DISABLE;
        }
    }

    /**
     * 查询代理商列表
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo list(AgentInfoParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<AgentInfoData> list = agentMapper.queryAgentInfoList(param);
        PageInfo<AgentInfoData> pageInfo = new PageInfo<>(list);

        List<AgentInfoData> pageInfoList = pageInfo.getList();
        //获取代理商id列表
        List<Long> agentIdList = pageInfoList.stream().map(AgentInfoData::getAgentId).collect(Collectors.toList());
        //根据代理id列表，获取充值总金额总条数
        List<AgentRechargeTotalData> agentRechargeTotalDataList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(agentIdList)) {
            agentRechargeTotalDataList = agentOrderMapper.queryAgentRechargeTotalDataList(agentIdList);
        }
        Map<Long, AgentRechargeTotalData> agentIdAndAgentRechargeTotalDataMap = new HashMap<>();
        for (AgentRechargeTotalData e : agentRechargeTotalDataList) {
            agentIdAndAgentRechargeTotalDataMap.put(e.getAgentId(), e);
        }
        List<AgentInfoData> resultList = new ArrayList<>();
        //组装充值总金额总条数、能否升级等数据
        if (!CollectionUtils.isEmpty(pageInfoList)) {
            List<AgentBasicLevel> agentBasicLevelList = agentBasicLevelMapper.selectListByNotDeleted();
            Map<Long, AgentBasicLevel> levelIdAndAgentBasicLevelMap = new HashMap<>();
            for (AgentBasicLevel agentBasicLevel : agentBasicLevelList) {
                levelIdAndAgentBasicLevelMap.put(agentBasicLevel.getId(), agentBasicLevel);
            }
            for (AgentInfoData agentInfoData : pageInfoList) {
                AgentInfoData resultAgentInfoData = new AgentInfoData();
                BeanUtils.copyProperties(agentInfoData, resultAgentInfoData);

                //代理商总金额总条数
                resultAgentInfoData.setStatusName(convertToAgentStatusName(agentInfoData.getStatus()));
                AgentRechargeTotalData agentRechargeTotalData = agentIdAndAgentRechargeTotalDataMap.get(agentInfoData.getAgentId());
                if (agentRechargeTotalData != null) {
                    resultAgentInfoData.setTotalRechargeMoney(agentRechargeTotalData.getTotalRechargeMoney());
                    resultAgentInfoData.setTotalRechargeNumber(agentRechargeTotalData.getTotalRechargeNumber());
                } else {
                    resultAgentInfoData.setTotalRechargeMoney(BigDecimal.ZERO);
                    resultAgentInfoData.setTotalRechargeNumber(0L);
                }
                //代理商等级信息
                BigDecimal price = BigDecimal.ZERO;
                Boolean canUpgrade = false;
                String canUpgradeName = Constant.AGENT_LEVEL_CAN_NOT_UPGRADE_NAME;
                AgentBasicLevel agentBasicLevel = levelIdAndAgentBasicLevelMap.get(agentInfoData.getLevelId());
                if (agentBasicLevel != null) {
                    price = agentBasicLevel.getPrice();
                    BigDecimal maxRecharge = agentBasicLevel.getMaxRecharge();
                    //代理商登记能否升级
                    if (resultAgentInfoData.getTotalRechargeMoney() != null && maxRecharge != null &&
                            resultAgentInfoData.getTotalRechargeMoney().compareTo(maxRecharge) > 0) {
                        canUpgrade = true;
                        canUpgradeName = Constant.AGENT_LEVEL_CAN_UPGRADE_NAME;
                    }
                }
                resultAgentInfoData.setPrice(price);
                resultAgentInfoData.setCanUpgrade(canUpgrade);
                resultAgentInfoData.setCanUpgradeName(canUpgradeName);
                resultList.add(resultAgentInfoData);
            }
        }
        pageInfo.setList(resultList);
        return pageInfo;
    }

    /**
     * 上传营业执照图片
     *
     * @param sysUserId
     * @param file
     * @return
     */

    @Override
    public Map<String, String> uploadLicense(Long sysUserId, MultipartFile file) throws Exception {
        AgentPicture agentPicture = uploadPictureService.uploadAgentPictureService(sysUserId, file, AgentPictureTypeEnum.AGENT_LICENSE.getCode(), "");
        String picPath = agentPicture.getPicPath();
        Map<String, String> map = new HashMap<>();
        map.put("licenseUrl", agentPicture.getPicUrl());
        map.put("licensePicNo", agentPicture.getPicNo());
        if (StringUtils.isNotBlank(picPath)) {
            Map<String, String> ocrResultMap = ocrService.agentBizLicenseByFilePath(picPath, String.valueOf(System.currentTimeMillis()));
            if (!CollectionUtils.isEmpty(ocrResultMap)) {
                map.put("companyName", ocrResultMap.get("name"));
                map.put("address", ocrResultMap.get("address"));
                map.put("legalPerson", ocrResultMap.get("person"));
                map.put("licenseNo", ocrResultMap.get("regNum"));
                String establishDate = ocrResultMap.get("establishDate");
                if (StringUtils.isNotBlank(establishDate) && establishDate.length() == 8) {
                    String effectDate = String.format("%s-%s-%s", establishDate.substring(0, 4), establishDate.substring(4, 6), establishDate.substring(6, 8));
                    map.put("effectDate", effectDate);
                }
                String validPeriod = ocrResultMap.get("validPeriod");
                if (StringUtils.isNotBlank(validPeriod) && validPeriod.length() == 8) {
                    String expireDate = String.format("%s-%s-%s", validPeriod.substring(0, 4), validPeriod.substring(4, 6), validPeriod.substring(6, 8));
                    map.put("expireDate", expireDate);
                }
            }
        }
        return map;
    }

    /**
     * 新增代理商
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void saveAgent(AgentInfoSaveParam param, Long sysUserId) {
        //验证密码不能为空
        if (StringUtils.isBlank(param.getPassword())) {
            throw new RRException("登录密码不能为空");
        }
        //验证单价
        if (param.getPrice().compareTo(BigDecimal.ZERO) < 1) {
            throw new RRException("单价必须大于0");
        }
        //验证预警条数
        if (param.getEmptyWarnNumber().compareTo(0L) < 0) {
            throw new RRException("预警条数必须大于0");
        }
        if (StringUtils.isBlank(param.getLicensePicNo())) {
            throw new RRException("营业执照图片不能为空");
        }
        //验证营业执照图片信息是否存在
        AgentPicture agentPictureVerify = agentPictureMapper.queryOneByPicNo(param.getLicensePicNo());
        Assert.notNull(agentPictureVerify, "营业执照图片未照到，请重新上传");
        //验证登录账号是否重复
        //查询该手机号的管理员账号
        SysUserEntity userVerify = sysUserService.queryByUserName(param.getMobile());
        Assert.isNull(userVerify, "此手机号已被注册");
        //查询该手机号的代理商列表
        List<AgentContact> agentContactVerifyList = agentContactMapper.queryListByMobile(param.getMobile());
        if (!CollectionUtils.isEmpty(agentContactVerifyList)) {
            throw new RRException("此手机号代理商已经存在");
        }
        //验证代理商名称是否重复
        Agent agentVerify = agentMapper.queryOneByCompanyName(param.getCompanyName());
        Assert.isNull(agentVerify, "代理商已存在");
        //验证商户编号是否重复
        Agent agentVerifyMchId = agentMapper.queryOneByMchId(param.getMchId());
        Assert.isNull(agentVerifyMchId, "商户编号已存在");
        //验证营业执照编号是否重复
        Agent agentVerifyLicenseNo = agentMapper.queryOneByLicenseNo(param.getLicenseNo());
        Assert.isNull(agentVerifyLicenseNo, "营业执照编号已存在");
        //验证代理商等级是否存在
        AgentBasicLevel agentBasicLevelVerify = agentBasicLevelMapper.selectByPrimaryKey(param.getLevelId());
        Assert.notNull(agentBasicLevelVerify, "代理商等级不存在");
        //1.保存agent
        Agent agent = new Agent();
        BeanUtils.copyProperties(param, agent);
        agent.setLicenseUrl(agentPictureVerify.getPicUrl());
        agent.setAuditState(AgentAuditStateEnum.NOT_SET.getCode());
        agentMapper.insertSelective(agent);
        Long agentId = agent.getId();
        //更新agent_no代理商编号 为000001
        String agentNo = String.format("%06d", agentId);
        agentMapper.updateAgentNoById(agentId, agentNo);
        //2.保存联系人信息
        AgentContact agentContact = new AgentContact();
        BeanUtils.copyProperties(param, agentContact);
        agentContact.setAgentId(agentId);
        agentContact.setName(param.getContactName());
        agentContactMapper.insertSelective(agentContact);
        //3.保存账号信息
        SysUserEntity sysUser = new SysUserEntity();
        sysUser.setUsername(param.getMobile());
        sysUser.setPassword(param.getPassword());
        sysUser.setEmail(param.getEmail());
        sysUser.setMobile(param.getMobile());
        sysUser.setRoleIdList(Collections.singletonList(Constant.AGENT_ROLE_ID));
        sysUser.setRealName(param.getContactName());
        sysUser.setStatus(Constant.COMMON_NORMAL_STATUS_VALUE);
        sysUser.setCreateUserId(sysUserId);
        sysUserService.save(sysUser);

        //4.保存代理商级别
        AgentLevel agentLevel = new AgentLevel();
        agentLevel.setAgentId(agentId);
        agentLevel.setLevelId(param.getLevelId());
        agentLevel.setLevelName(agentBasicLevelVerify.getName());
        agentLevel.setPrice(param.getPrice());
        agentLevelMapper.insertSelective(agentLevel);
        //5.保存代理商账户信息
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setAgentId(agentId);
        agentAccount.setEmptyWarnNumber(Constant.DEFAULT_AGENT_EMPTY_WARN_NUMBER);
        agentAccount.setEmptyWarnNumber(param.getEmptyWarnNumber());
        agentAccountMapper.insertSelective(agentAccount);

        //6.保存代理商和管理员账号
        AgentSysUser agentSysUser = new AgentSysUser();
        agentSysUser.setAgentId(agentId);
        agentSysUser.setSysUserId(sysUser.getUserId());
        agentSysUser.setFlag(Constant.MASTER_SYS_USER_FLAG);
        agentSysUserMapper.insertSelective(agentSysUser);

        //7.保存代理商套餐记录
        agentPackageMapper.insertByAgentId(agentId);
    }

    /**
     * 查看代理商详情
     *
     * @param agentId
     * @return
     */
    @Override
    public AgentInfoDetailData detail(Long agentId) {
        AgentInfoDetailData result = new AgentInfoDetailData();
        //1.agent信息
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        if (agent == null) {
            throw new RRException("代理商信息不存在");
        }
        BeanUtils.copyProperties(agent, result);
        result.setAgentId(agent.getId());
        //2.联系人
        AgentContact agentContact = agentContactMapper.queryOneByAgentId(agentId);
        if (agentContact != null) {
            result.setContactName(agentContact.getName());
            result.setMobile(agentContact.getMobile());
            result.setEmail(agentContact.getEmail());
            result.setPosition(agentContact.getPosition());
        }
        //3.代理商等级
        AgentLevel agentLevel = agentLevelMapper.queryOneByAgentId(agentId);
        if (agentLevel != null) {
            result.setLevelId(agentLevel.getLevelId());
            result.setPrice(agentLevel.getPrice());
        }
        //4.代理商账户信息
        AgentAccount agentAccount = agentAccountMapper.queryOneByAgentId(agentId);
        if (agentAccount != null) {
            result.setEmptyWarnNumber(agentAccount.getEmptyWarnNumber());
        }
        return result;
    }

    /**
     * 修改代理商信息
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void updateAgent(AgentInfoUpdateParam param) {
        //验证单价
        if (param.getPrice().compareTo(BigDecimal.ZERO) < 1) {
            throw new RRException("单价必须大于0");
        }
        //验证预警条数
        if (param.getEmptyWarnNumber().compareTo(0L) < 0) {
            throw new RRException("预警条数必须大于0");
        }
        //验证代理商等级是否存在
        AgentBasicLevel agentBasicLevelVerify = agentBasicLevelMapper.selectByPrimaryKey(param.getLevelId());
        Assert.notNull(agentBasicLevelVerify, "代理商等级不存在");

        //验证代理商名称是否重复
        Agent agentVerifyCompanyName = agentMapper.queryOneByCompanyName(param.getCompanyName());
        if (agentVerifyCompanyName != null && !agentVerifyCompanyName.getId().equals(param.getAgentId())) {
            throw new RRException("代理商已存在");
        }

        //验证并代理商手机号码
        List<AgentContact> agentContactVerifyMobile = agentContactMapper.queryListByMobile(param.getMobile());
        if (!CollectionUtils.isEmpty(agentContactVerifyMobile)) {
            for (AgentContact agentContact : agentContactVerifyMobile) {
                //如果已存在该手机号的别的代理商，则不允许
                if (!agentContact.getAgentId().equals(param.getAgentId())) {
                    throw new RRException("此手机号代理商已经存在");
                }
            }
        }
        agentContactMapper.updateMobileByAgentId(param.getAgentId(), param.getMobile());
        //验证并更新代管理员账号
        //根据代理商id 获取管理员主账号
        AgentSysUser agentSysUser = agentSysUserMapper.queryMasterSysUserByAgentId(param.getAgentId());
        Assert.notNull(agentSysUser, "该代理商主账号不存在，请联系管理员");
        //更新代理商用户名和手机号 userName 为唯一索引
        sysUserService.updateUserNameAndMobileByUserId(agentSysUser.getSysUserId(), param.getMobile(), param.getMobile());
        //更新登录密码
        if (StringUtils.isNotBlank(param.getPassword())) {
            SysUserEntity sysUserEntity = sysUserService.selectById(agentSysUser.getSysUserId());
            String password = new Sha256Hash(param.getPassword(), sysUserEntity.getSalt()).toHex();
            sysUserService.updatePasswordByUserId(agentSysUser.getSysUserId(), password);
        }
        //验证并更新营业执照图片信息
        String licenseUrl = "";
        if (StringUtils.isNotBlank(param.getLicensePicNo())) {
            //验证营业执照图片信息是否存在
            AgentPicture agentPictureVerify = agentPictureMapper.queryOneByPicNo(param.getLicensePicNo());
            Assert.notNull(agentPictureVerify, "营业执照图片未照到，请重新上传");
            licenseUrl = agentPictureVerify.getPicUrl();
        }
        //验证商户编号是否重复
        Agent agentVerifyMchId = agentMapper.queryOneByMchId(param.getMchId());
        if (agentVerifyMchId != null && !agentVerifyMchId.getId().equals(param.getAgentId())) {
            throw new RRException("商户编号已存在");
        }
        //验证营业执照编号是否重复
        Agent agentVerifyLicenseNo = agentMapper.queryOneByLicenseNo(param.getLicenseNo());
        if (agentVerifyLicenseNo != null && !agentVerifyLicenseNo.getId().equals(param.getAgentId())) {
            throw new RRException("营业执照编号已存在");
        }

        //1.保存agent
        Agent agent = new Agent();
        BeanUtils.copyProperties(param, agent);
        agent.setId(param.getAgentId());
        if (StringUtils.isNotBlank(licenseUrl)) {
            agent.setLicenseUrl(licenseUrl);
        }
        agentMapper.updateByPrimaryKeySelective(agent);
        //2.保存联系人信息
        AgentContact agentContact = agentContactMapper.queryOneByAgentId(param.getAgentId());
        BeanUtils.copyProperties(param, agentContact);
        agentContact.setName(param.getContactName());
        agentContactMapper.updateByPrimaryKeySelective(agentContact);
        //3.保存账号信息
        sysUserService.updateRealNameAndEmailByUserId(agentSysUser.getSysUserId(), param.getContactName(), param.getEmail());
        //4.保存代理商级别
        AgentLevel agentLevel = agentLevelMapper.queryOneByAgentId(param.getAgentId());
        Assert.notNull(agentLevel, "代理商级别记录不存在，请联系管理员");
        agentLevel.setLevelId(param.getLevelId());
        agentLevel.setLevelName(agentBasicLevelVerify.getName());
        agentLevel.setPrice(param.getPrice());
        agentLevelMapper.updateByPrimaryKeySelective(agentLevel);
        //5.保存代理商账户信息
        AgentAccount agentAccount = agentAccountMapper.queryOneByAgentId(param.getAgentId());
        Assert.notNull(agentAccount, "代理商级别记录不存在，请联系管理员");
        agentAccount.setEmptyWarnNumber(Constant.DEFAULT_AGENT_EMPTY_WARN_NUMBER);
        agentAccount.setEmptyWarnNumber(param.getEmptyWarnNumber());
        agentAccountMapper.updateByPrimaryKeySelective(agentAccount);
    }

    /**
     * 禁用代理商
     */
    @Override
    public void pauseAgent(Long agentId) {
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        if (!agent.getStatus().equals(Constant.COMMON_NORMAL_STATUS_VALUE)) {
            throw new RRException("该代理商目前已是禁用状态");
        }
        agentMapper.updateStatusById(agentId, Constant.COMMON_DISABLED_STATUS_VALUE);
        //登出并禁用该代理商的所有账号
        List<AgentSysUser> agentSysUserList = agentSysUserMapper.queryListByAgentId(agentId);
        List<Long> sysUserIdList = agentSysUserList.stream().map(AgentSysUser::getSysUserId).collect(Collectors.toList());
        for (Long sysUserId : sysUserIdList) {
            //禁用账号
            sysUserService.disableByUserId(sysUserId);
            //登出
            sysUserTokenService.logout(sysUserId);
        }
    }

    /**
     * 启用代理商
     */
    @Override
    public void resumeAgent(Long agentId) {
        Agent agent = agentMapper.selectByPrimaryKey(agentId);
        Assert.notNull(agent, "记录不存在");
        if (!agent.getStatus().equals(Constant.COMMON_DISABLED_STATUS_VALUE)) {
            throw new RRException("该代理商目前已是启用状态");
        }
        agentMapper.updateStatusById(agentId, Constant.COMMON_NORMAL_STATUS_VALUE);
        //启用该代理商的所有账号
        List<AgentSysUser> agentSysUserList = agentSysUserMapper.queryListByAgentId(agentId);
        List<Long> sysUserIdList = agentSysUserList.stream().map(AgentSysUser::getSysUserId).collect(Collectors.toList());
        for (Long sysUserId : sysUserIdList) {
            //启用账号
            sysUserService.enableByUserId(sysUserId);
        }
    }

    /**
     * 代理商充值
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void recharge(AgentRechargeParam param) {

        String flag = sysConfigService.getValue(AgentConstant.AdminRechargeForAgent);
        if (!"true".equals(flag)) {
            throw new RRException("暂时关闭充值功能，请联系管理员");
        }

        //验证条数
        if (param.getNumber().compareTo(0L) < 1) {
            throw new RRException("充值条数必须大于0");
        }
        //验证入账方式是否正确
        AgentRechargePayTypeEnum agentRechargePayTypeEnum = AgentRechargePayTypeEnum.getEnumByCode(param.getPayType());
        Assert.notNull(agentRechargePayTypeEnum, "入账方式不存在");
        //1.增加充值记录
        AgentOrder agentOrder = new AgentOrder();
        BeanUtils.copyProperties(param, agentOrder);
        agentOrder.setOrderNo(UUID.randomUUID().toString().replaceAll("-", ""));
        agentOrder.setType(Constant.AGENT_ORDER_TYPE_RECHARGE);
        agentOrder.setStatus(Constant.AGENT_ORDER_STATUS_SUCCESS);
        agentOrder.setRoleType(Constant.AGENT_ORDER_ROLE_TYPE_ADMIN);
        agentOrderMapper.insertSelective(agentOrder);
        //2.增加代理商账户余额
        agentAccountMapper.addEmptyBalanceByAgentId(param.getAgentId(), param.getNumber());
    }


}
