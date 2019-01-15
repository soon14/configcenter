package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/9.
 */
public class CreateStrategyFieldReqVO implements BaseAppReqVO{
    private int appId;
    private int strategyId;
    private int strategyStepId;
    private String clazz;
    private String fieldName;
    private String classInstance;
    private String fieldValue;
    private String dataType;
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

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getClassInstance() {
        return classInstance;
    }

    public void setClassInstance(String classInstance) {
        this.classInstance = classInstance;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Override
    public int getEnvId() {
        return 0;
    }
}
