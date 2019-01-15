package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/10.
 */
public class QueryStrategyAllReqVo implements BaseAppReqVO{

    private int appId;
    private int strategyId;

    @Override
    public int getEnvId() {
        return 0;
    }

    @Override
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }
}
