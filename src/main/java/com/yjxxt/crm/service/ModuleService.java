package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.ModuleMapper;
import com.yjxxt.crm.dao.PermissionMapper;
import com.yjxxt.crm.model.TreeModel;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class ModuleService extends BaseService<Module, Integer> {
    public static final String 授权码为空 = "url已存在";
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeModel> queryAllModules(Integer roleId) {
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        if (permissionIds != null && permissionIds.size() > 0) {
            treeModelList.forEach(treeModel -> {
                if (permissionIds.contains(treeModel.getId())) {
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }

    public Map<String, Object> queryModulesList() {
        Map<String, Object> map = new HashMap<>();
        List<Module> moduleList = moduleMapper.queryAllModuleList();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", moduleList.size());
        map.put("data", moduleList);
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module) {
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 1 || grade == 2 || grade == 0), "grade不正确");
        //模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称为空");
        AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndName(grade, module.getModuleName()), "模块名称已经存在");
        //地址
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "url为空");
            AssertUtil.isTrue(null != moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl()), "url已存在");
        }
        //父级菜单
        if (grade == 0) {
            module.setParentId(-1);
        }

        if (grade != 0) {
            AssertUtil.isTrue(null == module.getParentId(), "父级菜单不能为空");
            AssertUtil.isTrue(null == moduleMapper.selectByPrimaryKey(module.getParentId()), "父级菜单不存在");
        }
        //授权码
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "授权码为空");
        AssertUtil.isTrue(null != moduleMapper.queryModuleByOptValue(module.getOptValue()), "授权码已存在");
        //设置默认参数
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        //执行添加操作
        AssertUtil.isTrue(moduleMapper.insertSelective(module) != 1, "添加module失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        AssertUtil.isTrue(null == module.getId(), "待更新记录不存在");
        Module temp = moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 1 || grade == 2 || grade == 0), "grade不正确");
        //模块名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称为空");
        temp = moduleMapper.queryModuleByGradeAndName(grade, module.getModuleName());
        if (temp != null) {
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "该层级下菜单名存在");
        }
        //地址url
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "url为空");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl());
            if (temp != null) {
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "url已存在");
            }
            //授权码
            AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "授权码为空");
            temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
            if (temp != null) {
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "授权码已存在");
            }

        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1, "更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        Module temp = moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == temp,"待删除记录不存在");
        Integer count = moduleMapper.queryModuleByParentId(id);
        AssertUtil.isTrue(count > 0,"该资源存在子记录，不可删除！");
        count = permissionMapper.countPermissionByModuleId(id);
        if (count > 0){
            permissionMapper.deletePermissionByModuleId(id);
        }
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp)<1,"删除失败");
    }
}

