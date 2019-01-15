package com.asiainfo.configcenter.center.vo.configItem;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 配置项历史查询请求参数
 * Created by bawy on 2018/8/7 14:43.
 */
public class ConfigItemHisReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -7433997790329875175L;

    private int envId;
    private int configItemId;
    private int configItemHisId;
    private String creatorName;

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

    public int getConfigItemId() {
        return configItemId;
    }

    public void setConfigItemId(int configItemId) {
        this.configItemId = configItemId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getConfigItemHisId() {
        return configItemHisId;
    }

    public void setConfigItemHisId(int configItemHisId) {
        this.configItemHisId = configItemHisId;
    }
}
