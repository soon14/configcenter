package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

/**
 * 重置密码对象
 * Created by bawy on 2018/7/20 0:06.
 */
public class ResetPasswordVO implements Serializable {

    private static final long serialVersionUID = 8301692493368213445L;

    private String code;
    private String newPass;
    private String ensurePass;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getEnsurePass() {
        return ensurePass;
    }

    public void setEnsurePass(String ensurePass) {
        this.ensurePass = ensurePass;
    }
}
