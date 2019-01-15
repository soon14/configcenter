package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

public class UserInfoAndRoleReqVO implements Serializable {
    private String nickName = "";
    private int userStatus = -1;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
