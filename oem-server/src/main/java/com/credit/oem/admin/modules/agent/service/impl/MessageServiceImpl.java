package com.credit.oem.admin.modules.agent.service.impl;


import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.dao.AgentMessageAuditRecordMapper;
import com.credit.oem.admin.modules.agent.dao.AgentMessageMapper;
import com.credit.oem.admin.modules.agent.dao.CreUserMapper;
import com.credit.oem.admin.modules.agent.dao.MessageMapper;
import com.credit.oem.admin.modules.agent.entity.AgentMessage;
import com.credit.oem.admin.modules.agent.entity.AgentMessageAuditRecord;
import com.credit.oem.admin.modules.agent.entity.Message;
import com.credit.oem.admin.modules.agent.enums.MessageAuditStateEnum;
import com.credit.oem.admin.modules.agent.enums.MessageTypeEnum;
import com.credit.oem.admin.modules.agent.model.data.MessageInfoData;
import com.credit.oem.admin.modules.agent.model.data.MessageInfoDetailData;
import com.credit.oem.admin.modules.agent.model.param.MessageAuditParam;
import com.credit.oem.admin.modules.agent.model.param.MessageInfoParam;
import com.credit.oem.admin.modules.agent.model.param.MessageSaveParam;
import com.credit.oem.admin.modules.agent.service.MessageService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author chenzj
 * @since 2018/5/23
 */

