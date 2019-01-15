package com.asiainfo.configcenter.center.vo.task;

import com.asiainfo.configcenter.center.vo.strategy.ConfigUpdateStrategyVO;

/**
 * 刷新策略变更详情对象
 * Created by bawy on 2018/8/27 14:42.
 */
public class StrategyChangeDetailVO {

    private ConfigUpdateStrategyVO oldStrategy;
    private ConfigUpdateStrategyVO newStrategy;

    public ConfigUpdateStrategyVO getOldStrategy() {
        return oldStrategy;
    }

    public void setOldStrategy(ConfigUpdateStrategyVO oldStrategy) {
        this.oldStrategy = oldStrategy;
    }

    public ConfigUpdateStrategyVO getNewStrategy() {
        return newStrategy;
    }

    public void setNewStrategy(ConfigUpdateStrategyVO newStrategy) {
        this.newStrategy = newStrategy;
    }
}
