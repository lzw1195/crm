package com.yjxxt.crm.dao;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    Integer countPermissionByRoleId(Integer roleId);

    void deletePermissionByRoleId(Integer roleId);

    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    Integer countPermissionByModuleId(Integer id);

    Integer deletePermissionByModuleId(Integer id);
}