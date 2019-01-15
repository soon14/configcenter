package com.asiainfo.configcenter.center.vo.env;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 环境创建、修改请求参数
 * Created by oulc on 2018/7/24.
 */
public class AppEnvReqVO implements BaseAppReqVO {
    private int appId;
    private int envId;
    private String envName;
    private String desc;

    @Override
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId){
        this.envId = envId;
    }

}
