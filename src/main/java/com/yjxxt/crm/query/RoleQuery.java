package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

/**
 * @author 刘志伟
 * @version 1.0
 */
public class RoleQuery extends BaseQuery {
    String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
