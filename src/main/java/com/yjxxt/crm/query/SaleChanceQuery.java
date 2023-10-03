package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

/**
 * @author 刘志伟
 * @version 1.0
 */
public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名
    private String createMan;//创建人
    private Integer state;//状态
    private String devResult;
    private Integer assignMan;

    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String customerName, String createMan, Integer state) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state=" + state +
                ", devResult='" + devResult + '\'' +
                ", assignMan=" + assignMan +
                '}';
    }
}
