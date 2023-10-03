package com.yjxxt.crm;

import com.alibaba.fastjson.JSON;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.exceptions.AuthException;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.interceptor.NoLoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author 刘志伟
 * @version 1.0
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler, Exception ex) {

        if (ex instanceof NoLoginException){
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }



        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "异常，请重试");

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (responseBody == null) {
                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("mas", p.getMsg());
                } else if (ex instanceof AuthException) {
                    AuthException p = (AuthException) ex;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("mas", p.getMsg());
                }
                return modelAndView;
            } else {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("异常异常，请重试！");

                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                } else  if (ex instanceof AuthException) {
                    AuthException p = (AuthException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    String json = JSON.toJSONString(resultInfo);
                    out.write(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }


        return null;
    }
}
