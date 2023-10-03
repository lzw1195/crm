package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.ModuleMapper;
import com.yjxxt.crm.dao.PermissionMapper;
import com.yjxxt.crm.dao.RoleMapper;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.vo.Permission;
import com.yjxxt.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"角色已存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()),"角色等级为空");
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1,"角色添加失败");
    }

    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(role.getId() == null,"查询不到该角色");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(role.getId())), "角色名称已存在，不可使用！");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()),"角色等级为空");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1,"角色更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(roleId == null,"待删除记录不存在");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null,"待删除记录不存在");
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"删除失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        if (mIds != null && mIds.length > 0) {
            List<Permission> permissionsList = new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setCreateDate(new Date());
                permissionsList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionsList) != permissionsList.size(),"添加失败");
        }
    }
}
