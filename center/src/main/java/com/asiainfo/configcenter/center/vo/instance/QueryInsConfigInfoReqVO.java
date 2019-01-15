package com.asiainfo.configcenter.center.vo.instance;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 查询实例配置信息请求参数对象
 * Created by oulc on 2018/7/27.
 */
public class QueryInsConfigInfoReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -1648983260590222771L;

    private int envId;
    private int insId;

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

    public int getInsId() {
        return insId;
    }

    public void setInsId(int insId) {
        this.insId = insId;
    }
}
