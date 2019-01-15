package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;

/**
 * 任务下一操作员信息请求参数
 * Created by bawy on 2018/8/24 17:08.
 */
public class NextOperatorInfoReqVO implements Serializable {

    private static final long serialVersionUID = -6524855133326593442L;

    private int taskId;
    private byte step;

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
}
