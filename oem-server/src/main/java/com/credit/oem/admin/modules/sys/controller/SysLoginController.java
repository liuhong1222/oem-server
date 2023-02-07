package com.credit.oem.admin.modules.sys.controller;

import com.credit.oem.admin.common.utils.ConfigConstant;
import com.credit.oem.admin.common.utils.MD5Util;
import com.credit.oem.admin.common.utils.R;
import com.credit.oem.admin.modules.agent.service.AgentTrdOrderIdService;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;
import com.credit.oem.admin.modules.sys.form.SysLoginForm;
import com.credit.oem.admin.modules.sys.service.SysCaptchaService;
import com.credit.oem.admin.modules.sys.service.SysUserService;
import com.credit.oem.admin.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
@RequestMapping("/open")
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    AgentTrdOrderIdService agentTrdOrderIdService;

    /**
     * 验证码
     */
    @GetMapping("/captcha2")
    public void captchaJpg(HttpServletResponse response, String uuid) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        sysCaptchaService.getCaptcha(uuid, response.getOutputStream());

        ServletOutputStream out = response.getOutputStream();
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody SysLoginForm form) throws IOException {
/*		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}*/

        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        R r = sysUserTokenService.createToken(user.getUserId());
        Boolean flag = false;
        //判断用户是否首次登录
        if (user.getPassword().equals(new Sha256Hash(MD5Util.MD5(ConfigConstant.DEFAULT_PASSWORD).toLowerCase(), user.getSalt()).toHex())) {
            flag = true;
        }
        r.put("isFirstLogin", flag);
        return r;
    }


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout() {
        sysUserTokenService.logout(getUserId());
        return R.ok();
    }


    /**
     * 退出
     */
    @RequestMapping("/test")
    public R test() {
        agentTrdOrderIdService.insertFromTrdorder();
        return R.ok();
    }

}
