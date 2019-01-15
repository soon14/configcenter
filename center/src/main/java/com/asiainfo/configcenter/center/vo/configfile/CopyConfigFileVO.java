package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/7.
 */
public class CopyConfigFileVO implements BaseAppReqVO{
    private int originEnvId;
    private int envId;//targetEnvId
    private int [] configIds;

    public int getOriginEnvId() {
        return originEnvId;
    }

    public void setOriginEnvId(int originEnvId) {
        this.originEnvId = originEnvId;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public int[] getConfigIds() {
        return configIds;
    }

    public void setConfigIds(int[] configIds) {
        this.configIds = configIds;
    }

    @Override
    public int getAppId() {
        return 0;
    }
}
