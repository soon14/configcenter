package com.asiainfo.configcenter.center.vo.strategy;

import java.util.List;

/**
 * Created by oulc on 2018/8/10.
 */
public class ConfigUpdateStrategyVO {
    private int strategyId;
    private String strategyName;
    private String description;
    private List<UpdateStrategyStepVO> strategyStepVOS;

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UpdateStrategyStepVO> getStrategyStepVOS() {
        return strategyStepVOS;
    }

    public void setStrategyStepVOS(List<UpdateStrategyStepVO> strategyStepVOS) {
        this.strategyStepVOS = strategyStepVOS;
    }
}
