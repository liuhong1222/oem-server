package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.service.AgentTrdOrderIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/15
 */
@Service("agentTrdOrderIdServiceImpl")
public class AgentTrdOrderIdServiceImpl implements AgentTrdOrderIdService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AgentTrdOrderIdMapper agentTrdOrderIdMapper;

    @Autowired
    TrdOrderMapper trdOrderMapper;

    @Autowired
    CreUserMapper creUserMapper;

    @Autowired
    IdCardInfoMapper idCardInfoMapper;

    @Autowired
    BusinessLicenceInfoMapper businessLicenceInfoMapper;

    @Autowired
    ProductPackageMapper productPackageMapper;

    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentCreUserMapper agentCreUserMapper;

    /**
     * 更新代理商用户充值记录
     */
    @Override
    public void updateInfo() {
        //1.查询未更新的记录
        logger.info("开始更新AgentTrdOrderId");
        List<AgentTrdOrderId> agentTrdOrderIdList = agentTrdOrderIdMapper.queryListByNotUpdated();
        if (CollectionUtils.isEmpty(agentTrdOrderIdList)) {
            logger.info("无需更新AgentTrdOrderId");
            return;
        }
        for (AgentTrdOrderId originAgentTrdOrderId : agentTrdOrderIdList) {
            updateAgentTrdOrderIdInfo(originAgentTrdOrderId);
        }
        logger.info("完成更新AgentTrdOrderId");
    }

    @Override
    public void updateAgentTrdOrderIdInfo(AgentTrdOrderId originAgentTrdOrderId){
        AgentTrdOrderId updateAgentTrdOrderId = new AgentTrdOrderId();
        updateAgentTrdOrderId.setId(originAgentTrdOrderId.getId());
        try {
            //查询充值订单信息 获取 cre_user_id
            TrdOrder trdOrder = trdOrderMapper.queryOneWithValidPayTimeById(originAgentTrdOrderId.getTrdOrderId().intValue());
            if (trdOrder != null) {
                Long creUserId = trdOrder.getCreUserId().longValue();
                updateAgentTrdOrderId.setCreUserId(creUserId);
                Long trdOrderPackageId = trdOrder.getProductsId().longValue();
                BigDecimal trdOrderPrice = BigDecimal.ZERO;
                if (trdOrder.getMoney() != null && trdOrder.getNumber() != null && !trdOrder.getNumber().equals(0)) {
                    trdOrderPrice = trdOrder.getMoney().divide(new BigDecimal(trdOrder.getNumber()),5, BigDecimal.ROUND_HALF_UP);
                }
                updateAgentTrdOrderId.setTrdOrderPrice(trdOrderPrice);
                //查询cre_user 获取 CreUserPhone cre_user_type
                CreUser creUser = creUserMapper.selectByPrimaryKey(creUserId.intValue());
                if (creUser != null) {
                    updateAgentTrdOrderId.setCreUserPhone(creUser.getUserPhone());
                    Integer creUserType = creUser.getUserType();
                    String creUserName = "";
                    if (creUserType != null) {
                        if (creUserType.equals(0)) {
                            //user_type=0 个人信息查询 idcardinfo
                            IdCardInfo idCardInfo = idCardInfoMapper.queryOneByCreUserId(creUserId);
                            if (idCardInfo != null) {
                                creUserName = idCardInfo.getUsername();
                            }
                        } else if (creUserType.equals(1)) {
                            //user_type=1 企业信息 businesslicenceinfo
                            BusinessLicenceInfo businessLicenceInfo = businessLicenceInfoMapper.queryOneByCreUserId(creUserId);
                            if (businessLicenceInfo != null) {
                                creUserName = businessLicenceInfo.getName();
                            }
                        }
                    }
                    updateAgentTrdOrderId.setCreUserName(creUserName);
                }
                //查询t_product_package
                ProductPackage productPackage = productPackageMapper.selectByPrimaryKey(trdOrderPackageId);
                if (productPackage != null) {
                    updateAgentTrdOrderId.setTrdOrderProductId(productPackage.getProductId());
                    updateAgentTrdOrderId.setTrdOrderPackageName(productPackage.getPackageName());
                }
                //查询o_agent
                Agent agent = agentMapper.selectByPrimaryKey(originAgentTrdOrderId.getAgentId());
                if (agent != null) {
                    updateAgentTrdOrderId.setAgentCompanyName(agent.getCompanyName());
                    updateAgentTrdOrderId.setAgentMobile(agent.getMobile());
                }
                //更新数据
                updateAgentTrdOrderId.setUpdated(1);
                agentTrdOrderIdMapper.updateByPrimaryKeySelective(updateAgentTrdOrderId);
            }
        } catch (Exception e) {
            logger.error("更新AgentTrdOrderId出错了,id:{}", originAgentTrdOrderId.getId(), e);
        }
    }

    /**
     * 注意客户名称变化
     * */
    @Override
    public void insertFromTrdorder() {
        List<TrdOrder> trdOrdersList=trdOrderMapper.selectSyncTrdOrderList();
        for(TrdOrder trdOrder:trdOrdersList){
            if(trdOrder.getCreUserId()!=null){
                List<AgentCreUser> agentCreUserList=agentCreUserMapper.selectByCreUserId(trdOrder.getCreUserId());
                if(agentCreUserList.size()!=1){
                    logger.error("trdOrder数据异常，找不到creUserId关联agent表 "+ trdOrder.getId()+",creUserId:"+trdOrder.getCreUserId());
                    continue;
                }
                AgentCreUser agentCreUser=agentCreUserList.get(0);
                AgentTrdOrderId agentTrdOrderId = new AgentTrdOrderId();
                agentTrdOrderId.setAgentId(agentCreUser.getAgentId());
                agentTrdOrderId.setAgentOrderId(null);//未知
                agentTrdOrderId.setTrdOrderId(Long.valueOf(trdOrder.getId()));
                agentTrdOrderId.setRemark(null);
                agentTrdOrderIdMapper.insertSelective(agentTrdOrderId);
                this.updateAgentTrdOrderIdInfo(agentTrdOrderId);
            }
        }
    }



}
