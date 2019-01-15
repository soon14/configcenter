package com.asiainfo.configcenter.center.vo.system;

import java.io.Serializable;

/**
 * 登录请求参数对象
 * Created by bawy on 2018/7/6 13:48.
 */
public class LoginReqVO implements Serializable{

    private static final long serialVersionUID = 8128408640322358619L;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
