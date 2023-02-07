package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.UserAgentChangeParam;

/**
 * @author chenzj
 * @since 2018/8/11
 */
public interface UserAgentChangeService {

    R changeAgent(Integer creUserId, String outAgentName, String inAgentName, String remark);


    R changeAgentList(UserAgentChangeParam param);

    R findCompanyName(String inAgentName);
}
