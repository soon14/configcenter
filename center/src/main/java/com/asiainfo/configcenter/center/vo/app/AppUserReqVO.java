package com.asiainfo.configcenter.center.vo.app;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 应用用户请求参数对象
 * Created by bawy on 2018/7/25 16:15.
 */
public class AppUserReqVO implements BaseAppReqVO {


    private static final long serialVersionUID = -8333046559719086275L;

    private int appId;
    private int userId;
    private String nickName;
    private int roleId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAppId(int appId){
        this.appId = appId;
    }

    @Override
    public int getAppId() {
        return appId;
    }

    @Override
    public int getEnvId() {
        return 0;
    }
}
