package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.CusDevPlanMapper;
import com.yjxxt.crm.dao.SaleChanceMapper;
import com.yjxxt.crm.query.CusDevPlanQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        checkCusDevPlan(cusDevPlan);
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setPlanDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1,"计划项数据添加失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(null == cusDevPlan.getId() || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常！");
        checkCusDevPlan(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项数据更新失败");
    }

    private void checkCusDevPlan(CusDevPlan cusDevPlan) {
        Integer id = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(id == null || saleChanceMapper.selectByPrimaryKey(id) == null,"数据异常");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"数据内容为空");
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null,"计划时间为空");
    }

    public void deleteCusDevPlan(Integer id) {
        AssertUtil.isTrue(id == null,"待删除记录不存在");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"删除失败！" );
    }
}
