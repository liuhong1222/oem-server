package com.credit.oem.admin.modules.agent.dao;


import com.credit.oem.admin.modules.agent.entity.News;
import com.credit.oem.admin.modules.agent.model.data.NewsInfoData;
import com.credit.oem.admin.modules.agent.model.param.NewsInfoParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NewsMapper {

    News selectByPrimaryKey(String id);

    int insert(News record);

    int insertSelective(News record);

    News selectNewsByCreateAndTitle(@Param("title") String title, @Param("createby") String createby);

    /**
     * 查询新闻列表
     */
    List<NewsInfoData> queryNewsList(NewsInfoParam param);

    /**
     * 审核新闻
     */
    int auditNews(@Param("id") String id, @Param("auditState") Integer auditState, @Param("auditRemark") String auditRemark);

    /**
     * 修改新闻
     */
    int modifyNews(News news);

    /**
     * 删除新闻
     */
    int deleteNews(@Param("id") String id, @Param("agentId") Long agentId);

    /**
     * 数秒内最近的新闻
     */
    long countByCreateByWithinSeconds(@Param("createby") String createby, @Param("seconds") Long seconds);

}
