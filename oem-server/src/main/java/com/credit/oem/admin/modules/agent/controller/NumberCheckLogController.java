package com.credit.oem.admin.modules.agent.controller;

import com.credit.oem.admin.common.annotation.SysLog;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.service.NumberCheckLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzj
 * @since 2018/8/15
 */
@RestController
@RequestMapping("/data/agent/numberCheckLog")
@Validated
@Api("空号检测消费记录操作")
public class NumberCheckLogController {
    @Autowired
    NumberCheckLogService numberCheckLogService;

    //@SysLog("更新空号检测消费记录")
    @PostMapping("/updateInfo")
    @ApiOperation("更新空号检测消费记录")
    public R list() {
        numberCheckLogService.updateInfo();
        return R.ok();
    }

}
