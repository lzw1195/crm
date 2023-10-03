package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Controller
public class UserRoleContorller extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}
