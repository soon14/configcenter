package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;

/**
 * 任务详情基础请求参数对象
 * Created by bawy on 2018/8/2.
 */
public class TaskDetailBaseReqVO implements Serializable {

    private static final long serialVersionUID = -4797465396337956646L;

    private int taskDetailId;
    private int taskId;


    public int getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(int taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
