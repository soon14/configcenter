package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

/**
 * 修改密码请求对象
 * Created by bawy on 2018/7/18 20:19.
 */
public class PasswordVO implements Serializable {


    private static final long serialVersionUID = 6647970169547062601L;


    private String oldPass;
    private String newPass;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
