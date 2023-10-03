package com.yjxxt.crm.dao;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.vo.User;
import java.util.List;
import java.util.Map;
public interface UserMapper extends BaseMapper<User, Integer> {
    User queryUserByName(String uname);
    List<Map<String, Object>> queryAllSales();
}