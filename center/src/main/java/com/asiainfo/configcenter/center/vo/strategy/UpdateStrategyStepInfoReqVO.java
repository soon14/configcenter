package com.asiainfo.configcenter.center.vo.strategy;

/**
 * Created by oulc on 2018/8/9.
 */
public class UpdateStrategyStepInfoReqVO {
    private int strategyStepId;
    private byte stepNum;
    private String paramValue;

    public int getStrategyStepId() {
        return strategyStepId;
    }

    public void setStrategyStepId(int strategyStepId) {
        this.strategyStepId = strategyStepId;
    }


    public byte getStepNum() {
        return stepNum;
    }

    public void setStepNum(byte stepNum) {
        this.stepNum = stepNum;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
