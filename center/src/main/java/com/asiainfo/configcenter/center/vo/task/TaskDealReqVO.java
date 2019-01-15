package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.util.Map;

/**
 * 任务处理请求参数
 * Created by bawy on 2018/8/2 16:44.
 */
public class TaskDealReqVO implements Serializable {

    private static final long serialVersionUID = -5093941375398772828L;

    private int taskId;
    private byte step;
    private byte operateType;
    private String remark;
    private int nextOperator;
    private Map<String, String> extInfo;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public byte getOperateType() {
        return operateType;
    }

    public void setOperateType(byte operateType) {
        this.operateType = operateType;
    }

    public int getNextOperator() {
        return nextOperator;
    }

    public void setNextOperator(int nextOperator) {
        this.nextOperator = nextOperator;
    }
}
