package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

public class EnvAllConfigFileReqVO implements BaseAppReqVO {
    private int envId;

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    @Override
    public int getAppId() {
        return 0;
    }
}
