package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 我提交的任务VO对象
 * Created by bawy on 2018/8/4.
 */
public class MySubmitTaskVO implements Serializable {

    private static final long serialVersionUID = -6059593143103333323L;

    private int taskId;
    private String taskName;
    private String appName;
    private String envName;
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

    public byte getTaskState() {
        return taskState;
    }

    public void setTaskState(byte taskState) {
        this.taskState = taskState;
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
}
