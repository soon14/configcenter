package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 修改刷新策略请求参数对象
 * Created by bawy on 2018/8/22 15:39.
 */
public class ChangeStrategyReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -4186364656507292904L;
    private Integer strategyId;
    private int configId;
    private int envId;


    @Override
    public int getAppId() {
        return 0;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }
}
