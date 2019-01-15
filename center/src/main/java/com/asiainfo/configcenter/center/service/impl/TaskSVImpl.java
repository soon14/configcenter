package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.InstanceMapper;
import com.asiainfo.configcenter.center.dao.mapper.TaskMapper;
import com.asiainfo.configcenter.center.dao.repository.*;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcStringUtil;
import com.asiainfo.configcenter.center.util.NotificationUtil;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.audit.AuditStrategyStepVO;
import com.asiainfo.configcenter.center.vo.org.OrgUserVO;
import com.asiainfo.configcenter.center.vo.strategy.ConfigUpdateStrategyVO;
import com.asiainfo.configcenter.center.vo.task.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;

/**
 * 任务服务实现类
 * Created by bawy on 2018/8/1 16:22.
 */
@Service
public class TaskSVImpl implements ITaskSV {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskExtInfoRepository taskExtInfoRepository;

    @Resource
    private TaskDetailConfigRepository taskDetailConfigRepository;

    @Resource
    private TaskDetailPushRepository taskDetailPushRepository;

    @Resource
    private TaskOperRecordRepository taskOperRecordRepository;

    @Resource
    private TaskOperRecordDetailRepository taskOperRecordDetailRepository;

    @Resource
    private InstanceRepository instanceRepository;

    @Resource
    private ITaskAuditStrategySV taskAuditStrategySV;

    @Resource
    private INotificationSV notificationSV;

    @Resource
    private IConfigFileSV configFileSV;

    @Resource
    private IConfigItemSV configItemSV;

    @Resource
    private IConfigPushSV configPushSV;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private InstanceMapper instanceMapper;

    @Resource
    private IInstanceSV instanceSV;

    @Resource
    private IOrgSV orgSV;

    @Resource
    private IAppEnvSV appEnvSV;

    @Resource
    private IConfigUpdateStrategySV configUpdateStrategySV;

    @Resource
    private IUserSV iUserSV;

    @Override
    @Transactional
    public void createTaskForConfigItem(int envId, int creator, byte changeType, Integer confId, Integer strategyId, String itemKey, String itemValue, String description) {
        createTaskForConfig(envId, creator, changeType, ProjectConstants.CONFIG_TYPE_ITEM, confId, strategyId, itemKey, itemValue, description);
    }

    @Override
    @Transactional
    public TaskInfo createTaskForConfigFile(int envId, int creator, byte changeType, Integer confId, Integer strategyId, String fileName, String commitId, String description) {
        return createTaskForConfig(envId, creator, changeType, ProjectConstants.CONFIG_TYPE_FILE, confId, strategyId, fileName, commitId, description);
    }

    @Override
    @Transactional
    public void createTaskForConfigPush(int envId, int creator, byte configType, int configId, String configName, String version, String instances, String cronTime, String description) {
        //校验配置推送是否已经存在于正在审核的任务中
        checkConfigPushExist(envId);
        CcTaskEntity taskEntity = taskRepository.findByAppEnvIdAndCreatorAndTaskTypeAndTaskStateAndStatus(envId, creator, ProjectConstants.TASK_TYPE_CONFIG_PUSH, ProjectConstants.TASK_STATE_TO_SUBMIT, ProjectConstants.STATUS_VALID);
        Timestamp currentTime = TimeUtil.currentTime();
        if (taskEntity == null){
            //用户不存在待提交任务则创建新的待提交任务
            taskEntity = createToSubmitTaskEntity(envId, ProjectConstants.TASK_TYPE_CONFIG_PUSH, creator, currentTime);
        }else {
            //判断配置的推送是否已经存在
            CcTaskDetailPushEntity taskDetailPushEntity = taskDetailPushRepository.findByTaskIdAndConfigIdAndStatus(taskEntity.getId(), configId, ProjectConstants.STATUS_VALID);
            Assert4CC.isNull(taskDetailPushEntity, ResultCodeEnum.TASK_CONFIG_PUSH_HAS_THIS_ERROR, "配置名称：" + configName + "，请勿重复添加");
            taskEntity.setModifier(creator);
            taskEntity.setUpdateTime(currentTime);
        }
        //保存任务
        taskRepository.saveAndFlush(taskEntity);
        //新增配置变更详情数据
        CcTaskDetailPushEntity taskDetailPushEntity = new CcTaskDetailPushEntity();
        taskDetailPushEntity.setTaskId(taskEntity.getId());
        taskDetailPushEntity.setConfigId(configId);
        taskDetailPushEntity.setConfigType(configType);
        taskDetailPushEntity.setConfigName(configName);
        taskDetailPushEntity.setConfigVersion(version);
        taskDetailPushEntity.setCronTime(cronTime);
        taskDetailPushEntity.setDescription(description);
        taskDetailPushEntity.setCreator(creator);
        taskDetailPushEntity.setCreateTime(currentTime);
        taskDetailPushEntity.setModifier(creator);
        taskDetailPushEntity.setUpdateTime(currentTime);
        taskDetailPushEntity.setStatus(ProjectConstants.STATUS_VALID);
        taskDetailPushRepository.saveAndFlush(taskDetailPushEntity);
    }

