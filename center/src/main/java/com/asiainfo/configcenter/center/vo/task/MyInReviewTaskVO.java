package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 待审核的任务VO对象
 * Created by bawy on 2018/8/4.
 */
public class MyInReviewTaskVO implements Serializable {


    private static final long serialVersionUID = -5165388598837569260L;

    private int taskId;
    private int envId;
    private byte step;
    private String taskName;
    private String appName;
    private String envName;
    private String creatorName;
    private String description;
    private byte taskType;
    private byte taskState;
    private Timestamp createTime;
    private Timestamp updateTime;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getTaskType() {
        return taskType;
    }

    public void setTaskType(byte taskType) {
        this.taskType = taskType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public byte getTaskState() {
        return taskState;
    }

    public void setTaskState(byte taskState) {
        this.taskState = taskState;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }
}
