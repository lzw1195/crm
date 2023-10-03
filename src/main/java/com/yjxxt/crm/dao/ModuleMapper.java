package com.yjxxt.crm.dao;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.model.TreeModel;
import com.yjxxt.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    public List<TreeModel> queryAllModules();

    public List<Module> queryAllModuleList();

    Module queryModuleByGradeAndName(@Param("grade") Integer grade,@Param("moduleName") String moduleName);

    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade,@Param("url") String url);

    Module queryModuleByOptValue(String optValue);

    Integer queryModuleByParentId(Integer id);
}