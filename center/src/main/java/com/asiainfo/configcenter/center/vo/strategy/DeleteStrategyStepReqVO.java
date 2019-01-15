package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/13.
 */
public class DeleteStrategyStepReqVO implements BaseAppReqVO {
    private int appId;
    private int strategyId;
    private int strategyStepId;

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

    public int getStrategyStepId() {
        return strategyStepId;
    }

    public void setStrategyStepId(int strategyStepId) {
        this.strategyStepId = strategyStepId;
    }

    @Override
    public int getEnvId() {
        return 0;
    }
}
