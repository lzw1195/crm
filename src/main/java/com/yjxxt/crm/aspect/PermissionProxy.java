package com.yjxxt.crm.aspect;

import com.yjxxt.crm.annoation.RequiredPermission;
import com.yjxxt.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession httpSession;

    @Around(value = "@annotation(com.yjxxt.crm.annoation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        List<String> permissions = (List<String>) httpSession.getAttribute("permissions");
        if (permissions == null || permissions.size() < 1) {
            throw new AuthException();
        }

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        if (!permissions.contains(requiredPermission.code())) {
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }
}
