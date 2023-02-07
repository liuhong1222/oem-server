package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.modules.agent.entity.AgentPicture;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenzj
 * @since 2018/8/10
 */
public interface UploadPictureService {

    /**
     * 上传图片文件
     */
    AgentPicture uploadAgentPictureService(Long sysUserId, MultipartFile file, Integer type,String sealImageUploadPath);

}
