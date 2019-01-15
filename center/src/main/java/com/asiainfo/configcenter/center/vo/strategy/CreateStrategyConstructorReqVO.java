package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/9.
 */
public class CreateStrategyConstructorReqVO implements BaseAppReqVO{
    private int appId;
    private int strategyId;
    private int strategyStepId;
    private String clazz;
    private String paramsType;
    private String paramsValue;
    private String description;
    private byte stepNum;

    public int getStrategyStepId() {
        return strategyStepId;
    }

    public void setStrategyStepId(int strategyStepId) {
        this.strategyStepId = strategyStepId;
    }

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    public byte getStepNum() {
        return stepNum;
    }

    public void setStepNum(byte stepNum) {
        this.stepNum = stepNum;
    }

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

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getParamsType() {
        return paramsType;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public String getParamsValue() {
        return paramsValue;
    }

    public void setParamsValue(String paramsValue) {
        this.paramsValue = paramsValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
