package com.yjxxt.crm.model;

/**
 * @author 刘志伟
 * @version 1.0
 */
public class UserModel {
    //    private Integer userId;
    private String userIdStr;
    private String userName;
    private String trueName;

    public UserModel() {
    }

    public UserModel(String userIdStr, String userName, String trueName) {
        this.userIdStr = userIdStr;
        this.userName = userName;
        this.trueName = trueName;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userIdStr='" + userIdStr + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                '}';
    }
}
