package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dao.UserMapper;
import com.yjxxt.crm.dao.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import com.yjxxt.crm.vo.User;
import com.yjxxt.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户姓名不能为空！");
        // 验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserPwd()), "用户密码不能为空！");
        User u = userMapper.queryUserByName(user.getUserName());
        AssertUtil.isTrue(u == null, "用户不存在");
        String userPwd = Md5Util.encode(user.getUserPwd());
        AssertUtil.isTrue(!userPwd.equals(u.getUserPwd()), "密码不正确");
        return bulidUserInfo(u);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user, "待更新记录不存在");
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改密码失败！");
    }

    public void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原密码为空！");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原密码不正确！");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码为空！");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码与原密码相同！");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "确认密码不能为空！");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "新密码与确认密码不一致！");
    }

    public UserModel bulidUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), null);
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1, "添加失败");

        //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        AssertUtil.isTrue(user.getId() == null, "待更新记录不存在");
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在");
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "更新失败！");

        //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    private void relationUserRole(Integer userId, String roleIds) {
        Integer count = userRoleMapper.countUSerRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"删除用户角色失败");
        }
        if (StringUtils.isNoneBlank(roleIds)) {
            List<UserRole> userRoleList = new ArrayList<>();
            String[] roleIdArray = roleIds.split(",");

            for (String s : roleIdArray) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.valueOf(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),"用户角色分配失败！");
        }
    }

    private void checkUserParams(String userName, String email, String phone, Integer id) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名称不能为空");
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user != null && !(user.getId().equals(id)), "用户已存在");
        AssertUtil.isTrue(StringUtils.isBlank(email), "邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机格式不正确");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择要删除的数据");
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "营销数据删除失败！");

        for (Integer userId : ids) {
            Integer count = userRoleMapper.countUSerRoleByUserId(userId);
            if (count > 0) {
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"删除用户角色失败");
            }
        }
    }
}
