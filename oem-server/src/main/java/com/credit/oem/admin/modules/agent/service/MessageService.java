package com.credit.oem.admin.modules.agent.service;


import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.data.MessageInfoDetailData;
import com.credit.oem.admin.modules.agent.model.param.MessageAuditParam;
import com.credit.oem.admin.modules.agent.model.param.MessageInfoParam;
import com.credit.oem.admin.modules.agent.model.param.MessageSaveParam;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * author zhangx
 * date  2018/8/2 11:45
 */
@Service
public interface MessageService {
//    void addMessage(MessageSaveParam param);

    R findUserIdMobileByMobile(String userPhone);

    R test(String userPhone);


    /**
     * 查询代理商消息列表
     */
    PageInfo messageAllList(MessageInfoParam param);

    /**
     * 审核代理商消息
     */
    void messageAllAudit(MessageAuditParam param, Long sysUserId);

    /**
     * 查看代理商消息详情
     */
    MessageInfoDetailData messageAllDetail(Long agentMessageId);

    /**
     * 删除代理商消息
     */
    void messageAllDelete(Long agentMessageId, Long sysUserId);

    /**
     * 查询我的消息列表
     */
    PageInfo messageMyList(MessageInfoParam param, Long agentId);

    /**
     * 发布我的消息
     */
    void messageMySave(MessageSaveParam param, Long sysUserId, Long agentId);

    /**
     * 修改我的消息
     */
    void messageMyUpdate(MessageSaveParam param, Long sysUserId, Long agentId);

    /**
     * 查看我的消息详情
     */
    MessageInfoDetailData messageMyDetail(Long agentMessageId, Long agentId);

    /**
     * 删除我的消息
     */
    void messageMyDelete(Long agentMessageId, Long sysUserId, Long agentId);

}
