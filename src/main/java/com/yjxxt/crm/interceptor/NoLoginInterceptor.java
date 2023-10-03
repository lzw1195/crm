package com.yjxxt.crm.interceptor;

import com.yjxxt.crm.dao.UserMapper;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 刘志伟
 * @version 1.0
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if (userId == null || userMapper.selectByPrimaryKey(userId) == null) {
            throw new NoLoginException();
        }
        return true;
    }
}
