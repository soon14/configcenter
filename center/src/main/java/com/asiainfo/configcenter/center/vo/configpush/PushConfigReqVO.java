package com.asiainfo.configcenter.center.vo.configpush;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

public class PushConfigReqVO implements BaseAppReqVO {
    private int envId;
    private byte configType;//1:配置文件 2：配置项
    private int configId;
    private String configVersion;


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
