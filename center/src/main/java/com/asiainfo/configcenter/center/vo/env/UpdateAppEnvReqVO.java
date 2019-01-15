package com.asiainfo.configcenter.center.vo.env;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/7/24.
 */
public class UpdateAppEnvReqVO implements BaseAppReqVO {
    private int envId;
    private String envName;
    private String desc;

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int getAppId() {
        return 0;
    }
}
