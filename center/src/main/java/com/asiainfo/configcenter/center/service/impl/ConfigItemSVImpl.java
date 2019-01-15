package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.repository.ConfigItemHisRepository;
import com.asiainfo.configcenter.center.dao.repository.ConfigItemRepository;
import com.asiainfo.configcenter.center.dao.mapper.ConfigItemMapper;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.service.interfaces.IAppEnvSV;
import com.asiainfo.configcenter.center.service.interfaces.IConfigItemHisSV;
import com.asiainfo.configcenter.center.service.interfaces.IConfigItemSV;
import com.asiainfo.configcenter.center.service.interfaces.ITaskSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configItem.*;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.asiainfo.configcenter.zookeeper.cczk.ConfigItemNodeOper;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

/**
 * 配置项服务实现类
 * Created by oulc on 2018/7/25.
 */
@Service
public class ConfigItemSVImpl implements IConfigItemSV {

    @Resource
    private ConfigItemRepository configItemRepository;

    @Resource
    private ConfigItemHisRepository configItemHisRepository;

    @Resource
    private ConfigItemMapper configItemMapper;

    @Resource
    private IConfigItemHisSV configItemHisSV;

    @Resource
    private ITaskSV taskSV;

    @Resource
    private IAppEnvSV appEnvSV;

    @Resource
    private CCServerZKManager ccServerZKManager;

    private Logger logger = Logger.getLogger(ConfigItemSVImpl.class);

