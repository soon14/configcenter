package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 我提交的任务请求参数对象
 * Created by bawy on 2018/8/4.
 */
public class MySubmitTaskReqVO implements Serializable {

    private static final long serialVersionUID = 8110957277687219838L;

    private byte taskType;
    private byte taskState;
    private Timestamp startDate;
    private Timestamp endDate;

    public byte getTaskType() {
        return taskType;
    }

    public void setTaskType(byte taskType) {
        this.taskType = taskType;
    }

    public byte getTaskState() {
        return taskState;
    }

    public void setTaskState(byte taskState) {
        this.taskState = taskState;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
