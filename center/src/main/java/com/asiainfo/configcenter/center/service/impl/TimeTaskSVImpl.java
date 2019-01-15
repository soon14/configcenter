package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.ErrorCodeException;
import com.asiainfo.configcenter.center.common.ErrorInfo;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.service.interfaces.ITimeTaskSV;
import com.asiainfo.configcenter.center.task.PushConfigTask;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 定时任务业务层代码
 * @author oulc
 * @date 18-8-22 上午10:32
 */
@Service
public class TimeTaskSVImpl implements ITimeTaskSV {
    private static Logger logger = Logger.getLogger(TimeTaskSVImpl.class);

    @Resource
    private Scheduler scheduler;

    @Override
    public void createTask(long pushTime, String groupName, String taskName, JobDataMap jobDataMap)throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(PushConfigTask.class).withIdentity(taskName,groupName).setJobData(jobDataMap).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(taskName,groupName).startAt(new Date(pushTime)).build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}
