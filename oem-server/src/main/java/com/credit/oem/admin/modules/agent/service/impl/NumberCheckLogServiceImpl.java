package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.modules.agent.dao.*;
import com.credit.oem.admin.modules.agent.entity.*;
import com.credit.oem.admin.modules.agent.service.NumberCheckLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/15
 */
@Service("numberCheckLogServiceImpl")
public class NumberCheckLogServiceImpl implements NumberCheckLogService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NumberCheckLogMapper numberCheckLogMapper;

    @Autowired
    CreUserMapper creUserMapper;

    @Autowired
    IdCardInfoMapper idCardInfoMapper;

    @Autowired
    BusinessLicenceInfoMapper businessLicenceInfoMapper;

    @Autowired
    AgentMapper agentMapper;

    /**
     * 更新空号检测消费记录
     */
    @Override
    public void updateInfo() {
        //1.查询未更新的记录
        logger.info("开始更新NumberCheckLog");
        List<NumberCheckLog> numberCheckLogList = numberCheckLogMapper.queryListByNotUpdated();
        if (CollectionUtils.isEmpty(numberCheckLogList)) {
            logger.info("无需更新NumberCheckLog");
            return;
        }
        for (NumberCheckLog o : numberCheckLogList) {
            NumberCheckLog updateNumberCheckLog = new NumberCheckLog();
            updateNumberCheckLog.setId(o.getId());
            try {
                //查询cre_user 获取 CreUserPhone cre_user_type
                CreUser creUser = creUserMapper.selectByPrimaryKey(o.getUserid());
                if (creUser != null) {
                    updateNumberCheckLog.setCreUserPhone(creUser.getUserPhone());
                    Integer creUserType = creUser.getUserType();
                    String creUserName = "";
                    if (creUserType != null) {
                        if (creUserType.equals(0)) {
                            //user_type=0 个人信息查询 idcardinfo
                            IdCardInfo idCardInfo = idCardInfoMapper.queryOneByCreUserId(o.getUserid().longValue());
                            if (idCardInfo != null) {
                                creUserName = idCardInfo.getUsername();
                            }
                        } else if (creUserType.equals(1)) {
                            //user_type=1 企业信息 businesslicenceinfo
                            BusinessLicenceInfo businessLicenceInfo = businessLicenceInfoMapper.queryOneByCreUserId(o.getUserid().longValue());
                            if (businessLicenceInfo != null) {
                                creUserName = businessLicenceInfo.getName();
                            }
                        }
                    }
                    updateNumberCheckLog.setCreUserName(creUserName);
                }
                //查询o_agent
                Agent agent = agentMapper.selectByPrimaryKey(o.getAgentId());
                if (agent != null) {
                    updateNumberCheckLog.setAgentCompanyName(agent.getCompanyName());
                    updateNumberCheckLog.setAgentMobile(agent.getMobile());
                }
            } catch (Exception e) {
                logger.error("更新NumberCheckLog出错了,id:{}", o.getId(), e);
            }
            //更新数据
            updateNumberCheckLog.setUpdated(1);
            numberCheckLogMapper.updateByPrimaryKeySelective(updateNumberCheckLog);
        }
        logger.info("完成更新NumberCheckLog");
    }
}
