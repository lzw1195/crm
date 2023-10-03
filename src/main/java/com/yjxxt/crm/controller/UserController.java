package com.yjxxt.crm.controller;

import com.yjxxt.crm.GlobalExceptionResolver;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import com.yjxxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 刘志伟
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @PostMapping("login")
    @ExceptionHandler(ParamsException.class)
    @ResponseBody
    public ResultInfo userLogin(@Valid User user) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(user);
        resultInfo.setResult(userModel);
//        try {
//
//        } catch (ParamsException p) {
//            p.printStackTrace();
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultInfo.setCode(500);
//            resultInfo.setMsg("操作失败！");
//        }
        return resultInfo;
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);
//        try {
//
//        } catch (ParamsException p) {
//            p.printStackTrace();
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){return userService.queryAllSales();}

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功");
    }

    @RequestMapping("toAddOrUpdateUserPage")
    public String openSaleChanceDialog(HttpServletRequest request,Integer id){
        if (id != null) {
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }

    @PostMapping("deleteUser")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteByIds(ids);
        return success("用户删除成功");
    }

}
