package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.entity.CcTaskEntity;
import com.asiainfo.configcenter.center.entity.CcTaskDetailPushEntity;
import com.asiainfo.configcenter.center.vo.task.*;

import java.util.List;

/**
 * 任务服务接口
 * Created by bawy on 2018/8/1 15:43.
 */
public interface ITaskSV {


    /**
     * 创建配置项变更任务
     * **************************************************
     * 1.参数校验应在配置项本身的业务服务中完成，此处不再做参数校验
     * 2.变更类型需要从常量（CONFIG_CHANGE_TYPE_*）中取，只能是新增、修改、删除中的一种
     * 3.如果是新增配置项则configId传值为0，否则需要填写configId
     * 4.如果是删除配置项，则配置项名称和配置项值可以为null
     * 5.如果当前用户已存在未提交任务，则将此配置项变更合并到此任务中。
     * 6.如果当前用户未提交任务中已经存在对改配置项的变更，则抛出异常不允许变更此配置项。
     * **************************************************
     * @author bawy
     * @date 2018/8/1 16:07
     * @param envId 环境标识
     * @param creator 创建人
     * @param changeType 变更类型
     * @param confId 配置标识（非必填）
     * @param strategyId 刷新策略标识（非必填）
     * @param itemKey 配置项名称
     * @param itemValue 配置项值
     * @param description 描述（非必填）
     */
    void createTaskForConfigItem(int envId, int creator, byte changeType, Integer confId, Integer strategyId, String itemKey, String itemValue, String description);

    /**
     * 创建配置文件变更任务
     * **************************************************
     * 1.参数校验应在配置文件本身的业务服务中完成，此处不再做参数校验
     * 2.变更类型需要从常量（CONFIG_CHANGE_TYPE_*）中取，只能是新增、修改、删除中的一种
     * 3.如果是新增配置文件则configId传值为0，否则需要填写configId
     * 4.如果是删除配置文件，则配置文件名称和commitId可以为null
     * 5.如果当前用户已存在未提交任务，则将此配置文件变更合并到此任务中。
     * 6.如果当前用户未提交任务中已经存在对改配置文件的变更，则抛出异常不允许变更此配置文件。
     * **************************************************
     * @author bawy
     * @date 2018/8/1 16:07
     * @param envId 环境标识
     * @param creator 创建人
     * @param changeType 变更类型
     * @param confId 配置标识
     * @param strategyId 刷新策略标识（非必填）
     * @param fileName 配置文件名称
     * @param commitId gitlab合并提交标识
     * @param description 描述（非必填）
     * @return 任务主键和任务详情主键
     */
    TaskInfo createTaskForConfigFile(int envId, int creator, byte changeType, Integer confId, Integer strategyId, String fileName, String commitId, String description);

    /**
     * 创建配置推送任务
     ** **************************************************
     * 1.参数校验应在配置文件本身的业务服务中完成，此处不再做参数校验
     * 2.配置类型需要从常量（CONFIG_TYPE_*）中取，只能是配置文件、配置项中的一种
     * 3.如果当前用户已存在未提交任务，则将此配置推送合并到此任务中。
     * 4.如果当前用户未提交任务中已经存在对此配置的推送，则抛出异常不允许重复推送。
     * **************************************************
     * @author bawy
     * @date 2018/8/14 14:32
     * @param envId 环境标识
     * @param creator 创建人
     * @param configType 配置类型
     * @param configId 配置标识
     * @param configName 配置名称
     * @param version 版本号 配置项：填写历史版本ID，配置文件填写历史版本commitId
     * @param instances 实例
     * @param cronTime cron表达式
     * @param description 描述信息
     */
    void createTaskForConfigPush(int envId, int creator, byte configType, int configId, String configName, String version, String instances, String cronTime, String description);

    /**
     * 提交任务，进入任务审核流程
     * @author bawy
     * @date 2018/8/1 19:58
     * @param submitTaskReq 提交任务的请求参数
     * @param submitter 任务提交人
     * @return 返回成功提交提示语
     */
    String submitTask(SubmitTaskReqVO submitTaskReq, int submitter);

    /**
     * 获取配置变更任务详情数据
     * @author bawy
     * @date 2018/8/10 14:04
     * @param requestContainer 请求参数容器
     * @param userId 用户标识
     * @return 配置变更详情列表
     */
    PageResultContainer<CcTaskDetailConfigEntity> getConfigTaskDetail(PageRequestContainer<QueryTaskDetailReqVO> requestContainer, int userId);

    /**
     * 获取配置推送任务详情数据
     * @author bawy
     * @date 2018/8/14 15:37
     * @param requestContainer 请求参数容器
     * @param userId 用户标识
     * @return 配置推送详情列表
     */
    PageResultContainer<CcTaskDetailPushEntity> getPushTaskDetail(PageRequestContainer<QueryTaskDetailReqVO> requestContainer, int userId);

    /**
     * 处理任务
     * @author bawy
     * @date 2018/8/2 16:50
     * @param taskDealReq 任务审核请求参数对象
     * @param operator 操作员（当前登录用户）
     * @return 操作结果
     */
    String dealTask(TaskDealReqVO taskDealReq, int operator);

    /**
     * 撤销任务
     * @author bawy
     * @date 2018/8/2 21:52
     * @param taskBaseReq 任务基础请求参数
     * @param operator 操作员（当前登录用户）
     * @return 撤销任务结果
     */
    String rollbackTask(TaskBaseReqVO taskBaseReq, int operator);

