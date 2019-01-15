package com.asiainfo.configcenter.center.task;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.service.interfaces.IConfigPushSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PushConfigTask extends QuartzJobBean {
    private static Logger logger = Logger.getLogger(PushConfigTask.class);

    @Resource
    private IConfigPushSV configPushSV;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        int taskId = jobDataMap.getInt(ProjectConstants.QUARTZ_PUSH_CONFIG_TASK_ID);
        logger.info("********************执行推送任务:"+ taskId);
        configPushSV.dealPushTimeTask(taskId);
    }
}
