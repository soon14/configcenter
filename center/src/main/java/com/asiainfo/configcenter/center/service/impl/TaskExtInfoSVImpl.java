package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.TaskExtInfoRepository;
import com.asiainfo.configcenter.center.entity.CcTaskExtInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.ITaskExtInfoSV;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskExtInfoSVImpl implements ITaskExtInfoSV {

    @Resource
    private TaskExtInfoRepository taskExtInfoRepository;

    @Override
    public CcTaskExtInfoEntity findTaskExtInfoByTaskIdAndKey(int taskId, String extInfoKey) {
        return taskExtInfoRepository.findByTaskIdAndExtInfoKeyAndStatus(taskId,extInfoKey, ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcTaskExtInfoEntity findTaskExtInfoByTaskIdAndKeyCheck(int taskId, String extInfoKey) {
        CcTaskExtInfoEntity ccTaskExtInfoEntity = findTaskExtInfoByTaskIdAndKey(taskId,extInfoKey);
        Assert4CC.notNull(ccTaskExtInfoEntity, ResultCodeEnum.TASK_COMMON_ERROR,"任务扩展信息不存在,taskId:"+taskId+",extInfoKey:"+extInfoKey);
        return ccTaskExtInfoEntity;
    }
}
