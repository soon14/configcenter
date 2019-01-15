package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/2.
 */
public class DelConfigFileVO implements BaseAppReqVO{
    private int[] configIds;
    private int envId;
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }
}
