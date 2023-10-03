package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import com.yjxxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author  刘志伟
* @version 1.0
*/

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    //系统登录页
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    //系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }


    //后端管理主页面
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);
        List<String> permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }


}
