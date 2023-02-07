package com.credit.oem.admin.modules.agent.service;


import com.credit.oem.admin.modules.agent.model.data.NewsInfoDetailData;
import com.credit.oem.admin.modules.agent.model.param.NewsAuditParam;
import com.credit.oem.admin.modules.agent.model.param.NewsInfoParam;
import com.credit.oem.admin.modules.agent.model.param.NewsSaveParam;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * author zhangx
 * date  2018/8/2 11:45
 */
@Service
public interface NewsService {

//    R addNews(NewsSendParam param);

    /**
     * 查询代理商新闻列表
     */
    PageInfo newsAllList(NewsInfoParam param);

    /**
     * 审核代理商新闻
     */
    void newsAllAudit(NewsAuditParam param,Long sysUserId);

    /**
     * 查看代理商新闻详情
     */
    NewsInfoDetailData newsAllDetail(String newsId);

    /**
     * 删除代理商新闻
     */
    void newsAllDelete(String newsId,Long sysUserId);

    /**
     * 查询我的新闻列表
     */
    PageInfo newsMyList(NewsInfoParam param, Long agentId);

    /**
     * 发布我的新闻
     */
    void newsMySave(NewsSaveParam param,Long sysUserId,Long agentId);

    /**
     * 修改我的新闻
     */
    void newsMyUpdate(NewsSaveParam param,Long sysUserId,Long agentId);

    /**
     * 查看我的新闻详情
     */
    NewsInfoDetailData newsMyDetail(String newsId,Long agentId);

    /**
     * 删除我的新闻详情
     */
    void newsMyDelete(String newsId,Long sysUserId,Long agentId);


}
