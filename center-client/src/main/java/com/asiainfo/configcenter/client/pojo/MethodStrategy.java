package com.asiainfo.configcenter.client.pojo;

public class MethodStrategy extends Strategy{
    private String methodName;
    private String classInstance;
    private String paramsType;
    private String paramsValue;

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
}
