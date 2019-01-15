package com.asiainfo.configcenter.center.service.interfaces;

import org.quartz.JobDataMap;
import org.quartz.SchedulerException;

public interface ITimeTaskSV {
    /**
     * 创建一个定时任务
     * @author oulc
     * @date 18-8-22 上午10:35
     * @param pushTime 执行时间
     * @param groupName 组名称
     * @param taskName 任务名称
     * @param jobDataMap 任务数据
     */
    void createTask(long pushTime, String groupName, String taskName, JobDataMap jobDataMap)throws SchedulerException;
}
