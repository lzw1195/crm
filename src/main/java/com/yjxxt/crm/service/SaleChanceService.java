package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.SaleChanceMapper;
import com.yjxxt.crm.enums.DevResult;
import com.yjxxt.crm.enums.StateStatus;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());

        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销失败！");
    }

    /**
     * 1. 参数校验
     * 营销机会ID  非空，数据库中对应的记录存在
     * customerName客户名称    非空
     * linkMan联系人           非空
     * linkPhone联系号码       非空，手机号码格式正确
     * <p>
     * 2. 设置相关参数的默认值
     * updateDate更新时间  设置为系统当前时间
     * assignMan指派人
     * 原始数据未设置
     * 修改后未设置
     * 不需要操作
     * 修改后已设置
     * assignTime指派时间  设置为系统当前时间
     * 分配状态    1=已分配
     * 开发状态    1=开发中
     * 原始数据已设置
     * 修改后未设置
     * assignTime指派时间  设置为null
     * 分配状态    0=未分配
     * 开发状态    0=未开发
     * 修改后已设置
     * 判断修改前后是否是同一个指派人
     * 如果是，则不需要操作
     * 如果不是，则需要更新 assignTime指派时间  设置为系统当前时间
     * <p>
     * 3. 执行更新操作，判断受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
//         1. 参数校验
//        营销机会ID 非空，数据库中对应的记录存在
        AssertUtil.isTrue(saleChance.getId() == null, "Id不存在");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null, "记录不存在");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setCreateDate(new Date());

        if (StringUtils.isBlank(temp.getAssignMan())){
            if (!StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        } else {
            if (StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    saleChance.setAssignTime(new Date());
                } else {
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"更新营销机会失败");
    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "手机号码格式不正确");
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length == 0,"请选择要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销数据删除失败！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(id == null,"id不存在");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null,"数据不存在");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"更新失败");
    }
}
