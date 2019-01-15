package com.asiainfo.configcenter.center.vo.task;

import java.io.Serializable;
import java.util.Map;

/**
 * 提交任务请求参数对象
 * Created by bawy on 2018/8/1 19:56.
 */
public class SubmitTaskReqVO implements Serializable {

    private static final long serialVersionUID = -5148935063694961659L;

    private int taskId;
    private String taskName;
    private String description;
    private int nextOperator;
    private Map<String,String> extInfo;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public int getNextOperator() {
        return nextOperator;
    }

    public void setNextOperator(int nextOperator) {
        this.nextOperator = nextOperator;
    }
}
