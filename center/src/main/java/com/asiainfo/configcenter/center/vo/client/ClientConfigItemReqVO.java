package com.asiainfo.configcenter.center.vo.client;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Author: Erick
 * Date: 2018/8/30
 *
 * client端请求参数
 */
public class ClientConfigItemReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -390995859910232927L;

    private String appName;
    private String envName;
    private ClientConfigItemContent[] clientConfigItemContents;

    public ClientConfigItemContent[] getClientConfigItemContents() {
        return clientConfigItemContents;
    }

    public void setClientConfigItemContents(ClientConfigItemContent[] clientConfigItemContents) {
        this.clientConfigItemContents = clientConfigItemContents;
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

    @Override
    public int getAppId() {
        return 0;
    }

    @Override
    public int getEnvId() {
        return 0;
    }

}
