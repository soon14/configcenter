package com.asiainfo.configcenter.client.vo;

import java.io.Serializable;

public class ClientRespVO implements Serializable{

    private static final long serialVersionUID = -9088001618901315624L;

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
