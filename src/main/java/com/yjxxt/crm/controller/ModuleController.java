package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.model.TreeModel;
import com.yjxxt.crm.service.ModuleService;
import com.yjxxt.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("module")
@Controller
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return  moduleService.queryAllModules(roleId);
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModuleList(){
        return moduleService.queryModulesList();
    }

    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    @PostMapping ("add")
    @ResponseBody
    public ResultInfo add(Module module){
        moduleService.addModule(module);
        return success("添加module成功");
    }

    @PostMapping ("update")
    @ResponseBody
    public ResultInfo update(Module module){
        moduleService.updateModule(module);
        return success("更新module成功");
    }

    @PostMapping ("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        moduleService.deleteModule(id);
        return success("删除module成功");
    }

    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade,Integer parentId,HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }

    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer id, Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

}
