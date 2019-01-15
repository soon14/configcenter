package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.vo.user.UserInfoVO;

/**
 * 控制器基类
 * Created by bawy on 2018/7/5 16:11.
 */
public class BaseController {

    private static final ThreadLocal<UserInfoVO> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserInfoVO userInfo){
        currentUser.set(userInfo);
    }

    public static UserInfoVO getCurrentUser(){
        return currentUser.get();
    }
}
