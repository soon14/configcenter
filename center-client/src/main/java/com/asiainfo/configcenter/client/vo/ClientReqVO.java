package com.asiainfo.configcenter.client.vo;

import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigVO;

import java.util.List;

public class ClientReqVO {
    private String appName;
    private String envName;
    private List<ZKConfigVO> zkConfigs;

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

    public List<ZKConfigVO> getZkConfigs() {
        return zkConfigs;
    }

    public void setZkConfigs(List<ZKConfigVO> zkConfigs) {
        this.zkConfigs = zkConfigs;
    }
}
