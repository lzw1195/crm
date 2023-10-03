package com.yjxxt.crm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.dao.CusDevPlanMapper;
import com.yjxxt.crm.enums.StateStatus;
import com.yjxxt.crm.query.CusDevPlanQuery;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.CusDevPlanService;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.utils.LoginUserUtil;
import com.yjxxt.crm.vo.CusDevPlan;
import com.yjxxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Controller
@RequestMapping("/cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;


    @RequestMapping("/index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("/toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        request.setAttribute("saleChance",saleChance);
        return "/cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功");
    }
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("计划更新加成功");
    }

    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(HttpServletRequest request,Integer sId,Integer id){
        request.setAttribute("sId",sId);
        CusDevPlan cusDevPlan  = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo updateCusPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划更删除成功");
    }


}
