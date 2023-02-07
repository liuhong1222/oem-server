package com.credit.oem.admin.modules.agent.controller;


import com.credit.oem.admin.modules.ueditor.ActionEnter;
import com.credit.oem.admin.modules.ueditor.EditorConfig;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author chenzj
 * @since 2018/8/17
 */
@RestController
@RequestMapping("/open/agent/")
public class UeditorController {

    @Value("${ueditorConfigPath}")
    private String ueditorConfigPath;

    @RequestMapping("ueditor")
    @ResponseBody
    public String exec(HttpServletRequest request) throws JSONException, UnsupportedEncodingException {
        EditorConfig.ueditorConfigPath=ueditorConfigPath;
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getRealPath("/");
        return new ActionEnter(request, rootPath).exec();
    }

}
