package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.modules.agent.model.data.AgentBasicLevelData;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelSaveParam;
import com.credit.oem.admin.modules.agent.model.param.AgentBasicLevelUpdateParam;

import java.util.List;

/**
 * @author chenzj
 * @since 2018/8/11
 */
public interface AgentBasicLevelService {

    List<AgentBasicLevelData> list();

    void saveBasicLevel(AgentBasicLevelSaveParam param);

    void updateBasicLevel(AgentBasicLevelUpdateParam param);

    void deleteBasicLevel(Long id);

    AgentBasicLevelData detail(Long id);
}
