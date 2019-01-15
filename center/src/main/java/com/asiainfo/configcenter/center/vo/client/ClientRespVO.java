package com.asiainfo.configcenter.center.vo.client;

import com.asiainfo.configcenter.center.vo.strategy.ConfigUpdateStrategyVO;

import java.io.Serializable;

public class ClientRespVO implements Serializable{

    private static final long serialVersionUID = 115377852072472479L;

    private int configId;
    private String name;
    private String content;
    private String version;
    private ConfigUpdateStrategyVO configUpdateStrategyVO;

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ConfigUpdateStrategyVO getConfigUpdateStrategyVO() {
        return configUpdateStrategyVO;
    }

    public void setConfigUpdateStrategyVO(ConfigUpdateStrategyVO configUpdateStrategyVO) {
        this.configUpdateStrategyVO = configUpdateStrategyVO;
    }
}
