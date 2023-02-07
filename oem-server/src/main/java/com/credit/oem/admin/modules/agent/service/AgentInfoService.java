package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.modules.agent.entity.AgentPicture;
import com.credit.oem.admin.modules.agent.model.data.AgentInfoDetailData;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentInfoUpdateParam;
import com.credit.oem.admin.modules.agent.model.param.AgentRechargeParam;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author chenzj
 * @since 2018/8/8
 */
public interface AgentInfoService {

    PageInfo list(AgentInfoParam param);

    Map<String, String> uploadLicense(Long sysUserId , MultipartFile file) throws Exception;

    void saveAgent(AgentInfoSaveParam param,Long sysUserId);

    AgentInfoDetailData detail(Long agentId);

    void updateAgent(AgentInfoUpdateParam param);

    void pauseAgent(Long agentId);

    void resumeAgent(Long agentId);

    void recharge(AgentRechargeParam param);

}
