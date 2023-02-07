package com.credit.oem.admin.modules.agent.dao;

import com.credit.oem.admin.modules.agent.BaseCreditMapper;
import com.credit.oem.admin.modules.agent.entity.AgentPicture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgentPictureMapper extends BaseCreditMapper<AgentPicture, Long> {
    /**
     * 根据图片编号获取图片信息
     */
    AgentPicture queryOneByPicNo(String picNo);

}