    @Transactional
    public TaskInfo createTaskForConfig(int envId, int creator, byte changeType, byte configType, Integer confId, Integer strategyId, String configName, String configContent, String description){
        //校验配置变更是否已经存在于正在审核的任务中
        checkConfigChangeExist(envId, configName, configType);
        TaskInfo taskInfo = new TaskInfo();
        CcTaskEntity taskEntity = taskRepository.findByAppEnvIdAndCreatorAndTaskTypeAndTaskStateAndStatus(envId, creator, ProjectConstants.TASK_TYPE_CONFIG_CHANGE, ProjectConstants.TASK_STATE_TO_SUBMIT, ProjectConstants.STATUS_VALID);
        Timestamp currentTime = TimeUtil.currentTime();
        if (taskEntity == null){
            //用户不存在待提交任务则创建新的待提交任务
            taskEntity = createToSubmitTaskEntity(envId, ProjectConstants.TASK_TYPE_CONFIG_CHANGE, creator, currentTime);
        }else {
            if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD){
                //新增需要判断同名配置项或者配置文件是否存在
                CcTaskDetailConfigEntity taskDetailConfigEntity = taskDetailConfigRepository.findByTaskIdAndConfigTypeAndConfigNameAndStatus(taskEntity.getId(), configType, configName, ProjectConstants.STATUS_VALID);
                Assert4CC.isNull(taskDetailConfigEntity, ResultCodeEnum.TASK_CONFIG_CHANGE_EXIST, "配置名称：" + configName + "，请勿重复新增");
            }else {
                //用户存在待提交任务且配置变更类型不是新增，则需要判断待提交任务中是否已经存在该配置的变更
                CcTaskDetailConfigEntity taskDetailConfigEntity = taskDetailConfigRepository.findByTaskIdAndConfigIdAndStatus(taskEntity.getId(), confId, ProjectConstants.STATUS_VALID);
                Assert4CC.isNull(taskDetailConfigEntity, ResultCodeEnum.TASK_CONFIG_CHANGE_EXIST, "配置名称：" + configName + "，请勿重复修改或删除");
            }
            taskEntity.setModifier(creator);
            taskEntity.setUpdateTime(currentTime);
        }
        //保存任务
        taskRepository.save(taskEntity);
        taskInfo.setTaskId(taskEntity.getId());
        //新增配置变更详情数据
        CcTaskDetailConfigEntity taskDetailConfigEntity = new CcTaskDetailConfigEntity();
        taskDetailConfigEntity.setTaskId(taskEntity.getId());
        taskDetailConfigEntity.setChangeType(changeType);
        taskDetailConfigEntity.setConfigType(configType);
        taskDetailConfigEntity.setConfigId(confId);
        taskDetailConfigEntity.setConfigName(configName);
        if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
            taskDetailConfigEntity.setConfigContent(configContent);
            taskDetailConfigEntity.setConfigDesc(description);
        }
        if(changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE){
            taskDetailConfigEntity.setStrategyId(strategyId);
        }
        taskDetailConfigEntity.setCreator(creator);
        taskDetailConfigEntity.setCreateTime(currentTime);
        taskDetailConfigEntity.setModifier(creator);
        taskDetailConfigEntity.setUpdateTime(currentTime);
        taskDetailConfigEntity.setStatus(ProjectConstants.STATUS_VALID);
        taskDetailConfigRepository.saveAndFlush(taskDetailConfigEntity);
        taskInfo.setDetailId(taskDetailConfigEntity.getId());
        return taskInfo;
    }

    /**
     * 生成待提交任务实体
     * @author bawy
     * @date 2018/8/14 14:55
     * @param envId 环境标识
     * @param taskType 任务类型
     * @param creator 创建人
     * @param currentTime 当前时间
     * @return 实体对象
     */
    private CcTaskEntity createToSubmitTaskEntity(int envId, byte taskType, int creator, Timestamp currentTime){
        CcTaskEntity taskEntity = new CcTaskEntity();
        taskEntity.setAppEnvId(envId);
        taskEntity.setTaskName("用户“"+creator+"”的待提交任务");
        taskEntity.setTaskType(taskType);
        taskEntity.setTaskState(ProjectConstants.TASK_STATE_TO_SUBMIT);
        taskEntity.setCreator(creator);
        taskEntity.setCreateTime(currentTime);
        taskEntity.setModifier(creator);
        taskEntity.setUpdateTime(currentTime);
        taskEntity.setStatus(ProjectConstants.STATUS_VALID);
        return taskEntity;
    }

    @Override
    @Transactional
    public String submitTask(SubmitTaskReqVO submitTaskReq, int submitter) {
        int taskId = submitTaskReq.getTaskId();
        String taskName = submitTaskReq.getTaskName();
        Assert4CC.isTrue(taskId>0, "任务标识不可为空");
        Assert4CC.hasLength(taskName, "任务名称不可为空");
        CcTaskEntity taskEntity = taskRepository.findByIdAndCreatorAndStatus(taskId, submitter, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "任务（"+taskId+"）不存在，或者非当前登录用户创建");
        Assert4CC.isTrue(taskEntity.getTaskState() == ProjectConstants.TASK_STATE_TO_SUBMIT, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "任务状态不是待提交状态");
        Map<String,String> taskExtInfo = checkTaskExtInfo(taskEntity.getAppEnvId(),submitTaskReq.getExtInfo(),taskEntity.getTaskType());
        String description = submitTaskReq.getDescription();
        //验证任务详情中所有变更当前是否存在待审核的数据
        checkTaskDetail(taskEntity);
        //添加任务扩展信息
        addTaskExtInfo(taskId, submitter, taskExtInfo);
        //根据审核策略生成任务记录
        addTaskOperRecord(taskId, taskEntity.getAppEnvId(), submitter);
        //更新任务
        Timestamp currentTime = TimeUtil.currentTime();
        taskEntity.setTaskName(taskName);
        taskEntity.setDescription(description);
        taskEntity.setCreator(submitter);
        taskEntity.setCreateTime(currentTime);
        taskEntity.setModifier(submitter);
        taskEntity.setUpdateTime(currentTime);
        taskRepository.saveAndFlush(taskEntity);
        //处理任务流程
        return dealTask(taskEntity, ProjectConstants.OPERATE_STATE_SUBMIT, submitter, submitTaskReq.getNextOperator(), ProjectConstants.OPERATE_TYPE_SUBMIT, "任务提交成功", null);
    }

    /**
     * 校验任务扩展信息
     * @author bawy
     * @date 2018/8/24 13:46
     * @param envId 环境标识
     * @param extInfo 扩展信息集合
     * @param taskType 任务类型
     * @return 处理后的扩展信息集合
     */
    private Map<String, String> checkTaskExtInfo(int envId, Map<String,String> extInfo,byte taskType){
        Map<String, String> taskExtInfo = new HashMap<>();
        switch (taskType){
            case ProjectConstants.TASK_TYPE_CONFIG_CHANGE:
                break;
            case ProjectConstants.TASK_TYPE_CONFIG_PUSH:
                String selectType = extInfo.get(ProjectConstants.TASK_INFO_KEY_SELECTED_TYPE);
                String pushTime = extInfo.get(ProjectConstants.TASK_INFO_KEY_PUSH_TIME);
                Assert4CC.hasLength(selectType, "实例选择类型不能为空");
                switch (selectType) {
                    case ProjectConstants.INSTANCE_SELECTED_TYPE_NORMAL:
                        //普通选择
                        String selectedItem = extInfo.get(ProjectConstants.TASK_INFO_KEY_SELECTED_ITEM);
                        Assert4CC.hasLength(selectedItem, "已选择实例不能为空");
                        taskExtInfo.put(ProjectConstants.TASK_EXT_INFO_INSTANCES, selectedItem);
                        break;
                    case ProjectConstants.INSTANCE_SELECTED_TYPE_ALL:
                        //全选
                        String cancelItem = extInfo.getOrDefault(ProjectConstants.TASK_INFO_KEY_CANCEL_ITEM, "");
                        String insName = extInfo.getOrDefault(ProjectConstants.TASK_INFO_KEY_INS_NAME,"");
                        String insIp = extInfo.getOrDefault(ProjectConstants.TASK_INFO_KEY_INS_IP,"");
                        int[] notInIds = CcStringUtil.splitByCommaAndConvertToInt(cancelItem);
                        List<CcInstanceEntity> pushInstanceList = instanceSV.getInstancesByNotIn(envId,notInIds,insName,insIp);
                        Assert4CC.isTrue(pushInstanceList != null && pushInstanceList.size() > 0,ResultCodeEnum.INSTANCE_COMMON_ERROR,"没有存活的实例");
                        int pushIds[] = new int[pushInstanceList.size()];
                        for( int i = 0; i < pushIds.length ; i++){
                            pushIds[i] = pushInstanceList.get(i).getId();
                        }
                        taskExtInfo.put(ProjectConstants.TASK_EXT_INFO_INSTANCES, CcStringUtil.convertIntArrayToString(pushIds));
                        break;
                    default:
                        throw new ErrorCodeException(ResultCodeEnum.TASK_COMMON_ERROR, "错误的实例选择类型");
                }
                if (pushTime != null){
                    taskExtInfo.put(ProjectConstants.TASK_EXT_INFO_PUSH_TIME, pushTime);
                }
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.TASK_TYPE_ILLEGAL);
        }
        return taskExtInfo;
    }


    /**
     * 检查任务详情
     * 1.检查任务下是否存在任务详情数据
     * 2.检查任务详情中的变更是否已经存在于正在审核的任务中
     *   例如。已有待审核的任务中删除了某个配置文件，则本次任务提交中所有变更不应该再包含此配置文件
     * @author bawy
     * @date 2018/8/8 11:02
     * @param taskEntity 任务实体
     */
    private void checkTaskDetail(CcTaskEntity taskEntity) {
        switch (taskEntity.getTaskType()){
            case ProjectConstants.TASK_TYPE_CONFIG_CHANGE:
                //配置文件变更
                List<CcTaskDetailConfigEntity> taskDetailConfigs = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskEntity.getId(), ProjectConstants.STATUS_VALID);
                Assert4CC.notNull(taskDetailConfigs, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "任务为空，不允许提交");
                for (CcTaskDetailConfigEntity taskDetailConfig : taskDetailConfigs) {
                    checkConfigChangeExist(taskEntity.getAppEnvId(), taskDetailConfig.getConfigName(), taskDetailConfig.getConfigType());
                }
                break;
            case ProjectConstants.TASK_TYPE_CONFIG_PUSH:
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.TASK_TYPE_ILLEGAL);
        }
    }

    /**
     * 检查任务详情中的变更是否已经存在于正在审核的任务中
     * @author bawy
     * @date 2018/8/8 18:07
     * @param envId 环境标识
     * @param configName 配置名称
     * @param configType 配置类型
     */
    private void checkConfigChangeExist(int envId, String configName, byte configType){
        CcTaskDetailConfigEntity inReviewTaskDetail = taskDetailConfigRepository.findByEnvIdAndConfigNameAndConfigType(envId, configName, configType, ProjectConstants.TASK_STATE_IN_REVIEW, ProjectConstants.STATUS_VALID);
        if (inReviewTaskDetail != null){
            configName = configType == ProjectConstants.CONFIG_TYPE_FILE?"配置文件":"配置项" + configName;
            throw new ErrorCodeException(ResultCodeEnum.TASK_CONFIG_CHANGE_REPEAT_ERROR, configName, inReviewTaskDetail.getTaskId());
        }
    }

    /**
     * 校验当前环境是否存在正在审核中的配置推送任务，如果已经存在则不允许提交新的配置推送任务
     * @author bawy
     * @date 2018/8/14 15:00
     * @param envId 环境标识
     */
    private void checkConfigPushExist(int envId){
        CcTaskEntity inReviewTask = taskRepository.findByAppEnvIdAndTaskTypeAndTaskStateAndStatus(envId, ProjectConstants.TASK_TYPE_CONFIG_PUSH, ProjectConstants.TASK_STATE_IN_REVIEW, ProjectConstants.STATUS_VALID);
        if (inReviewTask != null){
            throw new ErrorCodeException(ResultCodeEnum.TASK_CONFIG_PUSH_EXIST_ERROR);
        }
    }

    /**
     * 保存任务扩展信息
     * @author bawy
     * @date 2018/8/1 20:36
     * @param taskId 任务标识
     * @param creator 创建人
     * @param extInfo 扩展信息集合
     */
    @Transactional
    public void addTaskExtInfo(int taskId, int creator, Map<String, String> extInfo){
        if (extInfo!=null){
            Timestamp currentTime = TimeUtil.currentTime();
            List<CcTaskExtInfoEntity> taskExtInfoEntities = new ArrayList<>();
            for (Map.Entry<String, String> entry:extInfo.entrySet()){
                String value = entry.getValue();
                if (value!=null&&value.length()>0){
                    CcTaskExtInfoEntity taskExtInfoEntity = new CcTaskExtInfoEntity();
                    taskExtInfoEntity.setTaskId(taskId);
                    taskExtInfoEntity.setExtInfoKey(entry.getKey());
                    taskExtInfoEntity.setExtInfoValue(entry.getValue());
                    taskExtInfoEntity.setCreator(creator);
                    taskExtInfoEntity.setCreateTime(currentTime);
                    taskExtInfoEntity.setModifier(creator);
                    taskExtInfoEntity.setUpdateTime(currentTime);
                    taskExtInfoEntity.setStatus(ProjectConstants.STATUS_VALID);
                    taskExtInfoEntities.add(taskExtInfoEntity);
                }
            }
            taskExtInfoRepository.save(taskExtInfoEntities);
        }
    }

    /**
     * 生成任务操作记录
     * @author bawy
     * @date 2018/8/1 20:59
     * @param taskId 任务标识
     * @param envId 环境标识
     * @param operator 操作人员
     */
    @Transactional
    public void addTaskOperRecord(int taskId, int envId, int operator){
        List<CcTaskOperRecordEntity> taskOperRecordEntities = new ArrayList<>();
        Timestamp currentTime = TimeUtil.currentTime();
        List<AuditStrategyStepVO> auditStrategySteps = taskAuditStrategySV.getAuditStrategy(envId);
        if (auditStrategySteps==null||auditStrategySteps.size()==0){
            throw new ErrorCodeException(ResultCodeEnum.TASK_ENV_NO_STRATEGY_ERROR);
        }else {
            for (int i=0; i<=auditStrategySteps.size();i++){
                CcTaskOperRecordEntity taskOperRecordEntity = new CcTaskOperRecordEntity();
                taskOperRecordEntity.setTaskId(taskId);
                if (i==0){
                    taskOperRecordEntity.setOperationState(ProjectConstants.OPERATE_STATE_SUBMIT);
                    taskOperRecordEntity.setStep((byte)1);
                    taskOperRecordEntity.setOperator(operator);
                }else{
                    taskOperRecordEntity.setOperationState(ProjectConstants.OPERATE_STATE_TO_AUDIT);
                    taskOperRecordEntity.setStep((byte)(i+1));
                    taskOperRecordEntity.setOperator(0);
                }
                taskOperRecordEntity.setCreateTime(currentTime);
                taskOperRecordEntity.setUpdateTime(currentTime);
                taskOperRecordEntity.setStatus(ProjectConstants.STATUS_VALID);
                taskOperRecordEntities.add(taskOperRecordEntity);
            }
        }
        taskOperRecordRepository.save(taskOperRecordEntities);
    }
    /*public void addTaskOperRecord(int taskId, int envId, int operator, byte step, List<Integer> operators){
        //校验统一操作员在审核链中是否重复出现
        if (operators.contains(operator)){
            throw new ErrorCodeException(ResultCodeEnum.TASK_AUDIT_STRATEGY_OPERATOR_REPEAT_ERROR, operator);
        }
        operators.add(operator);
        CcTaskOperRecordEntity taskOperRecordEntity = new CcTaskOperRecordEntity();
        Timestamp currentTime = TimeUtil.currentTime();
        if (step == 1){
            //如果是第一步，即为提交任务的操作记录
            taskOperRecordEntity.setOperationState(ProjectConstants.OPERATE_STATE_SUBMIT);
        }else {
            //如果非第一步，则为任务审核的操作记录
            taskOperRecordEntity.setOperationState(ProjectConstants.OPERATE_STATE_TO_AUDIT);
        }
        taskOperRecordEntity.setTaskId(taskId);
        taskOperRecordEntity.setStep(step);
        taskOperRecordEntity.setOperator(operator);
        taskOperRecordEntity.setCreateTime(currentTime);
        taskOperRecordEntity.setUpdateTime(currentTime);
        taskOperRecordEntity.setStatus(ProjectConstants.STATUS_VALID);
        taskOperRecordRepository.saveAndFlush(taskOperRecordEntity);
        //判断是否有上级审核人员
        int superOperator = taskAuditStrategySV.getSuperOperator(operator, envId);
        if (superOperator!=0){
            addTaskOperRecord(taskId, envId, superOperator, (byte)(step+1), operators);
        }
    }*/

    @Override
    public PageResultContainer<CcTaskDetailConfigEntity> getConfigTaskDetail(PageRequestContainer<QueryTaskDetailReqVO> requestContainer, int userId) {
        CcTaskEntity taskEntity = getTaskEntity(requestContainer, userId, ProjectConstants.TASK_TYPE_CONFIG_CHANGE);
        if (taskEntity == null){
            return new PageResultContainer<>(new ArrayList<>(), 0);
        }else{
            return getConfigTaskDetail(taskEntity.getId(), requestContainer.getCurrentPage(), requestContainer.getPageSize());
        }
    }

    @Override
    public PageResultContainer<CcTaskDetailPushEntity> getPushTaskDetail(PageRequestContainer<QueryTaskDetailReqVO> requestContainer, int userId) {
        CcTaskEntity taskEntity = getTaskEntity(requestContainer, userId, ProjectConstants.TASK_TYPE_CONFIG_PUSH);
        if (taskEntity == null){
            return new PageResultContainer<>(new ArrayList<>(), 0);
        }else{
            return getPushTaskDetail(taskEntity.getId(), requestContainer.getCurrentPage(), requestContainer.getPageSize());
        }
    }

    /**
     * 获取当前登录用户指定类型的待提交任务或审核中任务
     * @author bawy
     * @date 2018/8/14 15:45
     * @param requestContainer 请求参数容器
     * @param userId 用户标识
     * @param taskType 任务类型
     * @return 符合条件的任务实体
     */
    private CcTaskEntity getTaskEntity(PageRequestContainer<QueryTaskDetailReqVO> requestContainer, int userId, byte taskType){
        ValidateUtil.checkPageParam(requestContainer);
        QueryTaskDetailReqVO queryTaskDetailReq = requestContainer.getData();
        byte taskState = queryTaskDetailReq.getTaskState();
        CcTaskEntity taskEntity;
        switch (taskState){
            case ProjectConstants.TASK_STATE_TO_SUBMIT:
                int envId = queryTaskDetailReq.getEnvId();
                Assert4CC.isTrue(envId>0, "环境标识不可为空");
                taskEntity = taskRepository.findByAppEnvIdAndCreatorAndTaskTypeAndTaskStateAndStatus(envId, userId, taskType, taskState, ProjectConstants.STATUS_VALID);
                break;
            case ProjectConstants.TASK_STATE_IN_REVIEW:
                int taskId = queryTaskDetailReq.getTaskId();
                Assert4CC.isTrue(taskId>0, "任务标识不可为空");
                taskEntity = taskRepository.findTaskWithOperator(taskId, userId, taskState, ProjectConstants.OPERATE_STATE_IN_REVIEW,ProjectConstants.STATUS_VALID);
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.PARAM_ERROR, "任务状态（" + taskState + "）异常");
        }
        return taskEntity;
    }

    /**
     * 获取指定配置任务的任务详情数据
     * @author bawy
     * @date 2018/8/10 14:08
     * @param taskId 任务标识
     * @param currentPage 当前页
     * @param pageZie 每页大小
     * @return 结果
     */
    private PageResultContainer<CcTaskDetailConfigEntity> getConfigTaskDetail(int taskId, int currentPage, int pageZie) {
        Pageable pageable = new PageRequest(currentPage, pageZie, Sort.Direction.ASC, "id");
        Page<CcTaskDetailConfigEntity> page = taskDetailConfigRepository.findAll(new Specification<CcTaskDetailConfigEntity>(){
            @Override
            public Predicate toPredicate(Root<CcTaskDetailConfigEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                list.add(criteriaBuilder.equal(root.get("taskId").as(int.class), taskId));
                list.add(criteriaBuilder.equal(root.get("status").as(byte.class), ProjectConstants.STATUS_VALID));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return new PageResultContainer<>(page);
    }

    /**
     * 获取指定推送任务的任务详情数据
     * @author bawy
     * @date 2018/8/14 15:46
     * @param taskId 任务标识
     * @param currentPage 当前页
     * @param pageZie 每页大小
     * @return 结果
     */
    private PageResultContainer<CcTaskDetailPushEntity> getPushTaskDetail(int taskId, int currentPage, int pageZie) {
        Pageable pageable = new PageRequest(currentPage, pageZie, Sort.Direction.ASC, "id");
        Page<CcTaskDetailPushEntity> page = taskDetailPushRepository.findAll(new Specification<CcTaskDetailPushEntity>(){
            @Override
            public Predicate toPredicate(Root<CcTaskDetailPushEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                list.add(criteriaBuilder.equal(root.get("taskId").as(int.class), taskId));
                list.add(criteriaBuilder.equal(root.get("status").as(byte.class), ProjectConstants.STATUS_VALID));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return new PageResultContainer<>(page);
    }

    @Override
    @Transactional
    public String dealTask(TaskDealReqVO taskDealReq, int operator){
        int taskId = taskDealReq.getTaskId();
        byte step = taskDealReq.getStep();
        byte operateType = taskDealReq.getOperateType();
        String remark = taskDealReq.getRemark();
        Assert4CC.isTrue(taskId>0, "任务标识不可为空");
        Assert4CC.isTrue(step>0, "操作步骤不能小于1");
        Assert4CC.isTrue(operateType == ProjectConstants.OPERATE_TYPE_PASS || operateType == ProjectConstants.OPERATE_TYPE_NOT_PASS, "操作类型不正确");
        if (operateType == ProjectConstants.OPERATE_TYPE_NOT_PASS){
            Assert4CC.isTrue(remark!=null&&remark.length()<=200&&remark.length()>=5, "审核不通过原因不可为空，且不通过原因5-200个字符");
        }
        CcTaskEntity taskEntity = taskRepository.findByIdAndStatus(taskId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务数据异常，taskId:"+taskId+";operator:"+operator+";");
        return dealTask(taskEntity, step, operator, taskDealReq.getNextOperator(), operateType, remark, taskDealReq.getExtInfo());
    }

    /**
     * 处理任务流程
     * @param taskEntity 任务对象
     * @param step 要处理的步骤
     * @param operator 操作员
     * @param operateType 操作类型
     * @param remark 操作标记
     * @param extInfoMap 操作扩展信息集合
     * @return 操作结果提示语
     */
    @Transactional
    public String dealTask(CcTaskEntity taskEntity, byte step, int operator, int nextOperator, byte operateType, String remark, Map<String, String> extInfoMap) {
        String response = "操作成功";
        int taskId = taskEntity.getId();
        CcTaskOperRecordEntity taskOperRecord = taskOperRecordRepository.findByTaskIdAndStepAndOperatorAndStatus(taskId, step, operator, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskOperRecord, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务操作记录数据异常，taskId:"+taskId+";step:"+step+";operator:"+operator+";");
        if (operateType == ProjectConstants.OPERATE_TYPE_PASS || operateType == ProjectConstants.OPERATE_TYPE_NOT_PASS){
            Assert4CC.isTrue(taskOperRecord.getOperationState() == ProjectConstants.OPERATE_STATE_IN_REVIEW, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "操作状态不是审核中,taskId:"+taskId+";setp:"+step+";operator:"+operator+";");
        }
        CcNotificationEntity notification = null;
        taskOperRecord.setReason(remark);
        //操作类型为审核通过或者提交任务
        if (operateType == ProjectConstants.OPERATE_TYPE_PASS || operateType == ProjectConstants.OPERATE_TYPE_SUBMIT){
            if (operateType == ProjectConstants.OPERATE_TYPE_SUBMIT){
                //将本记录设置为提交成功状态
                taskOperRecord.setOperationState(ProjectConstants.OPERATE_STATE_SUBMIT);
                response = "任务提交成功，";
            }else {
                //将本记录设置为审核通过状态
                taskOperRecord.setOperationState(ProjectConstants.OPERATE_STATE_REVIEW_PASS);
                response = "审核通过，";
            }
            //判断是否有下一步骤
            CcTaskOperRecordEntity nextTaskOperRecord = taskOperRecordRepository.findByTaskIdAndStepAndStatus(taskId, (byte)(step+1), ProjectConstants.STATUS_VALID);
            if (nextTaskOperRecord == null){
                //任务流程结束，调用保存接口
                taskEntity.setTaskState(ProjectConstants.TASK_STATE_END);
                if (operateType == ProjectConstants.OPERATE_TYPE_SUBMIT){
                    response += "本次任务提交无需审核";
                }else{
                    response += "任务流程结束，系统将通知任务创建人";
                    notification = NotificationUtil.createNotification(NotificationUtil.NOTIFICATION_TYPE_REVIEW_RESULT,"任务审核结果通知","您创建的任务（任务名称："+taskEntity.getTaskName()+";任务编号："+taskEntity.getId()+"）审核通过", taskEntity.getCreator(), operator, null);
                }
                commitTaskDetails(taskId, taskEntity.getAppEnvId(), taskEntity.getTaskType());
            }else{
                Assert4CC.isTrue(nextTaskOperRecord.getOperationState() == ProjectConstants.OPERATE_STATE_TO_AUDIT, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "下一审核节点操作状态不是待审核状态，taskId:"+taskId+";step:"+step+";");
                //将下一步骤设置为审核中
                checkNextOperator(taskEntity, nextTaskOperRecord, nextOperator);
                response += "系统将通知下一审核人审核";
                notification = NotificationUtil.createNotification(NotificationUtil.NOTIFICATION_TYPE_TASK_REVIEW,"任务审核通知","有新的任务（任务名称："+taskEntity.getTaskName()+";任务编号："+taskEntity.getId()+"）等待您审核", nextOperator, operator, taskEntity.getId()+"");
                taskEntity.setTaskState(ProjectConstants.TASK_STATE_IN_REVIEW);
                nextTaskOperRecord.setOperator(nextOperator);
                nextTaskOperRecord.setOperationState(ProjectConstants.OPERATE_STATE_IN_REVIEW);
                taskOperRecordRepository.saveAndFlush(nextTaskOperRecord);
            }
        }else if (operateType == ProjectConstants.OPERATE_TYPE_NOT_PASS){
            //将本记录设置为审核不通过状态
            taskOperRecord.setOperationState(ProjectConstants.OPERATE_STATE_REVIEW_NOT_PASS);
            //回退任务
            taskEntity.setTaskState(ProjectConstants.TASK_STATE_END);
            response = "审核不通过，任务已驳回，系统将通知任务创建人";
            notification = NotificationUtil.createNotification(NotificationUtil.NOTIFICATION_TYPE_REVIEW_RESULT,"任务审核结果通知","您创建的任务（任务名称："+taskEntity.getTaskName()+";任务编号："+taskEntity.getId()+"）审核不通过", taskEntity.getCreator(), operator, null);
            rollBackTaskDetails(taskId, taskEntity.getTaskType(), operator);
        }
        //处理任务操作记录详情
        dealTaskOperateRecordDetail(taskOperRecord.getId(), operator, extInfoMap);
        //更新任务
        Timestamp currentTime = TimeUtil.currentTime();
        taskEntity.setModifier(operator);
        taskEntity.setUpdateTime(currentTime);
        taskRepository.saveAndFlush(taskEntity);
        //更新任务操作记录
        taskOperRecord.setUpdateTime(currentTime);
        taskOperRecordRepository.saveAndFlush(taskOperRecord);
        //消息通知（放在最后）
        notificationSV.createNotification(notification);
        return response;
    }

    /**
     * 处理任务操作记录详情
     * @author bawy
     * @date 2018/8/2 17:09
     * @param taskOperRecordId 任务操作记录标识
     * @param extInfo 扩展信息
     */
    @Transactional
    public void dealTaskOperateRecordDetail(int taskOperRecordId, int creator, Map<String, String> extInfo){
        if (extInfo!=null){
            Timestamp currentTime = TimeUtil.currentTime();
            List<CcTaskOperRecordDetailEntity> taskOperRecordDetailEntities = new ArrayList<>();
            for (Map.Entry<String, String> entry:extInfo.entrySet()){
                CcTaskOperRecordDetailEntity taskOperRecordDetailEntity = new CcTaskOperRecordDetailEntity();
                taskOperRecordDetailEntity.setRecordId(taskOperRecordId);
                taskOperRecordDetailEntity.setRecordDetailKey(entry.getKey());
                taskOperRecordDetailEntity.setRecordDetailType(ProjectConstants.OPER_RECORD_TYPE_INSTANCE_INFO);
                taskOperRecordDetailEntity.setRecordDetailValue(entry.getValue());
                taskOperRecordDetailEntity.setCreator(creator);
                taskOperRecordDetailEntity.setCreateTime(currentTime);
                taskOperRecordDetailEntity.setModifier(creator);
                taskOperRecordDetailEntity.setUpdateTime(currentTime);
                taskOperRecordDetailEntity.setStatus(ProjectConstants.STATUS_VALID);
                taskOperRecordDetailEntities.add(taskOperRecordDetailEntity);
            }
            taskOperRecordDetailRepository.save(taskOperRecordDetailEntities);
        }
    }

    /**
     * 校验选择的下一审核人是否正确
     * @author bawy
     * @date 2018/8/24 16:01
     * @param taskEntity 任务
     * @param taskOperRecordEntity 下一审核记录
     * @param nextOperator 下一审核人
     */
    private void checkNextOperator(CcTaskEntity taskEntity, CcTaskOperRecordEntity taskOperRecordEntity, int nextOperator){
        Assert4CC.isTrue(nextOperator>0, "当前任务存在下一审核流程，下一审核人不可为空");
        int envId = taskEntity.getAppEnvId();
        int creator = taskEntity.getCreator();
        byte step = (byte)(taskOperRecordEntity.getStep()-1);
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = taskAuditStrategySV.getAuditStrategyStep(envId, step);
        Assert4CC.notNull(taskAuditStrategyEntity, ResultCodeEnum.DATA_ERROR, "无法获取环境"+envId+"审核策略第"+step+"步的内容");
        int orgId = taskAuditStrategyEntity.getOrgId();
        int roleId = taskAuditStrategyEntity.getRoleId();
        if (orgId == 0){
            //获取
            CcOrgUserRelEntity orgUserRelEntity = orgSV.getOrgByUserId(creator);
            Assert4CC.notNull(orgUserRelEntity, ResultCodeEnum.DATA_ERROR, "无法获取任务创建人所在组织");
            orgId = orgUserRelEntity.getOrgId();
        }
        boolean nextOperatorInOrg = orgSV.checkUserInOrg(orgId, roleId, nextOperator);
        Assert4CC.isTrue(nextOperatorInOrg, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "下一审核人"+nextOperator+"不在组织"+orgId+"中，或者在此组织中非"+roleId+"角色");
    }

    @Override
    @Transactional
    public String rollbackTask(TaskBaseReqVO taskBaseReq, int operator) {
        int taskId = taskBaseReq.getTaskId();
        Assert4CC.isTrue(taskId>0, "任务标识不可为空");
        CcTaskEntity taskEntity = taskRepository.findByIdAndCreatorAndStatus(taskId, operator, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务数据失败，taskId:"+taskId+";operator:"+operator+";");
        Assert4CC.isTrue(taskEntity.getTaskState() == ProjectConstants.TASK_STATE_TO_SUBMIT, "任务不是待提交状态");
        taskEntity.setStatus(ProjectConstants.STATUS_INVALID);
        taskRepository.saveAndFlush(taskEntity);
        rollBackTaskDetails(taskId, taskEntity.getTaskType(), operator);
        return "任务撤销成功";
    }

    @Override
    @Transactional
    public String rollbackTaskDetail(TaskDetailBaseReqVO taskDetailBaseReq, int operator) {
        int taskId = taskDetailBaseReq.getTaskId();
        int taskDetailId = taskDetailBaseReq.getTaskDetailId();
        Assert4CC.isTrue(taskId>0, "任务标识不可为空");
        Assert4CC.isTrue(taskDetailId>0, "任务详情标识不可为空");
        CcTaskEntity taskEntity = taskRepository.findByIdAndCreatorAndStatus(taskId, operator, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务数据失败，taskId:"+taskId+";operator:"+operator+";");
        switch (taskEntity.getTaskType()){
            case ProjectConstants.TASK_TYPE_CONFIG_CHANGE:
                CcTaskDetailConfigEntity taskDetailConfig = taskDetailConfigRepository.findByIdAndTaskIdAndCreatorAndStatus(taskDetailId, taskId, operator, ProjectConstants.STATUS_VALID);
                Assert4CC.notNull(taskDetailConfig, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取任务详情数据，id:"+taskDetailId+";taskId:"+taskId+";operator:"+operator+";");
                taskDetailConfig.setUpdateTime(TimeUtil.currentTime());
                taskDetailConfig.setStatus(ProjectConstants.STATUS_INVALID);
                if (taskDetailConfig.getConfigType() == ProjectConstants.CONFIG_TYPE_FILE) {
                    //如果配置类型为配置文件，需要回调接口将临时文件删除
                    configFileSV.rollback(taskId, taskDetailConfig);
                }
                taskDetailConfigRepository.save(taskDetailConfig);
                break;
            case ProjectConstants.TASK_TYPE_CONFIG_PUSH:
                CcTaskDetailPushEntity taskDetailPushEntity = taskDetailPushRepository.findByIdAndTaskIdAndCreatorAndStatus(taskDetailId, taskId, operator, ProjectConstants.STATUS_VALID);
                Assert4CC.notNull(taskDetailPushEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取任务详情数据，id:"+taskDetailId+";taskId:"+taskId+";operator:"+operator+";");
                taskDetailPushEntity.setUpdateTime(TimeUtil.currentTime());
                taskDetailPushEntity.setStatus(ProjectConstants.STATUS_INVALID);
                taskDetailPushRepository.save(taskDetailPushEntity);
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.TASK_TYPE_ILLEGAL);
        }
        return "撤销成功";
    }

    /**
     * 回退任务详情（配置变更、配置推送）
     * @author bawy
     * @date 2018/8/2 22:15
     * @param taskId 任务标识
     * @param taskType 任务类型 1：配置变更；2：配置推送
     * @param operator 操作人
     */
    @Transactional
    public void rollBackTaskDetails(int taskId, byte taskType, int operator){
        Timestamp currentTime = TimeUtil.currentTime();
        switch (taskType){
            case ProjectConstants.TASK_TYPE_CONFIG_CHANGE:
                //配置文件变更
                List<CcTaskDetailConfigEntity> taskDetailConfigs = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId, ProjectConstants.STATUS_VALID);
                if (taskDetailConfigs!=null) {
                    for (CcTaskDetailConfigEntity taskDetailConfig : taskDetailConfigs) {
                        taskDetailConfig.setStatus(ProjectConstants.STATUS_INVALID);
                        taskDetailConfig.setModifier(operator);
                        taskDetailConfig.setUpdateTime(currentTime);
                        if (taskDetailConfig.getConfigType() == ProjectConstants.CONFIG_TYPE_FILE) {
                            //如果配置类型为配置文件，需要回调接口将临时文件删除
                            configFileSV.rollback(taskId, taskDetailConfig);
                        }
                    }
                    taskDetailConfigRepository.save(taskDetailConfigs);
                }
                break;
            case ProjectConstants.TASK_TYPE_CONFIG_PUSH:
                //配置推送
                List<CcTaskDetailPushEntity> taskDetailPushEntities = taskDetailPushRepository.findByTaskIdAndStatusOrderByIdAsc(taskId, ProjectConstants.STATUS_VALID);
                if (taskDetailPushEntities!=null) {
                    for (CcTaskDetailPushEntity taskDetailPushEntity : taskDetailPushEntities) {
                        taskDetailPushEntity.setStatus(ProjectConstants.STATUS_INVALID);
                        taskDetailPushEntity.setModifier(operator);
                        taskDetailPushEntity.setUpdateTime(currentTime);
                    }
                    taskDetailPushRepository.save(taskDetailPushEntities);
                }
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.TASK_TYPE_ILLEGAL);
        }

    }

    /**
     * 任务流程结束后，提交任务详情。将数据保存到配置文件、配置项表中。
     * @author bawy
     * @date 2018/8/3 13:44
     * @param taskId 任务标识
     * @param envId 环境标识
     * @param taskType 任务类型
     */
    @Transactional
    public void commitTaskDetails(int taskId, int envId, byte taskType){
        switch (taskType){
            case ProjectConstants.TASK_TYPE_CONFIG_CHANGE:
                //配置文件变更
                List<CcTaskDetailConfigEntity> taskDetailConfigs = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId, ProjectConstants.STATUS_VALID);
                if (taskDetailConfigs!=null) {
                    for (CcTaskDetailConfigEntity taskDetailConfig : taskDetailConfigs) {
                        if (taskDetailConfig.getConfigType() == ProjectConstants.CONFIG_TYPE_FILE) {
                            configFileSV.saveOrUpdateFile(taskId, envId, taskDetailConfig);
                        }else {
                            configItemSV.saveOrUpdateItem(taskId, envId, taskDetailConfig);
                        }
                    }
                    taskDetailConfigRepository.save(taskDetailConfigs);
                }
                break;
            case ProjectConstants.TASK_TYPE_CONFIG_PUSH:
                //配置推送
                configPushSV.pushTaskPass(taskId,envId);
                break;
            default:
                throw new ErrorCodeException(ResultCodeEnum.TASK_TYPE_ILLEGAL);
        }
    }

    @Override
    public PageResultContainer<MySubmitTaskVO> getSubmitTask(PageRequestContainer<MySubmitTaskReqVO> requestContainer, int userId) {
        ValidateUtil.checkPageParam(requestContainer);
        MySubmitTaskReqVO mySubmitTaskReq = requestContainer.getData();
        List<MySubmitTaskVO> mySubmitTasks = taskMapper.getSubmitTasks(userId, mySubmitTaskReq.getTaskType(), mySubmitTaskReq.getTaskState(),
                mySubmitTaskReq.getStartDate(), mySubmitTaskReq.getEndDate(),
                requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = taskMapper.getSubmitTasksCount(userId, mySubmitTaskReq.getTaskType(), mySubmitTaskReq.getTaskState(),
                mySubmitTaskReq.getStartDate(), mySubmitTaskReq.getEndDate());
        return new PageResultContainer<>(mySubmitTasks, count);
    }

    @Override
    public PageResultContainer<MyInReviewTaskVO> getInReviewTask(PageRequestContainer<MyInReviewTaskReqVO> requestContainer, int userId) {
        ValidateUtil.checkPageParam(requestContainer);
        MyInReviewTaskReqVO myInReviewTaskReq = requestContainer.getData();
        List<MyInReviewTaskVO> myInReviewTasks = taskMapper.getReviewWaitingTasks(userId, myInReviewTaskReq.getTaskId(), myInReviewTaskReq.getTaskType(), myInReviewTaskReq.getCreatorName(),
                myInReviewTaskReq.getStartDate(), myInReviewTaskReq.getEndDate(),
                requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = taskMapper.getReviewWaitingTasksCount(userId, myInReviewTaskReq.getTaskId(), myInReviewTaskReq.getTaskType(), myInReviewTaskReq.getCreatorName(),
                myInReviewTaskReq.getStartDate(), myInReviewTaskReq.getEndDate());
        return new PageResultContainer<>(myInReviewTasks, count);
    }

    @Override
    public PageResultContainer<MyReviewedTaskVO> getReviewedTask(PageRequestContainer<MyReviewedTaskReqVO> requestContainer, int userId) {
        ValidateUtil.checkPageParam(requestContainer);
        MyReviewedTaskReqVO myReviewedTaskReq = requestContainer.getData();
        List<MyReviewedTaskVO> myReviewedTasks = taskMapper.getMyReviewedTasks(userId, myReviewedTaskReq.getTaskType(), myReviewedTaskReq.getTaskState(), myReviewedTaskReq.getCreatorName(),
                myReviewedTaskReq.getStartDate(), myReviewedTaskReq.getEndDate(),
                requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = taskMapper.getMyReviewedTasksCount(userId, myReviewedTaskReq.getTaskType(), myReviewedTaskReq.getTaskState(), myReviewedTaskReq.getCreatorName(),
                myReviewedTaskReq.getStartDate(), myReviewedTaskReq.getEndDate());
        return new PageResultContainer<>(myReviewedTasks, count);
    }

    @Override
    public List<TaskStepVO> getTaskFlow(int taskId, int userId) {
        Assert4CC.isTrue(taskId>0, "任务编码不可为空");
        List<CcTaskOperRecordEntity> taskOperRecordEntity = taskOperRecordRepository.findByTaskIdAndOperatorAndStatus(taskId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.isTrue(taskOperRecordEntity.size()>0,ResultCodeEnum.ILLEGAL_OPERATION_ERROR,"当前登录用户（"+userId+"）不在此任务（"+taskId+"）流程中");
        return taskMapper.getTaskFlow(taskId);
    }

    @Override
    public CcTaskDetailConfigEntity getConfigChangeTaskInfo(int envId, int taskDetailId) {
        CcTaskDetailConfigEntity taskDetailConfigEntity = taskDetailConfigRepository.findByEnvIdAndDetailId(envId, taskDetailId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskDetailConfigEntity, ResultCodeEnum.TASK_COMMON_ERROR, "当前环境（ID："+envId+"）不存在此配置变更任务详情（ID："+taskDetailConfigEntity+"）");
        return taskDetailConfigEntity;
    }

    @Override
    public CcTaskEntity getTaskById(int taskId) {
        return taskRepository.findByIdAndStatus(taskId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public List<CcTaskDetailPushEntity> getTaskDetailPushsByTaskId(int taskId) {
        return taskDetailPushRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public List<CcTaskEntity> getUnfinishedTaskByCreatorId(int userId){
        byte [] taskTypes = {ProjectConstants.TASK_STATE_END};
        return taskRepository.findByCreatorAndTaskTypeNotInAndStatus(userId,taskTypes,ProjectConstants.STATUS_VALID);
    }

    @Override
    public void checkUserHasNotUnfinishedTask(int userId) {
        List<CcTaskEntity> taskList = getUnfinishedTaskByCreatorId(userId);
        Assert4CC.isTrue(taskList == null || taskList.size() == 0,ResultCodeEnum.USER_COMMON_ERROR,"用户还有未结束的任务,请结束任务之后再注销用户.");
    }

    @Override
    public boolean hasInReviewTask(int envId) {
        List<CcTaskEntity> taskEntities = taskRepository.findByAppEnvIdAndTaskStateAndStatus(envId, ProjectConstants.TASK_STATE_IN_REVIEW, ProjectConstants.STATUS_VALID);
        return taskEntities!=null&&taskEntities.size()>0;
    }

    @Override
    public TaskNextOperatorInfoVO getNextOperatorInfo(int taskId, byte step, int operator) {
        Assert4CC.isTrue(taskId>0, "任务标识不可为空");
        Assert4CC.isTrue(step>0, "步骤序号应为大于0的整数");
        CcTaskEntity taskEntity = taskRepository.findByIdAndStatus(taskId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务"+taskId+"的信息失败");
        if (step == 1){
            step++;
        }else {
            CcTaskOperRecordEntity taskOperRecordEntity = taskOperRecordRepository.findByTaskIdAndStepAndOperatorAndStatus(taskId, step, operator, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(taskOperRecordEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取任务操作记录数据异常，taskId:"+taskId+";step:"+step+";operator:"+operator+";");
            //判断是否有下一步
            CcTaskOperRecordEntity nextTaskOperRecord = taskOperRecordRepository.findByTaskIdAndStepAndStatus(taskId, ++step, ProjectConstants.STATUS_VALID);
            if (nextTaskOperRecord==null){
                return null;
            }
        }
        int envId = taskEntity.getAppEnvId();
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = taskAuditStrategySV.getAuditStrategyStep(envId, --step);
        Assert4CC.notNull(taskAuditStrategyEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "获取环境"+envId+"的审核策略的第"+step+"步信息失败");
        AuditStrategyStepVO auditStrategyStep = taskAuditStrategySV.convertAuditStrategyStep(taskAuditStrategyEntity);
        List<OrgUserVO> orgUsers = orgSV.getOrgUsers(auditStrategyStep.getOrgId(), auditStrategyStep.getRoleId(), operator);
        TaskNextOperatorInfoVO taskNextOperatorInfo = new TaskNextOperatorInfoVO();
        taskNextOperatorInfo.setStrategyStep(auditStrategyStep);
        taskNextOperatorInfo.setOrgUsers(orgUsers);
        return taskNextOperatorInfo;
    }

    @Override
    public CcTaskEntity getTaskInfoWithCheck(int taskId, int userId) {
        //1.校验任务是否由此用户创建
        CcTaskEntity taskEntity = taskRepository.findByIdAndCreatorAndStatus(taskId, userId, ProjectConstants.STATUS_VALID);
        if (taskEntity == null){
            //2.校验任务当前是否由此用户审核
            taskEntity = taskRepository.findTaskWithOperator(taskId, userId, ProjectConstants.TASK_STATE_IN_REVIEW, ProjectConstants.OPERATE_STATE_IN_REVIEW, ProjectConstants.STATUS_VALID);
        }
        if (taskEntity == null){
            throw new ErrorCodeException(ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "当前登录用户非此任务创建人或者当前审核人");
        } else {
            return taskEntity;
        }
    }

    @Override
    public StrategyChangeDetailVO getStrategyChangeDetail(int taskDetailId, int userId) {
        CcTaskDetailConfigEntity taskDetailConfigEntity = taskDetailConfigRepository.findByIdAndStatus(taskDetailId, ProjectConstants.STATUS_VALID);
        if (taskDetailConfigEntity == null || taskDetailConfigEntity.getChangeType() != ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE){
            throw new ErrorCodeException(ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "不存在ID为"+taskDetailId+"的配置刷新策略变更任务详情");
        }
        int taskId = taskDetailConfigEntity.getTaskId();
        int newStrategyId = taskDetailConfigEntity.getStrategyId();
        CcTaskEntity taskEntity = getTaskInfoWithCheck(taskId, userId);
        int envId = taskEntity.getAppEnvId();
        CcAppEnvEntity appEnvEntity = appEnvSV.getEnvByEnvId(envId);
        StrategyChangeDetailVO strategyChangeDetail = new StrategyChangeDetailVO();
        ConfigUpdateStrategyVO newStrategy = configUpdateStrategySV.getConfigUpdateStrategyAllById(newStrategyId, appEnvEntity.getAppId());
        strategyChangeDetail.setNewStrategy(newStrategy);
        int configId = taskDetailConfigEntity.getConfigId();
        Integer oldStrategyId = null;
        switch (taskDetailConfigEntity.getConfigType()){
            case ProjectConstants.CONFIG_TYPE_FILE:
                oldStrategyId = configFileSV.getConfigFileByIdCheck(configId).getStrategyId();
                break;
            case ProjectConstants.CONFIG_TYPE_ITEM:
                oldStrategyId = configItemSV.getConfigItemByIdCheck(configId, envId).getStrategyId();
                break;
        }
        if (oldStrategyId!=null&&oldStrategyId>0){
            ConfigUpdateStrategyVO oldStrategy = configUpdateStrategySV.getConfigUpdateStrategyAllById(oldStrategyId, appEnvEntity.getAppId());
            strategyChangeDetail.setOldStrategy(oldStrategy);
        }
        return strategyChangeDetail;
    }

    @Override
    public ConfigChangeDetailVO getConfigChangeDetail(int taskDetailId, int userId) {
        CcTaskDetailConfigEntity taskDetailConfigEntity = taskDetailConfigRepository.findByIdAndStatus(taskDetailId, ProjectConstants.STATUS_VALID);
        if (taskDetailConfigEntity == null || taskDetailConfigEntity.getChangeType() == ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE){
            throw new ErrorCodeException(ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "不存在ID为"+taskDetailId+"的配置内容变更任务详情");
        }
        int taskId = taskDetailConfigEntity.getTaskId();
        CcTaskEntity taskEntity = getTaskInfoWithCheck(taskId, userId);
        int envId = taskEntity.getAppEnvId();
        ConfigChangeDetailVO configChangeDetail = new ConfigChangeDetailVO();
        configChangeDetail.setOldContent("");
        configChangeDetail.setNewContent("");
        byte changeType = taskDetailConfigEntity.getChangeType();
        switch (taskDetailConfigEntity.getConfigType()){
            case ProjectConstants.CONFIG_TYPE_FILE:
                if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
                    configChangeDetail.setNewContent(configFileSV.getTempFileContent(envId, taskDetailId).getConfigFileContent());
                }
                if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_DEL || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
                    configChangeDetail.setOldContent(configFileSV.getLastVersion(taskDetailConfigEntity.getConfigId(), envId).getConfigFileContent());
                }
                break;
            case ProjectConstants.CONFIG_TYPE_ITEM:
                if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_ADD || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
                    configChangeDetail.setNewContent(taskDetailConfigEntity.getConfigContent());
                }
                if (changeType == ProjectConstants.CONFIG_CHANGE_TYPE_DEL || changeType == ProjectConstants.CONFIG_CHANGE_TYPE_MOD){
                    configChangeDetail.setOldContent(configItemSV.getConfigItemByIdCheck(taskDetailConfigEntity.getConfigId(), envId).getItemValue());
                }
                break;
        }
        return configChangeDetail;
    }

    @Override
    public long getPassTaskCount(int userId) {
        if(iUserSV.isAdministrator(userId)){
            return taskMapper.getTaskByOperationState(0,ProjectConstants.OPERATE_STATE_IN_REVIEW);
        }else{
            return taskMapper.getTaskByOperationState(userId,ProjectConstants.OPERATE_STATE_IN_REVIEW);
        }
    }

    @Override
    public long getAuditTaskCount(int userId) {
        if(iUserSV.isAdministrator(userId)){
            return taskMapper.getTaskByOperationState(0,ProjectConstants.OPERATE_STATE_TO_AUDIT);
        }else{
            return taskMapper.getTaskByOperationState(userId,ProjectConstants.OPERATE_STATE_TO_AUDIT);
        }
    }

    @Override
    public long getRollBackTaskCount(int userId) {
        if(iUserSV.isAdministrator(userId)){
            return taskMapper.getRollbackTaskByUserId(0);
        }else{
            return taskMapper.getRollbackTaskByUserId(userId);
        }
    }

    @Override
    public TaskTimeAndInsVO getTaskTimeAndIns(int taskId) {
        Assert4CC.isTrue(taskId > 0, "任务标识不可为空！");
        List<CcTaskExtInfoEntity> ccTaskExtInfoEntityList = taskExtInfoRepository.findByTaskIdAndStatus(taskId, ProjectConstants.STATUS_VALID);
        TaskTimeAndInsVO taskTimeAndInsVO = new TaskTimeAndInsVO();
        List<TaskInsVO> taskInsVOS = new ArrayList<>();
        TaskInsVO taskInsVO = new TaskInsVO();
        for (CcTaskExtInfoEntity ccTaskExtInfoEntity : ccTaskExtInfoEntityList) {
            //查询实例信息
            if (ccTaskExtInfoEntity.getExtInfoKey().equals("INSTANCES")) {
                int [] instanceIds = CcStringUtil.splitByCommaAndConvertToInt(ccTaskExtInfoEntity.getExtInfoValue());
                List<CcInstanceEntity> instanceEntityList = instanceRepository.findAllByIdInAndStatus(instanceIds, ProjectConstants.STATUS_VALID);
                for (CcInstanceEntity instanceEntity : instanceEntityList) {
                    taskInsVO.setInsName(instanceEntity.getInsName());
                    taskInsVO.setInsIp(instanceEntity.getInsIp());
                    taskInsVO.setUpdateTime(instanceEntity.getUpdateTime());
                    taskInsVOS.add(taskInsVO);
                }
                taskTimeAndInsVO.setTaskInsVOS(taskInsVOS);
            } else if (ccTaskExtInfoEntity.getExtInfoKey().equals("PUSH_TIME")){
                taskTimeAndInsVO.setPushTime(ccTaskExtInfoEntity.getExtInfoValue());
            }
        }
        return taskTimeAndInsVO;
    }
}
