package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.UserRoleMapper;
import com.yjxxt.crm.vo.Role;
import com.yjxxt.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}
