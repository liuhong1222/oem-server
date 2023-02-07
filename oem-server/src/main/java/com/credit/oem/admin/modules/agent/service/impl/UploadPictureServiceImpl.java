package com.credit.oem.admin.modules.agent.service.impl;

import com.credit.oem.admin.common.exception.RRException;
import com.credit.oem.admin.common.utils.UploadUtils;
import com.credit.oem.admin.modules.agent.dao.AgentPictureMapper;
import com.credit.oem.admin.modules.agent.entity.AgentPicture;
import com.credit.oem.admin.modules.agent.service.UploadPictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author chenzj
 * @since 2018/8/10
 */
@Service
public class UploadPictureServiceImpl implements UploadPictureService {

    private static final Logger logger = LoggerFactory.getLogger(UploadPictureServiceImpl.class);

    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Autowired
    UploadUtils uploadUtils;

    @Autowired
    AgentPictureMapper agentPictureMapper;

    @Override
    public AgentPicture uploadAgentPictureService(Long sysUserId, MultipartFile file, Integer type,String sealImageUploadPath) {
        try {
            logger.info("上传图片(sysUserId:{},type:{})", sysUserId, type);
            String fileUrl = uploadUtils.uploadPicture(file, fileUploadPath,sealImageUploadPath);
            String filePath = fileUploadPath +"/"+ fileUrl;
            AgentPicture agentPicture = new AgentPicture();
            agentPicture.setPicNo(UUID.randomUUID().toString().replaceAll("-", ""));
            agentPicture.setPicPath(filePath);
            agentPicture.setPicUrl(fileUrl);
            agentPicture.setType(type);
            agentPicture.setSysUserId(sysUserId);
            agentPictureMapper.insertSelective(agentPicture);
            return agentPicture;
        } catch (IOException ex){
            throw new RRException("保存图片出错");
        }
    }
}
