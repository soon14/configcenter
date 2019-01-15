package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 待审核的任务请求参数对象
 * Created by bawy on 2018/8/4.
 */
public class MyInReviewTaskReqVO implements Serializable {


    private static final long serialVersionUID = -307578021926318588L;

    private int taskId;
    private byte taskType;
    private String creatorName;
    private Timestamp startDate;
    private Timestamp endDate;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public byte getTaskType() {
        return taskType;
    }

    public void setTaskType(byte taskType) {
        this.taskType = taskType;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
