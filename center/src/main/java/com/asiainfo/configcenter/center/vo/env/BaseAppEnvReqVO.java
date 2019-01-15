package com.asiainfo.configcenter.center.vo.env;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 基础环境请求参数对象
 * Created by bawy on 2018/8/23 18:05.
 */
public class BaseAppEnvReqVO implements BaseAppReqVO {

    int envId;

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
