package com.asiainfo.configcenter.client.pojo;

public class CcStrategyStepMethodEntity {
    private int id;
    private int strategyStepId;
    private String clazz;
    private String methodName;
    private String classInstance;
    private String paramsType;
    private String paramsValue;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrategyStepId() {
        return strategyStepId;
    }

    public void setStrategyStepId(int strategyStepId) {
        this.strategyStepId = strategyStepId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassInstance() {
        return classInstance;
    }

    public void setClassInstance(String classInstance) {
        this.classInstance = classInstance;
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
