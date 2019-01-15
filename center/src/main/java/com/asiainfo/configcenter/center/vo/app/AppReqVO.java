package com.asiainfo.configcenter.center.vo.app;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * App创建、修改、删除请求参数对象
 * Created by bawy on 2018/7/24 11:20.
 */
public class AppReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = 6742494051611691525L;

    private int appId;
    private String appName;
    private String description;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Override
    public int getAppId() {
        return appId;
    }

    @Override
    public int getEnvId() {
        return 0;
    }
}
