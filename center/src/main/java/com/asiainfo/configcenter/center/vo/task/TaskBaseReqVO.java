package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;

/**
 * 任务请求基础参数对象
 * Created by bawy on 2018/8/2.
 */
public class TaskBaseReqVO implements Serializable {

    private static final long serialVersionUID = -9034647501832402759L;

    private int taskId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