@Service
public class MessageServiceImpl implements MessageService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CreUserMapper creUserMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private AgentMessageMapper agentMessageMapper;

    @Autowired
    private AgentMessageAuditRecordMapper agentMessageAuditRecordMapper;

    @Autowired
    private SysUserService sysUserService;

    private final String CreateByStart = "[OEM]";

    private final static String CREATE_BY_OEM = "[OEM]sysUserId:%s,agentId:%s";
    private final static String NON_AGENT_ROLE = "该用户不是代理商";

    private Message initMessage(MessageSaveParam param, String userId, Long sysUserId, Long agentId) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString().replace("-", ""));
        message.setCustomerId(StringUtils.isBlank(userId) ? null : userId);
        message.setTitle(param.getTitle());
        message.setMessage(param.getMessage());
        message.setFlag(0);
        message.setCreateby(CreateByStart + "sysUserId:" + sysUserId + ",agentId:" + agentId);
        message.setCreatetime(new Date());
        message.setCreatedate(new Date());
        message.setType(param.getType());
        message.setIsread("0");
        message.setAgentId(agentId);
        message.setAgentMessageId(param.getAgentMessageId());
        return message;
    }

    @Override
    public R findUserIdMobileByMobile(String userPhone) {

        Long sysUserId = sysUserService.getSysUserId();
        Long agentId = sysUserService.selectAgentIdBySysUserId(sysUserId);
        if (StringUtils.isNotBlank(userPhone)) {
            userPhone = "%" + userPhone + "%";
        } else {
            userPhone = "%%";
        }
        List<Map> mapList = creUserMapper.findUserIdMobileByMobileAndAgentId(userPhone, agentId);
        return R.ok(mapList);
    }

    @Override
    public R test(String userPhone) {
        return R.ok(new PageInfo<>(null));
    }

    /**
     * 代理商消息列表
     */
    private PageInfo messageList(MessageInfoParam param) {
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        List<MessageInfoData> list = agentMessageMapper.queryAgentMessageList(param);
        PageInfo<MessageInfoData> pageInfo = new PageInfo<>(list);
        pageInfo.getList().forEach(e -> {
            e.setAuditStateName(MessageAuditStateEnum.getDescriByCode(e.getAuditState()));
            e.setTypeName(MessageTypeEnum.getDescriByCode(e.getType()));
        });
        return pageInfo;
    }


    /**
     * 查询代理商消息列表
     */
    @Override
    public PageInfo messageAllList(MessageInfoParam param) {
        return messageList(param);
    }

    /**
     * 审核代理商消息
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void messageAllAudit(MessageAuditParam param, Long sysUserId) {
        //判断审核状态
        if (!param.getAuditState().equals(MessageAuditStateEnum.AUDITED.getCode()) &&
                !param.getAuditState().equals(MessageAuditStateEnum.REJECTED.getCode())) {
            throw new RRException("非法操作");
        }
        //1.更新代理商消息状态
        int auditAgentMessageCount = agentMessageMapper.auditAgentMessage(param.getAgentMessageId(), sysUserId, param.getAuditState(), param.getAuditRemark());
        if (auditAgentMessageCount == 0) {
            throw new RRException("代理商消息不存在或不可审核");
        }
        //2.更新消息状态
        if (param.getAuditState().equals(MessageAuditStateEnum.AUDITED.getCode())) {
            int approvedMessageCount = messageMapper.approvedMessage(param.getAgentMessageId());
            if (approvedMessageCount == 0) {
                throw new RRException("消息不存在或不可审核");
            }
        }

        AgentMessage agentMessage = agentMessageMapper.selectByPrimaryKey(param.getAgentMessageId());
        //3.插入操作记录
        AgentMessageAuditRecord agentMessageAuditRecord = new AgentMessageAuditRecord();
        agentMessageAuditRecord.setAgentMessageId(param.getAgentMessageId());
        agentMessageAuditRecord.setAuditState(param.getAuditState());
        agentMessageAuditRecord.setRemark(param.getAuditRemark());
        agentMessageAuditRecord.setAgentId(agentMessage.getAgentId());
        agentMessageAuditRecord.setSysUserId(sysUserId);
        agentMessageAuditRecordMapper.insertSelective(agentMessageAuditRecord);
    }

    /**
     * 查看新闻详情
     */
    private MessageInfoDetailData messageDetail(Long agentMessageId, Long agentId) {
        MessageInfoDetailData messageInfoDetailData = new MessageInfoDetailData();
        AgentMessage agentMessage = agentMessageMapper.selectByPrimaryKey(agentMessageId);
        if (agentMessage != null) {
            //判断是否为代理商id是否相同
            if (agentId != null && !agentId.equals(agentMessage.getAgentId())) {
                throw new RRException("无权查看此消息");
            }
            BeanUtils.copyProperties(agentMessage, messageInfoDetailData);
            messageInfoDetailData.setTypeName(MessageTypeEnum.getDescriByCode(messageInfoDetailData.getType()));
            //添加手机号列表
            List<String> mobileList = messageMapper.selectMobileListByAgentMessageId(agentMessageId);
            messageInfoDetailData.setMobileList(mobileList);
        }
        return messageInfoDetailData;
    }

    /**
     * 查看代理商消息详情
     */
    @Override
    public MessageInfoDetailData messageAllDetail(Long agentMessageId) {
        return messageDetail(agentMessageId, null);
    }

    /**
     * 删除代理商消息
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void messageAllDelete(Long agentMessageId, Long sysUserId) {
        //1.删除代理商消息
        int deleteCount = agentMessageMapper.deleteAgentMessage(agentMessageId, null);
        if (deleteCount == 0) {
            throw new RRException("消息不存在或不可删除");
        }
        //2.删除消息
        messageMapper.deleteMessage(null, agentMessageId);
        //3.插入操作记录
        AgentMessage agentMessage = agentMessageMapper.selectByPrimaryKey(agentMessageId);
        AgentMessageAuditRecord agentMessageAuditRecord = new AgentMessageAuditRecord();
        agentMessageAuditRecord.setAgentMessageId(agentMessageId);
        agentMessageAuditRecord.setAuditState(MessageAuditStateEnum.DELETED.getCode());
        agentMessageAuditRecord.setAgentId(agentMessage.getAgentId());
        agentMessageAuditRecord.setSysUserId(sysUserId);
        agentMessageAuditRecordMapper.insertSelective(agentMessageAuditRecord);
    }

    /**
     * 查询我的消息列表
     */
    @Override
    public PageInfo messageMyList(MessageInfoParam param, Long agentId) {
        Assert.notNull(agentId, NON_AGENT_ROLE);
        //查找并验证代理商主账号信息
        param.setAgentId(agentId);
        param.setAgentMobile(null);
        param.setAgentName(null);
        return messageList(param);
    }

    /**
     * 验证消息类型
     */
    private void verifyMessageType(String messageType) {
        if (MessageTypeEnum.getEnumByCode(messageType) == null) {
            throw new RRException("消息类型不正确");
        }
    }

    /**
     * 发布我的消息
     */
    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void messageMySave(MessageSaveParam param, Long sysUserId, Long agentId) {
        verifyMessageType(param.getType());
        //userIdList:"[1,2,3]" 字符串转成数组
        String userIdListStr=param.getUserIdList();
        if(StringUtils.isNotBlank(userIdListStr)){
            userIdListStr= userIdListStr.replace("\"","")
                    .replace("[","")
                    .replace("]","");
        }
        if (StringUtils.isBlank(userIdListStr) && !MessageSaveParam.SELECT_TYPE_ALL.equals(param.getSelectType())) {
            throw new RRException("用户列表不能为空");
        }
        List<String> userIdList = new ArrayList<>();
        for (String userId : userIdListStr.split(",")){
            userIdList.add(userId.trim());
        }

        Assert.notNull(agentId, NON_AGENT_ROLE);
        //60秒内只能发送一条新闻
        long recordCount = agentMessageMapper.countByCreatorWithinSeconds(String.format(CREATE_BY_OEM, sysUserId, agentId), 60L);
        if (recordCount > 0) {
            throw new RRException("60s内只能发送一条消息");
        }
        //1.插入AgentMessage
        AgentMessage agentMessage = new AgentMessage();
        agentMessage.setTitle(param.getTitle());
        agentMessage.setMessage(param.getMessage());
        agentMessage.setType(param.getType());
        agentMessage.setAuditState(MessageAuditStateEnum.TO_AUDIT.getCode());
        agentMessage.setCreator(String.format(CREATE_BY_OEM, sysUserId, agentId));
        agentMessage.setAgentId(agentId);
        agentMessage.setUpdater(String.format(CREATE_BY_OEM, sysUserId, agentId));
        int insertCount = agentMessageMapper.insertSelective(agentMessage);
        if (insertCount == 0) {
            throw new RRException("插入失败，请重试");
        }
        param.setAgentMessageId(agentMessage.getId());
        //2.根据手机号列表，查询用户，批量插入 Message
        if (MessageSaveParam.SELECT_TYPE_ALL.equals(param.getSelectType())) {
            int count = creUserMapper.selectUserCountByAgentId(agentId);
            if (count==0){
                throw new RRException("你还没有用户，不能发布消息");
            }
            int pageSize = 500;
            int pages = count / pageSize + 1;
            for (int i = 0; i < pages; i++) {
                Map map = Maps.newHashMap();
                map.put("start", i * pages);
                map.put("pagesize", pageSize);
                map.put("agentId", agentId);
                List<Integer> list = creUserMapper.selectUserIdListByAgentId(map);
                List<Message> toInsertMessageList = new ArrayList<>(pageSize + 1);
                for (Integer userId : list) {
                    toInsertMessageList.add(initMessage(param, userId == null ? null : userId.toString(), sysUserId, agentId));
                }
                messageMapper.insertBatch(toInsertMessageList);
            }
        } else {
            List<Message> toInsertMessageList = new ArrayList<>();
            for (String userId : userIdList) {
                toInsertMessageList.add(initMessage(param, userId, sysUserId, agentId));
            }
            messageMapper.insertBatch(toInsertMessageList);
        }
    }

    /**
     * 修改我的消息
     */
    @Override
    public void messageMyUpdate(MessageSaveParam param, Long sysUserId, Long agentId) {
        Long agentMessageId = param.getAgentMessageId();
        Assert.notNull(agentMessageId, "参数校验失败，消息id不能为空");
        verifyMessageType(param.getType());
        Assert.notNull(agentId, NON_AGENT_ROLE);
        //只能修改 发布待审核状态 和 驳回状态的 消息
        //1.修改agent_message
        AgentMessage agentMessage = agentMessageMapper.selectByPrimaryKey(agentMessageId);
        Assert.notNull(agentMessage, "消息不存在");
        if (!agentId.equals(agentMessage.getAgentId())) {
            throw new RRException("参数校验失败，消息id非法");
        }
        String updater = String.format(CREATE_BY_OEM, sysUserId, agentId);
        //审核状态赋值
        Integer auditState;
        if (MessageAuditStateEnum.TO_AUDIT.getCode().equals(agentMessage.getAuditState())) {
            auditState = MessageAuditStateEnum.TO_AUDIT.getCode();
        } else if (MessageAuditStateEnum.MODIFIED.getCode().equals(agentMessage.getAuditState()) || MessageAuditStateEnum.REJECTED.getCode().equals(agentMessage.getAuditState())) {
            auditState = MessageAuditStateEnum.MODIFIED.getCode();
        } else {
            throw new RRException("消息为不可编辑状态");
        }
        int modifyCount = agentMessageMapper.modifyAgentMessage(param.getTitle(), param.getMessage(),
                param.getAgentMessageId(), auditState,
                param.getType(), updater);
        if (modifyCount == 0) {
            throw new RRException("修改失败，不可编辑状态");
        }
        //2.修改message
        messageMapper.modifyByAgentMessageId(param.getTitle(), param.getMessage(), agentId, param.getAgentMessageId(), param.getType(), updater);
        //3.插入agent_message_audit_record
        AgentMessageAuditRecord agentMessageAuditRecord = new AgentMessageAuditRecord();
        agentMessageAuditRecord.setAgentMessageId(param.getAgentMessageId());
        agentMessageAuditRecord.setAuditState(auditState);
        agentMessageAuditRecord.setAgentId(agentId);
        agentMessageAuditRecord.setSysUserId(sysUserId);
        agentMessageAuditRecordMapper.insertSelective(agentMessageAuditRecord);
    }

    /**
     * 查看我的消息详情
     */
    @Override
    public MessageInfoDetailData messageMyDetail(Long agentMessageId, Long agentId) {
        Assert.notNull(agentId, NON_AGENT_ROLE);
        return messageDetail(agentMessageId, agentId);
    }

    /**
     * 删除我的消息
     */
    @Override
    public void messageMyDelete(Long agentMessageId, Long sysUserId, Long agentId) {
        Assert.notNull(agentId, NON_AGENT_ROLE);
        //1.删除代理商消息
        int deleteCount = agentMessageMapper.deleteAgentMessage(agentMessageId, agentId);
        if (deleteCount == 0) {
            throw new RRException("消息不存在或不可删除");
        }
        //2.删除消息
        messageMapper.deleteMessage(null, agentMessageId);
        //3.插入操作记录
        AgentMessage agentMessage = agentMessageMapper.selectByPrimaryKey(agentMessageId);
        AgentMessageAuditRecord agentMessageAuditRecord = new AgentMessageAuditRecord();
        agentMessageAuditRecord.setAgentMessageId(agentMessageId);
        agentMessageAuditRecord.setAuditState(MessageAuditStateEnum.DELETED.getCode());
        agentMessageAuditRecord.setAgentId(agentMessage.getAgentId());
        agentMessageAuditRecord.setSysUserId(sysUserId);
        agentMessageAuditRecordMapper.insertSelective(agentMessageAuditRecord);
    }


}
