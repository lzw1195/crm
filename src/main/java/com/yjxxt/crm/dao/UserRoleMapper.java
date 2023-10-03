package com.yjxxt.crm.dao;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    Integer countUSerRoleByUserId(Integer userId);

    Integer deleteUserRoleByUserId(Integer userId);
}