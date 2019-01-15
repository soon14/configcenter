package com.asiainfo.configcenter.center.vo.instance;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/7/31.
 */
public class DeleteInsVO implements BaseAppReqVO {
    private int insId;
    private int envId;

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
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
