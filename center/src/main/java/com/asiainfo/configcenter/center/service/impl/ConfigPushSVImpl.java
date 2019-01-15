package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcStringUtil;
import com.asiainfo.configcenter.center.util.JSONUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configpush.PushConfigReqVO;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigPushVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigVO;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConfigPushSVImpl implements IConfigPushSV {
    private static Logger logger = Logger.getLogger(ConfigPushSVImpl.class);

    @Resource
    private ITaskSV iTaskSV;

    @Resource
    private ITaskExtInfoSV iTaskExtInfoSV;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private IInstanceSV iInstanceSV;

    @Resource
    private IMailSV iMailSV;

    @Resource
    private ITimeTaskSV iTimeTaskSV;

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private IConfigItemSV iConfigItemSV;


    @Override
    public void pushOneConfig(PushConfigReqVO pushConfigReqVO, int userId) {
        //基本校验
        pushOneConfigCheckParam(pushConfigReqVO);
        //查看配置是否存在
        byte configType = pushConfigReqVO.getConfigType();
        int configId = pushConfigReqVO.getConfigId();
        int envId = pushConfigReqVO.getEnvId();
        String configName = null;
        Integer strategyId = 0;

        if(configType == ProjectConstants.CONFIG_TYPE_FILE){
            CcConfigFileEntity ccConfigFileEntity = iConfigFileSV.getConfigFileByIdAndEnvIdCheck(configId,envId);
            configName = ccConfigFileEntity.getFileName();
            strategyId = ccConfigFileEntity.getStrategyId();

        }else if(configType == ProjectConstants.CONFIG_TYPE_ITEM){
            CcConfigItemEntity ccConfigItemEntity = iConfigItemSV.getConfigItemByIdCheck(configId,envId);
            configName = ccConfigItemEntity.getItemKey();
            strategyId = ccConfigItemEntity.getStrategyId();
        }
        //校验配置是否有刷新策略
        Assert4CC.isTrue(strategyId!= null &&strategyId!=0,ResultCodeEnum.CONFIG_COMMON_ERROR,"配置没有关联更新策略,无法进行推送");

        iTaskSV.createTaskForConfigPush(envId,
                userId,
                configType,
                pushConfigReqVO.getConfigId(),
                configName,
                pushConfigReqVO.getConfigVersion(),
                null,
                null,
                null);
    }

    @Override
    public void pushTaskPass(int taskId, int envId) {
        CcTaskEntity ccTaskEntity = iTaskSV.getTaskById(taskId);
        CcTaskExtInfoEntity ccTaskExtInfoEntity = iTaskExtInfoSV.findTaskExtInfoByTaskIdAndKey(taskId,ProjectConstants.TASK_EXT_INFO_PUSH_TIME);
        if(ccTaskExtInfoEntity ==  null){
            dealPushTimeTask(taskId);
        }else{
            String pushTimeStr = ccTaskExtInfoEntity.getExtInfoValue();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(ProjectConstants.QUARTZ_PUSH_CONFIG_TASK_ID,taskId);
            try {
                iTimeTaskSV.createTask(Long.parseLong(pushTimeStr),ProjectConstants.QUARTZ_PUSH_CONFIG_GROUP_NAME,ccTaskEntity.getTaskName(),jobDataMap);
            }catch (Exception e){
                logger.info(ErrorInfo.errorInfo(e));
                throw new ErrorCodeException(ResultCodeEnum.TASK_COMMON_ERROR,"定时任务启动失败,taskId:"+taskId);
            }
        }
    }

    @Override
    public void dealPushTimeTask(int taskId) {
        ArrayList<String> notAliveInstances = new ArrayList<>();//推送的时候实例没有存活 就放在此容器中
        ZKConfigPushVO configPush = new ZKConfigPushVO();
        CcTaskEntity ccTaskEntity = iTaskSV.getTaskById(taskId);
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(ccTaskEntity.getAppEnvId());

        CcTaskExtInfoEntity instanceExtInfo = iTaskExtInfoSV.findTaskExtInfoByTaskIdAndKey(taskId,ProjectConstants.TASK_EXT_INFO_INSTANCES);
        int [] instanceIds = CcStringUtil.splitByCommaAndConvertToInt(instanceExtInfo.getExtInfoValue());

        List<CcTaskDetailPushEntity> detailList = iTaskSV.getTaskDetailPushsByTaskId(taskId);
        if(detailList != null && detailList.size() > 0){
            List<ZKConfigVO> files = new ArrayList<>();
            List<ZKConfigVO> items = new ArrayList<>();
            for(CcTaskDetailPushEntity taskDetailPushEntity: detailList){
                ZKConfigVO zkConfig =  new ZKConfigVO(taskDetailPushEntity.getConfigName(), taskDetailPushEntity.getConfigVersion());
                if (ProjectConstants.CONFIG_TYPE_FILE == taskDetailPushEntity.getConfigType()){
                    files.add(zkConfig);
                }else {
                    items.add(zkConfig);
                }
            }
            configPush.setFiles(files);
            configPush.setItems(items);
        }
        for(int instanceId : instanceIds){
            //查询实例
            CcInstanceEntity ccInstanceEntity = iInstanceSV.getInstanceByIdCheck(instanceId);
            if(ccInstanceEntity.getIsAlive() == ProjectConstants.STATUS_VALID){
                pushConfigToZookeeper(appInfoVO,ccInstanceEntity.getInsName(),configPush);
            }else{
                notAliveInstances.add(ccInstanceEntity.getInsName());
            }
        }
        //发送实例没有存活的邮件
        if(notAliveInstances.size() > 0){
            int taskCreator = ccTaskEntity.getCreator();
            iMailSV.sendMailByUserId(taskCreator,"配置文件推送给实例失败","失败实例列表:"+Arrays.toString(notAliveInstances.toArray()));
        }
    }

    @Override
    public void pushConfigToZookeeper(AppInfoVO appInfoVO, String insName, ZKConfigPushVO zkConfigPushVO) {
        try {
            ccServerZKManager.getInstanceNodeOper().writeInstanceNodeData(appInfoVO.getAppName(),appInfoVO.getEnvName(),insName, JSONUtil.obj2JsonStr(zkConfigPushVO));
        }catch ( KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    //*******************校验
    private void pushOneConfigCheckParam(PushConfigReqVO pushConfigReqVO){
        byte configType = pushConfigReqVO.getConfigType();
        Assert4CC.isTrue(configType != 0,"配置类型不能为空");
        Assert4CC.isTrue(configType == ProjectConstants.CONFIG_TYPE_FILE || configType == ProjectConstants.CONFIG_TYPE_ITEM,"配置类型不合法");
        Assert4CC.isTrue(pushConfigReqVO.getConfigId() != 0 ,"配置主键不能为空");
        Assert4CC.hasLength(pushConfigReqVO.getConfigVersion() , "配置文件版本不能为空");
    }
}
