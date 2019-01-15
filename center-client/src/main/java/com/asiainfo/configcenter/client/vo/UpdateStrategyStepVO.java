package com.asiainfo.configcenter.client.vo;

import com.asiainfo.configcenter.client.pojo.CcStrategyStepConstructorEntity;
import com.asiainfo.configcenter.client.pojo.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.client.pojo.CcStrategyStepMethodEntity;

public class UpdateStrategyStepVO {
    private int strategyStepId;
    private String strategyType;
    private byte stepNum;
    private String desc;
    private CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity;
    private CcStrategyStepFieldEntity ccStrategyStepFieldEntity;
    private CcStrategyStepMethodEntity ccStrategyStepMethodEntity;

    public int getStrategyStepId() {
        return strategyStepId;
    }

    public void setStrategyStepId(int strategyStepId) {
        this.strategyStepId = strategyStepId;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public byte getStepNum() {
        return stepNum;
    }

    public void setStepNum(byte stepNum) {
        this.stepNum = stepNum;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CcStrategyStepConstructorEntity getCcStrategyStepConstructorEntity() {
        return ccStrategyStepConstructorEntity;
    }

    public void setCcStrategyStepConstructorEntity(CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity) {
        this.ccStrategyStepConstructorEntity = ccStrategyStepConstructorEntity;
    }

    public CcStrategyStepFieldEntity getCcStrategyStepFieldEntity() {
        return ccStrategyStepFieldEntity;
    }

    public void setCcStrategyStepFieldEntity(CcStrategyStepFieldEntity ccStrategyStepFieldEntity) {
        this.ccStrategyStepFieldEntity = ccStrategyStepFieldEntity;
    }

    public CcStrategyStepMethodEntity getCcStrategyStepMethodEntity() {
        return ccStrategyStepMethodEntity;
    }

    public void setCcStrategyStepMethodEntity(CcStrategyStepMethodEntity ccStrategyStepMethodEntity) {
        this.ccStrategyStepMethodEntity = ccStrategyStepMethodEntity;
    }
}
