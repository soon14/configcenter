package com.asiainfo.configcenter.center.vo.env;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 删除环境请求参数对象
 * Created by oulc on 2018/7/25.
 */
public class DelAppEnvReqVO implements BaseAppReqVO {


    private int envId;

    private static final long serialVersionUID = 1992666785836308131L;

    public void setEnvId(int envId){
        this.envId = envId;
    }

    @Override
    public int getAppId() {
        return 0;
    }

    @Override
    public int getEnvId() {
        return envId;
    }
}
