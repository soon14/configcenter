package com.asiainfo.configcenter.center.vo.env;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 查询应用环境请求参数对象
 * Created by oulc on 2018/7/25.
 */
public class QueryAppEnvReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -4857109873769587091L;

    private int appId;

    @Override
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Override
    public int getEnvId() {
        return 0;
    }

}
