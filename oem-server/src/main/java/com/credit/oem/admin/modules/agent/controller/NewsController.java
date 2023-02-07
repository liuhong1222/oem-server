package com.credit.oem.admin.modules.agent.controller;


import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.model.param.NewsAuditParam;
import com.credit.oem.admin.modules.agent.model.param.NewsInfoParam;
import com.credit.oem.admin.modules.agent.model.param.NewsSaveParam;
import com.credit.oem.admin.modules.agent.service.NewsService;
import com.credit.oem.admin.modules.sys.controller.AbstractController;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * copyright (C), 2018-2018, 创蓝253
 *
 * @author zhangx
 * @fileName ManagerController
 * @date 2018/8/2 17:30
 */
@RestController
@RequestMapping("/open/agent/news")
public class NewsController extends AbstractController {

    @Autowired
    NewsService newsService;


    @SysLog("查询代理商新闻列表")
    @GetMapping("/all/list")
    @ApiOperation("查询代理商新闻列表")
    @RequiresPermissions("news:all:list")
    public R newsAllList(NewsInfoParam param) {
        return R.ok(newsService.newsAllList(param));
    }


    @SysLog("审核代理商新闻")
    @PostMapping("/all/audit")
    @ApiOperation("审核代理商新闻")
    @RepeatCommitToken
    @RequiresPermissions("news:all:audit")
    public R newsAllAudit(@Valid NewsAuditParam param) {
        newsService.newsAllAudit(param, getUserId());
        return R.ok();
    }


    @SysLog("查看代理商新闻详情")
    @GetMapping("/all/detail")
    @ApiOperation("查看代理商新闻详情")
    @RequiresPermissions("news:all:detail")
    public R newsAllDetail(@Valid String newsId) {
        return R.ok(newsService.newsAllDetail(newsId));
    }

    @SysLog(" 删除代理商新闻")
    @PostMapping("/all/delete")
    @ApiOperation("删除代理商新闻")
    @RepeatCommitToken
    @RequiresPermissions("news:all:delete")
    public R newsAllDelete(@Validated String newsId) {
        newsService.newsAllDelete(newsId, getUserId());
        return R.ok();
    }


    @SysLog("查询我的新闻列表")
    @GetMapping("/my/list")
    @ApiOperation("查询我的新闻列表")
    @RequiresPermissions("news:my:list")
    public R newsMyList(NewsInfoParam param) {
        return R.ok(newsService.newsMyList(param, getAgentId()));
    }


    @SysLog("发布我的新闻")
    @PostMapping({"/my/save"})
    @ApiOperation("发布我的新闻")
    @RepeatCommitToken
    @RequiresPermissions(value = {"news:my:save"})
    public R newsMySave(@Valid NewsSaveParam param) {
        newsService.newsMySave(param, getUserId(), getAgentId());
        return R.ok();
    }

    @SysLog("发布我的新闻")
    @PostMapping({"/addNews"})
    @ApiOperation("发布我的新闻")
    @RepeatCommitToken
    @RequiresPermissions(value = {"open:agent:news:addNews"})
    public R addNews(@Valid NewsSaveParam param) {
        newsService.newsMySave(param, getUserId(), getAgentId());
        return R.ok();
    }


    @SysLog("修改我的新闻")
    @PostMapping("/my/update")
    @ApiOperation("修改我的新闻")
    @RequiresPermissions("news:my:update")
    public R newsMyUpdate(@Valid NewsSaveParam param) {
        newsService.newsMyUpdate(param, getUserId(), getAgentId());
        return R.ok();
    }


    @SysLog("查看我的新闻详情")
    @GetMapping("/my/detail")
    @ApiOperation("查看我的新闻详情")
    @RequiresPermissions("news:my:detail")
    public R newsMyDetail(@Valid String newsId) {
        return R.ok(newsService.newsMyDetail(newsId, getAgentId()));
    }


    @SysLog("删除我的新闻")
    @PostMapping("/my/delete")
    @ApiOperation("删除我的新闻")
    @RepeatCommitToken
    @RequiresPermissions("news:my:delete")
    public R newsMyDelete(@Validated String newsId) {
        newsService.newsMyDelete(newsId, getUserId(), getAgentId());
        return R.ok();
    }


}
