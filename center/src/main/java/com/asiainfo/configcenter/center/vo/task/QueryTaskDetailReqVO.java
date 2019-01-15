package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;

/**
 * 获取待提交任务请求参数
 * Created by bawy on 2018/8/2 13:58.
 */
public class QueryTaskDetailReqVO implements Serializable {

    private static final long serialVersionUID = -1614988943639190969L;

    private byte taskState;
    private int envId;
    private int taskId;

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public byte getTaskState() {
        return taskState;
    }

    public void setTaskState(byte taskState) {
        this.taskState = taskState;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