    /**
     * 撤销一条任务详情
     * @author bawy
     * @date 2018/8/2 22:42
     * @param taskDetailBaseReq 任务详情基础请求参数
     * @param operator 操作员（当前登录用户）
     * @return 撤销任务详情结果
     */
    String rollbackTaskDetail(TaskDetailBaseReqVO taskDetailBaseReq, int operator);

    /**
     * 获取用户已经提交的所有任务
     * @author bawy
     * @date 2018/8/5 22:44
     * @param requestContainer 请求参数容器
     * @param userId 当前登录用户
     * @return 所有已提交的任务集合
     */
    PageResultContainer<MySubmitTaskVO> getSubmitTask(PageRequestContainer<MySubmitTaskReqVO> requestContainer, int userId);

    /**
     * 获取当前登录用户待审核任务
     * @author bawy
     * @date 2018/8/5 22:44
     * @param requestContainer 请求参数容器
     * @param userId 用户标识
     * @return 所有符合条件的待审核任务列表
     */
    PageResultContainer<MyInReviewTaskVO> getInReviewTask(PageRequestContainer<MyInReviewTaskReqVO> requestContainer, int userId);

    /**
     * 获取当前登录用户所有已审核的任务
     * @author bawy
     * @date 2018/8/6 17:01
     * @param requestContainer 请求参数容器
     * @param userId 用户标识（当前登录用户）
     * @return 所有符合条件的已审核任务列表
     */
    PageResultContainer<MyReviewedTaskVO> getReviewedTask(PageRequestContainer<MyReviewedTaskReqVO> requestContainer, int userId);

    /**
     * 获取任务流程
     * @author bawy
     * @date 2018/8/6 22:47
     * @param taskId 任务编码
     * @param userId 用户标识(当前登录用户)
     * @return 任务流程
     */
    List<TaskStepVO> getTaskFlow(int taskId, int userId);

    /**
     * 根据任务详情标识和环境标识获取配置变更任务信息
     * @author bawy
     * @date 2018/8/9 16:26
     * @param envId 环境标识
     * @param taskDetailId 任务详情标识
     * @return 配置变更任务信息
     */
    CcTaskDetailConfigEntity getConfigChangeTaskInfo(int envId, int taskDetailId);

    /**
     * 根据任务主键查询任务
     * @author oulc
     * @date 18-8-14 下午3:29
     * @param taskId 任务主键
     * @return 任务实体
     */
    CcTaskEntity getTaskById(int taskId);

    /**
     * 根据任务主键查询任务详情
     * @author oulc
     * @date 18-8-14 下午4:51
     * @param taskId 任务主键
     * @return 任务详情列表
     */
    List<CcTaskDetailPushEntity> getTaskDetailPushsByTaskId(int taskId);

    /**
     * 根据用户主键查询任务
     * @author oulc
     * @date 18-8-22 下午2:08
     * @param userId 用户主键
     * @return 任务列表
     */
    List<CcTaskEntity> getUnfinishedTaskByCreatorId(int userId);

    /**
     * 校验用户没有未完成的任务
     * @author oulc
     * @date 18-8-22 下午2:42
     * @param userId 用户主键
     */
    void checkUserHasNotUnfinishedTask(int userId);

    /**
     * 判断指定环境是否存在审核中任务
     * @author bawy
     * @date 2018/8/24 13:17
     * @param envId 环境标识
     */
    boolean hasInReviewTask(int envId);

    /**
     * 根据任务标识和操作员获取任务下一审核节点人员信息
     * @author bawy
     * @date 2018/8/24 16:34
     * @param taskId 任务标识
     * @param step 操作步骤
     * @param operator 当前操作员（当前登录用户）
     * @return 下一审核节点人员信息
     */
    TaskNextOperatorInfoVO getNextOperatorInfo(int taskId,  byte step, int operator);

    /**
     * 获取任务信息，需要校验用户是否在此任务的审核流程中
     * ***************************************
     * 1.校验任务是否由此用户创建
     * 2.校验任务当前是否有此用户审核
     * ***************************************
     * @author bawy
     * @date 2018/8/27 13:58
     * @param taskId 任务标识
     * @param userId 用户标识
     * @return 用户在任务流程中返回该任务实体
     */
    CcTaskEntity getTaskInfoWithCheck(int taskId, int userId);

    /**
     * 获取配置更新策略变更详情
     * @author bawy
     * @date 2018/8/27 15:00
     * @param taskDetailId 任务详情标识
     * @param userId 用户标识（当前登录用户）
     * @return 策略变更详情
     */
    StrategyChangeDetailVO getStrategyChangeDetail(int taskDetailId, int userId);

    /**
     * 获取配置内容变更详情
     * @author bawy
     * @date 2018/8/27 17:12
     * @param taskDetailId 任务详情标识
     * @param userId 用户标识（当前登录用户）
     * @return 配置内容变更详情
     */
    ConfigChangeDetailVO getConfigChangeDetail(int taskDetailId, int userId);

    /**
     * 根据用户查询已经通过的任务数量
     * @author oulc
     * @date 18-8-28 下午2:57
     * @param userId 用户主键
     * @return 通过的任务数量
     */
    long getPassTaskCount(int userId);

    long getAuditTaskCount(int userId);

    long getRollBackTaskCount(int userId);

    /**
     *  任务推送日期和推送实例查询
     * @author Erick
     * @date 2018/09/03
     * @param
     * */
    TaskTimeAndInsVO getTaskTimeAndIns(int taskId);
}
