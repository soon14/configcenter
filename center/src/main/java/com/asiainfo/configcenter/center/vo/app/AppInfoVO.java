package com.asiainfo.configcenter.center.vo.app;

/**
 * Created by oulc on 2018/7/27.
 */
public class AppInfoVO {
    private int appId;
    private int envId;
    private int insId;
    private String appName;
    private String envName;
    private String insName;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }
}
