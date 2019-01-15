package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/9.
 */
public class UpdateStrategyStepNumVO implements BaseAppReqVO {
    private int appId;
    private int strategyId;
    private UpdateStrategyStepInfoReqVO[] strategyStepInfoVOS;

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public UpdateStrategyStepInfoReqVO[] getStrategyStepInfoVOS() {
        return strategyStepInfoVOS;
    }

    public void setStrategyStepInfoVOS(UpdateStrategyStepInfoReqVO[] strategyStepInfoVOS) {
        this.strategyStepInfoVOS = strategyStepInfoVOS;
    }

    @Override
    public int getEnvId() {
        return 0;
    }
}
