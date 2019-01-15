package com.asiainfo.configcenter.center.vo.task;

import com.asiainfo.configcenter.center.entity.CcInstanceEntity;

import java.io.Serializable;
import java.util.List;

/**

 * Author: Erick
 * Date: 2018/9/3
 *
 * 任务推送日期和推送实例 返回参数
 */
public class TaskTimeAndInsVO implements Serializable {

    private static final long serialVersionUID = 1977372959181128654L;

    private String pushTime;
    private List<TaskInsVO> taskInsVOS;

    public List<TaskInsVO> getTaskInsVOS() {
        return taskInsVOS;
    }

    public void setTaskInsVOS(List<TaskInsVO> taskInsVOS) {
        this.taskInsVOS = taskInsVOS;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}