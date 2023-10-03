package com.yjxxt.crm.controller;

import com.yjxxt.crm.annoation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.enums.StateStatus;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.utils.CookieUtil;
import com.yjxxt.crm.utils.LoginUserUtil;
import com.yjxxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag,HttpServletRequest request) {
        if (flag != null && flag == 1) {
            saleChanceQuery.setState(StateStatus.STATED.getType());
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    @RequiredPermission(code = "101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        String userName = CookieUtil.getCookieValue(request, "userName");
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        return success("营销数据添加成功！");
    }

    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        saleChanceService.updateSaleChance(saleChance);
        return success("营销数据更新成功！");
    }

    //进入添加或者修改页面
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        if (saleChanceId != null) {
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    @RequiredPermission(code = "101003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        saleChanceService.deleteSaleChance(ids);
        return success();
    }

    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }
}
