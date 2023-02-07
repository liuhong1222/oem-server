package com.credit.oem.admin.modules.agent.service;

import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.entity.AgentPicture;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chenzj
 * @since 2018/8/8
 */
public interface UploadImageService {


    AgentPicture uploadLicense(Long sysUserId, MultipartFile file, Integer imageType);

    R downLoadImage(String path, HttpServletResponse response);

}
