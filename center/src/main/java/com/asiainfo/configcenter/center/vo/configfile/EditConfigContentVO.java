package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/7.
 */
public class EditConfigContentVO implements BaseAppReqVO{
    private String configFileContent;
    private int configId;
    private int envId;

    @Override
    public int getAppId() {
        return 0;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public String getConfigFileContent() {
        return configFileContent;
    }

    public void setConfigFileContent(String configFileContent) {
        this.configFileContent = configFileContent;
    }
}
