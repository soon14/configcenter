package com.asiainfo.configcenter.center.vo.instance;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

public class QueryInsByConfigReqVO implements BaseAppReqVO {
    private int envId;
    private byte configType;
    private int configId;
    private String configVersion;
    private String insName;
    private String insIp;

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public String getInsIp() {
        return insIp;
    }

    public void setInsIp(String insIp) {
        this.insIp = insIp;
    }

    @Override
    public int getAppId() {
        return 0;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public byte getConfigType() {
        return configType;
    }

    public void setConfigType(byte configType) {
        this.configType = configType;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }
}
