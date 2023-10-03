package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.PermissionMapper;
import com.yjxxt.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    PermissionMapper permissionMapper;


    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
