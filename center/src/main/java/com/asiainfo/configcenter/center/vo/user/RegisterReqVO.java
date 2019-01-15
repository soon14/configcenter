package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

/**
 * 注册请求参数对象
 * Created by bawy on 2018/7/17 13:44.
 */
public class RegisterReqVO implements Serializable{

    private static final long serialVersionUID = -8087266436033951745L;

    private String username;
    private String password;
    private String email;
    private String nickname;
    private int orgId;
    private int orgRoleId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getOrgRoleId() {
        return orgRoleId;
    }

    public void setOrgRoleId(int orgRoleId) {
        this.orgRoleId = orgRoleId;
    }
}
