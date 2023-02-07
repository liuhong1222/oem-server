/**
 *
 */
package com.credit.oem.admin.common.interceptor;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.credit.oem.admin.common.annotation.RepeatCommitToken;
import com.credit.oem.admin.common.utils.RedisKeys;
import com.credit.oem.admin.common.utils.RedisUtils;
import com.credit.oem.admin.common.utils.ShiroUtils;
import com.credit.oem.admin.modules.sys.entity.SysUserEntity;


/**
 * @author ChuangLan
 */
@Component
public class RepeatCommitInterceptor extends HandlerInterceptorAdapter {

    /**
     * repeat commit token
     */
    private static Long repeatTokenExpireSeconds = 30L;

    /**
     * 重复提交token,存放heards
     */
    private static final String HEADER_NAME = "commit_token";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean result = true;

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatCommitToken annotation = method.getAnnotation(RepeatCommitToken.class);

            if (annotation != null) {

                boolean validate = annotation.validate();
                SysUserEntity user = ShiroUtils.getUserEntity();
                String redisValue = redisUtils.get(RedisKeys.getUserRepeatCommitKey(user.getUserId()) + method.getName());
                if (validate) {
                    String commitToken = request.getHeader(HEADER_NAME);
                    if (StringUtils.isEmpty(commitToken)) {
                        for (Cookie cookie : request.getCookies()) {
                            if (cookie.getName().equals(HEADER_NAME)) {
                                commitToken = cookie.getValue();
                                break;
                            }
                        }
                    }
                    if (user != null && user.getUserId() != null) {

                        if (StringUtils.isNotBlank(commitToken) && commitToken.equals(redisValue) && redisValue != null) {
                            result = false;
                        } else {
                            result = true;
                        }
                    }
                }
                if (user != null && user.getUserId() != null && result && redisValue == null) {
                    String randomStr = UUID.randomUUID().toString();
                    redisUtils.set(RedisKeys.getUserRepeatCommitKey(user.getUserId()) + method.getName(), randomStr, repeatTokenExpireSeconds);
                    Cookie cookie = new Cookie(HEADER_NAME, randomStr);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setSecure(false);
                    cookie.setMaxAge(repeatTokenExpireSeconds.intValue());
                    response.addCookie(cookie);
                    //response.flushBuffer();
                }
            }
        }

        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        SysUserEntity user = ShiroUtils.getUserEntity();
        if (handler instanceof HandlerMethod && user != null && user.getUserId() != null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String redisValue = redisUtils.get(RedisKeys.getUserRepeatCommitKey(user.getUserId()) + method.getName());
            if (redisValue != null) {
                redisUtils.delete(RedisKeys.getUserRepeatCommitKey(user.getUserId()) + method.getName());
            }
        }
    }


}