    @Override
    public List<CcConfigItemEntity> getConfigItemsByEnvId(int envId) {
        return configItemRepository.findByAppEnvIdAndStatus(envId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public PageResultContainer<ConfigItemVO> getConfigItemsByCondition(PageRequestContainer<ConfigItemQueryReqVO> requestContainer) {
        ValidateUtil.checkPageParam(requestContainer);
        ConfigItemQueryReqVO configItemQueryReqVO = requestContainer.getData();
        int envId = configItemQueryReqVO.getEnvId();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        String creatorName = configItemQueryReqVO.getCreatorName();
        String itemKey = configItemQueryReqVO.getItemKey();
        Timestamp beginTime = configItemQueryReqVO.getBeginTime()==0?null:new Timestamp(configItemQueryReqVO.getBeginTime());
        Timestamp endTime = configItemQueryReqVO.getEndTime()==0?null:new Timestamp(configItemQueryReqVO.getEndTime());
        List<ConfigItemVO> configItems = configItemMapper.getConfigItems(envId, itemKey, creatorName, beginTime, endTime, requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = configItemMapper.getConfigItemsCount(envId, itemKey, creatorName, beginTime, endTime);
        return new PageResultContainer<>(configItems, count);
    }

    @Override
    @Transactional
    public String addConfigItem(ConfigItemReqVO configItem, int creator) {
        int envId = configItem.getEnvId();
        String itemKey = configItem.getItemKey();
        String itemValue = configItem.getItemValue();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        Assert4CC.hasLength(itemKey, "配置项项关键字不可为空");
        Assert4CC.hasLength(itemValue, "配置项值不可为空");
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_ITEM_KEY, itemKey), "配置项关键字不符合规则");
        CcConfigItemEntity configItemEntity = configItemRepository.findByAppEnvIdAndItemKeyAndStatus(envId, itemKey, ProjectConstants.STATUS_VALID);
        Assert4CC.isNull(configItemEntity, ResultCodeEnum.CONFIG_ITEM_KEY_EXIST_ERROR, itemKey);
        //新增配置项加入待提交任务
        taskSV.createTaskForConfigItem(envId, creator, ProjectConstants.CONFIG_CHANGE_TYPE_ADD, null, configItem.getStrategyId(), itemKey, itemValue, configItem.getDescription());
        return "新增配置项已加入待提交任务列表";
    }

    @Override
    @Transactional
    public void saveOrUpdateItem(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity) {
        if (taskDetailConfigEntity.getConfigType() != ProjectConstants.CONFIG_TYPE_ITEM){
            throw new ErrorCodeException(ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "任务（ID:"+taskId+"）的任务详情（ID:"+taskDetailConfigEntity.getId()+"）非配置项变更");
        }
        AppInfoVO appInfo = appEnvSV.getAppInfoByEnvId(envId);
        Assert4CC.notNull(appInfo, ResultCodeEnum.APP_ENV_NOT_EXIST_ERROR, "（" + envId + "）");
        byte changeType = taskDetailConfigEntity.getChangeType();
        CcConfigItemEntity configItemEntity;
        Timestamp currentTime = TimeUtil.currentTime();
        if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD){
            //新增配置项
            configItemEntity = new CcConfigItemEntity();
            configItemEntity.setAppEnvId(envId);
            configItemEntity.setItemKey(taskDetailConfigEntity.getConfigName());
            configItemEntity.setItemValue(taskDetailConfigEntity.getConfigContent());
            configItemEntity.setStrategyId(taskDetailConfigEntity.getStrategyId());
            configItemEntity.setDescription(taskDetailConfigEntity.getConfigDesc());
            configItemEntity.setCreator(taskDetailConfigEntity.getModifier());
            configItemEntity.setCreateTime(currentTime);
            configItemEntity.setModifier(taskDetailConfigEntity.getModifier());
            configItemEntity.setUpdateTime(currentTime);
            configItemEntity.setStatus(ProjectConstants.STATUS_VALID);
        }else {
            configItemEntity = configItemRepository.findByAppEnvIdAndIdAndStatus(envId, taskDetailConfigEntity.getConfigId(), ProjectConstants.STATUS_VALID);
            if (configItemEntity==null){
                throw new ErrorCodeException(ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "环境（ID:"+envId+"）中的配置（ID:"+taskDetailConfigEntity.getConfigId()+"）不存在或已经失效，无法更新");
            }else{
                //更新配置项数据
                if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
                    configItemEntity.setItemKey(taskDetailConfigEntity.getConfigName());
                    configItemEntity.setItemValue(taskDetailConfigEntity.getConfigContent());
                    configItemEntity.setDescription(taskDetailConfigEntity.getConfigDesc());
                }else if(changeType == ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE) {
                    configItemEntity.setStrategyId(taskDetailConfigEntity.getStrategyId());
                }else{
                    configItemEntity.setStatus(ProjectConstants.STATUS_INVALID);
                }
                configItemEntity.setModifier(taskDetailConfigEntity.getModifier());
                configItemEntity.setUpdateTime(currentTime);
            }
        }
        //更新数据到配置项表
        configItemRepository.saveAndFlush(configItemEntity);
        //保存配置项数据到历史数据表
        if (changeType != ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE) {
            CcConfigItemHisEntity configItemHis = configItemHisSV.transferConfigItem(configItemEntity);
            //处理节点数据，放在最后，保证zk节点处理出问题后数据库可以回退
            dealConfigItemNode(changeType, envId, appInfo.getAppName(), appInfo.getEnvName(), taskDetailConfigEntity.getConfigName(), configItemHis.getId()+"");
        }
    }

    /**
     * 操作配置项节点
     * @author bawy
     * @date 2018/8/7 17:36
     * @param type 操作类型
     * @param envId 环境标识
     * @param appName 应用名称
     * @param envName 环境名称
     * @param itemName 配置项名称
     * @param nodeValue 节点数据
     */
    private void dealConfigItemNode(byte type, int envId, String appName, String envName, String itemName, String nodeValue){
        switch (type){
            case ProjectConstants.CONFIG_CHANGE_TYPE_ADD:
                createConfigItemNode(envId, appName, envName, itemName, nodeValue);
                break;
            case ProjectConstants.CONFIG_CHANGE_TYPE_MOD:
                updateConfigItemNode(envId, appName, envName, itemName, nodeValue);
                break;
            case  ProjectConstants.CONFIG_CHANGE_TYPE_DEL:
                deleteConfigItemNode(appName, envName, itemName);
                break;
        }
    }

    /**
     * 创建配置项节点
     * @author bawy
     * @date 2018/8/7 17:19
     * @param envId 环境标识
     * @param appName 应用名称
     * @param envName 环境名称
     * @param itemName 配置项名称
     * @param nodeValue 节点数据
     */
    private void createConfigItemNode(int envId, String appName, String envName, String itemName, String nodeValue){
        try {
            ConfigItemNodeOper itemNodeOper = ccServerZKManager.getConfigItemNodeOper();
            if(itemNodeOper.configItemNodeExist(appName, envName, itemName)){
                throw new ErrorCodeException(ResultCodeEnum.ZK_COMMON_ERROR,"环境(ID："+envId+")中配置项名称为“"+itemName+"”的节点已经存在，无法创建，请联系管理员");
            }
            itemNodeOper.createConfigItemNode(appName, envName, itemName, nodeValue);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    /**
     * 删除配置项节点
     * @author bawy
     * @date 2018/8/7 17:28
     * @param appName 应用名称
     * @param envName 环境名称
     * @param itemName 配置项名称
     */
    private void deleteConfigItemNode(String appName, String envName, String itemName){
        try {
            ConfigItemNodeOper itemNodeOper = ccServerZKManager.getConfigItemNodeOper();
            itemNodeOper.deleteConfigItemNode(appName, envName, itemName);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    /**
     * 更新配置项节点
     * @author bawy
     * @date 2018/8/7 17:32
     * @param envId 环境标识
     * @param appName 应用名称
     * @param envName 环境名称
     * @param itemName 配置项名称
     * @param nodeValue 节点数据
     */
    private void updateConfigItemNode(int envId, String appName, String envName, String itemName, String nodeValue){
        try {
            ConfigItemNodeOper itemNodeOper = ccServerZKManager.getConfigItemNodeOper();
            if(!itemNodeOper.configItemNodeExist(appName, envName, itemName)){
                throw new ErrorCodeException(ResultCodeEnum.ZK_COMMON_ERROR,"环境(ID："+envId+")中配置项名称为“"+itemName+"”的节点已经不存在,无法更新，请联系管理员");
            }
            itemNodeOper.writeConfigItemNodeData(appName, envName, itemName, nodeValue);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    @Override
    @Transactional
    public String modConfigItem(ConfigItemReqVO configItem, int modifier) {
        int envId = configItem.getEnvId();
        String itemKey = configItem.getItemKey();
        String itemValue = configItem.getItemValue();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        Assert4CC.hasLength(itemKey, "配置项项关键字不可为空");
        CcConfigItemEntity configItemEntity = configItemRepository.findByAppEnvIdAndItemKeyAndStatus(envId, itemKey, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(configItemEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "此环境中不存在配置项名称为“"+itemKey+"”的配置项");
        //修改配置项加入待提交任务
        if (ProjectConstants.CONFIG_MOD_OPTION.equals(configItem.getModOption())){
            Integer strategyId = configItem.getStrategyId();
            Assert4CC.isTrue(strategyId!=null&&strategyId>0, "配置刷新策略不可为空");
            taskSV.createTaskForConfigItem(envId, modifier, ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE, configItemEntity.getId(), strategyId, itemKey, null, null);
        }else{
            Assert4CC.hasLength(itemValue, "配置项值不可为空");
            taskSV.createTaskForConfigItem(envId, modifier, ProjectConstants.CONFIG_CHANGE_TYPE_MOD, configItemEntity.getId(), null, itemKey, itemValue, configItem.getDescription());
        }
        return "修改配置项已加入待提交任务列表";
    }

    @Override
    @Transactional
    public String copyConfigItem(ConfigItemCopyReqVO configItemCopyReq, int creator) {
        int envId = configItemCopyReq.getEnvId();
        int sourceEnvId = configItemCopyReq.getSourceEnvId();
        List<Integer> configIds = configItemCopyReq.getConfigItemIds();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        Assert4CC.isTrue(sourceEnvId>0, "源环境标识不可为空");
        Assert4CC.isTrue(configIds!=null&&configIds.size()>0, "配置项标识列表不可为空");
        CcAppEnvEntity targetEnv = appEnvSV.getEnvByEnvId(envId);
        CcAppEnvEntity sourceEnv = appEnvSV.getEnvByEnvId(sourceEnvId);
        Assert4CC.isTrue(targetEnv.getAppId()==sourceEnv.getAppId(), ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "被复制环境与当前环境不属于同一应用，无法复制");
        List<CcConfigItemEntity> configItemEntities = configItemRepository.findByAppEnvIdAndIdInAndStatus(sourceEnvId, configIds, ProjectConstants.STATUS_VALID);
        for (CcConfigItemEntity configItemEntity: configItemEntities) {
            String itemKey = configItemEntity.getItemKey();
            CcConfigItemEntity existConfigItem = configItemRepository.findByAppEnvIdAndItemKeyAndStatus(envId, itemKey, ProjectConstants.STATUS_VALID);
            Assert4CC.isNull(existConfigItem, ResultCodeEnum.CONFIG_ITEM_KEY_EXIST_ERROR, itemKey);
            //修改配置项加入待提交任务
            taskSV.createTaskForConfigItem(envId, creator, ProjectConstants.CONFIG_CHANGE_TYPE_ADD, configItemEntity.getId(), configItemEntity.getStrategyId(), itemKey, configItemEntity.getItemValue(), configItemEntity.getDescription());
        }
        return "复制所选择配置项已加入待提交任务列表";
    }

    @Override
    @Transactional
    public String delConfigItem(ConfigItemReqVO configItemReq, int modifier) {
        int envId = configItemReq.getEnvId();
        String itemKey = configItemReq.getItemKey();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        Assert4CC.hasLength(itemKey, "配置项项关键字不可为空");
        CcConfigItemEntity configItemEntity = configItemRepository.findByAppEnvIdAndItemKeyAndStatus(envId, itemKey, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(configItemEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "此环境中不存在配置项名称为“"+itemKey+"”的配置项");
        //删除配置项加入待提交任务列表
        taskSV.createTaskForConfigItem(envId, modifier, ProjectConstants.CONFIG_CHANGE_TYPE_DEL, configItemEntity.getId(), null, itemKey, null, null);
        return "删除配置项已加入待提交任务列表";
    }

    @Override
    public List<ConfigItemHisVO> getConfigItemHis(ConfigItemHisReqVO configItemHisReq) {
        int envId = configItemHisReq.getEnvId();
        int configItemId = configItemHisReq.getConfigItemId();
        Assert4CC.isTrue(configItemId>0, "配置项标识不可为空");
        CcConfigItemEntity configItemEntity = configItemRepository.findByAppEnvIdAndIdAndStatus(envId, configItemId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(configItemEntity, "环境（ID："+envId+"）中不存在配置项（ID"+configItemId+"）");
        return configItemMapper.getConfigItemHis(configItemId, configItemHisReq.getCreatorName(), ProjectConstants.CONFIG_HIS_DATA_SIZE);
    }

    @Override
    public ConfigItemHisVO getVersionContent(ConfigItemHisReqVO configItemHisReq) {
        int envId = configItemHisReq.getEnvId();
        int configItemId = configItemHisReq.getConfigItemId();
        Assert4CC.isTrue(configItemId>0, "配置项标识不可为空");
        int configItemHisId = configItemHisReq.getConfigItemHisId();
        if (configItemHisId>0){
            CcConfigItemHisEntity configItemHisEntity = configItemHisRepository.findByEnvIdAndHisId(envId, configItemHisId, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(configItemHisEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取配置项历史版本"+configItemHisId+"的数据");
            ConfigItemHisVO configItemHis = new ConfigItemHisVO();
            configItemHis.setCreateTime(configItemHisEntity.getCreateTime());
            configItemHis.setItemKey(configItemHisEntity.getItemKey());
            configItemHis.setItemValue(configItemHisEntity.getItemValue());
            configItemHis.setDescription(configItemHisEntity.getDescription());
            return configItemHis;
        }else {
            CcConfigItemEntity configItemEntity = configItemRepository.findByAppEnvIdAndIdAndStatus(envId, configItemId, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(configItemEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取配置项"+configItemId+"最新版本数据");
            ConfigItemHisVO configItemHis = new ConfigItemHisVO();
            configItemHis.setCreateTime(configItemEntity.getUpdateTime());
            configItemHis.setItemKey(configItemEntity.getItemKey());
            configItemHis.setItemValue(configItemEntity.getItemValue());
            configItemHis.setStrategyId(configItemEntity.getStrategyId());
            configItemHis.setDescription(configItemEntity.getDescription());
            return configItemHis;
        }
    }

    @Override
    public CcConfigItemEntity getConfigItemById(int id, int envId) {
        return configItemRepository.findByAppEnvIdAndIdAndStatus(envId,id,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcConfigItemEntity getConfigItemByIdCheck(int id, int envId) {
        CcConfigItemEntity ccConfigItemEntity = getConfigItemById(id, envId);
        Assert4CC.notNull(ccConfigItemEntity,ResultCodeEnum.CONFIG_COMMON_ERROR,"配置项不存在：id:"+id+",envId:"+envId);
        return ccConfigItemEntity;
    }

    @Override
    public List<CcConfigItemEntity> getConfigItemByStrategyId(int strategyId) {
        return configItemRepository.findByStrategyIdAndStatus(strategyId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public String[] getConfigItemNamesByStrategyId(int strategyId) {
        List<CcConfigItemEntity> ccConfigItemEntities = getConfigItemByStrategyId(strategyId);
        if (ccConfigItemEntities != null && ccConfigItemEntities.size() > 0) {
            String[] configItemNames = new String[ccConfigItemEntities.size()];
            for (int i = 0; i < configItemNames.length; i++) {
                configItemNames[i] = ccConfigItemEntities.get(i).getItemKey();
            }
        }
        return null;
    }
    @Override
    public CcConfigItemHisEntity getConfigItemHisVersionByIdCheck(int envId, int id) {
        CcConfigItemHisEntity configItemHisEntity = configItemHisRepository.findByEnvIdAndHisId(envId, id, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(configItemHisEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取配置项历史版本"+id+"的数据");
        return configItemHisEntity;
    }
}
