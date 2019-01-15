package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 配置变更任务详情数据库访问接口
 * Created by bawy on 2018/8/1 16:41.
 */
@Repository
public interface TaskDetailConfigRepository extends JpaRepository<CcTaskDetailConfigEntity, Integer>, JpaSpecificationExecutor<CcTaskDetailConfigEntity> {

    CcTaskDetailConfigEntity findByTaskIdAndConfigIdAndStatus(int taskId, int configId, byte status);

    CcTaskDetailConfigEntity findByTaskIdAndConfigTypeAndConfigNameAndStatus(int taskId, byte configType, String configName, byte status);

    List<CcTaskDetailConfigEntity> findByTaskIdAndStatusOrderByIdAsc(int taskId, byte status);

    CcTaskDetailConfigEntity findByIdAndTaskIdAndCreatorAndStatus(int id, int taskId, int creator, byte status);

    CcTaskDetailConfigEntity findByIdAndStatus(int id, byte status);

    /**
     * 查询当前环境中是否存在待审核任务对指定配置进行了变更
     * @author bawy
     * @date 2018/8/8 11:30
     * @param envId 环境标识
     * @param configName 配置名称
     * @param configType 配置类型
     * @param taskState 任务状态
     * @param status 数据状态
     * @return 符合条件的任务详情数据
     */
    @Query(value = "select b from CcTaskEntity a,CcTaskDetailConfigEntity b where a.id=b.taskId and a.appEnvId=:envId and a.taskState=:taskState and b.configType=:configType and b.configName=:configName and a.status=:status and b.status=:status")
    CcTaskDetailConfigEntity findByEnvIdAndConfigNameAndConfigType(@Param("envId") int envId, @Param("configName") String configName, @Param("configType") byte configType, @Param("taskState") byte taskState, @Param("status") byte status);

    /**
     * 根据环境标识和详情标识获取配置变更任务信息
     * @author bawy
     * @date 2018/8/9 16:30
     * @param envId 环境标识
     * @param taskDetailId 任务详情标识
     * @param status 状态
     * @return 符合条件的任务详情
     */
    @Query(value = "select b from CcTaskEntity a,CcTaskDetailConfigEntity b where a.id=b.taskId and a.appEnvId=:envId and b.id=:taskDetailId and a.status=:status and b.status=:status")
    CcTaskDetailConfigEntity findByEnvIdAndDetailId(@Param("envId") int envId, @Param("taskDetailId") int taskDetailId, @Param("status") byte status);


}
