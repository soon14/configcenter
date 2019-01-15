package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 任务流程步骤对象
 * Created by bawy on 2018/8/7 9:20.
 */
public class TaskStepVO implements Serializable {

    private static final long serialVersionUID = 6331901391637394700L;

    private byte step;
    private byte operationState;
    private String reason;
    private String operatorName;
    private Timestamp updateTime;

    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public byte getOperationState() {
        return operationState;
    }

    public void setOperationState(byte operationState) {
        this.operationState = operationState;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
