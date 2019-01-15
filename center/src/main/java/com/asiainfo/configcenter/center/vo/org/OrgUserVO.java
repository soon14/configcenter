package com.asiainfo.configcenter.center.vo.org;

import java.io.Serializable;

/**
 * 组织中的角色信息
 * Created by bawy on 2018/8/21 17:04.
 */
public class OrgUserVO implements Serializable{

    private static final long serialVersionUID = 2538920245372981559L;

    private int userId;
    private String nickname;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
