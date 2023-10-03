package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import com.yjxxt.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    @RequestMapping("index")
    public String index(){
        return "/role/role";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("更新成功");
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("更新成功");
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        return success("角色删除成功");
    }

    @RequestMapping ("toAddOrUpdateRolePage")
    public String toRolePage(Integer roleId, HttpServletRequest request){
        if (roleId != null){
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",role);
        }
        return "/role/add_update";
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @PostMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
        roleService.addGrant(roleId,mIds);
        return success("角色授权成功");
    }
}
