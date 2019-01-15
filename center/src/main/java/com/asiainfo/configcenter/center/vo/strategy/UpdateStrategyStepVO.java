package com.asiainfo.configcenter.center.vo.strategy;

import com.asiainfo.configcenter.center.entity.CcStrategyStepConstructorEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepMethodEntity;

/**
 * Created by oulc on 2018/8/10.
 */
public class UpdateStrategyStepVO {
    private int strategyStepId;
    private String strategyType;
    private byte stepNum;
    private String desc;
    private CcStrategyStepFieldEntity ccStrategyStepFieldEntity;
    private CcStrategyStepMethodEntity ccStrategyStepMethodEntity;
    private CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity;

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

    public CcStrategyStepConstructorEntity getCcStrategyStepConstructorEntity() {
        return ccStrategyStepConstructorEntity;
    }

    public void setCcStrategyStepConstructorEntity(CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity) {
        this.ccStrategyStepConstructorEntity = ccStrategyStepConstructorEntity;
    }

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
}
