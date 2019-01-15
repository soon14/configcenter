package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务表数据库访问接口
 * Created by bawy on 2018/8/1 16:24.
 */
@Repository
public interface TaskRepository extends JpaRepository<CcTaskEntity, Integer> {

    CcTaskEntity findByAppEnvIdAndCreatorAndTaskTypeAndTaskStateAndStatus(int appEnvId, int creator, byte taskType, byte taskState, byte status);

    CcTaskEntity findByAppEnvIdAndTaskTypeAndTaskStateAndStatus(int appEnvId, byte taskType, byte taskState, byte status);

    CcTaskEntity findByIdAndCreatorAndStatus(int id, int creator, byte status);

    CcTaskEntity findByIdAndStatus(int id, byte status);

    List<CcTaskEntity> findByCreatorAndStatus(int creator, byte status);

    List<CcTaskEntity> findByCreatorAndTaskTypeNotInAndStatus(int creator,byte []taskTypes,byte status);

    List<CcTaskEntity> findByAppEnvIdAndTaskStateAndStatus(int appEnvId, byte taskState, byte status);

    /**
     * 查询与指定操作员有关的任务
     * @author bawy
     * @param taskId 任务标识
     * @param operator 操作员
     * @param taskState 任务状态
     * @param operationState 操作状态
     * @param status 数据状态
     * @return 符合条件的任务
     */
    @Query(value = "select a from CcTaskEntity a,CcTaskOperRecordEntity b where a.id=b.taskId and a.taskState=:taskState and b.operationState=:operationState " +
            "and a.id=:taskId and b.operator=:operator " +
            "and a.status=:status and b.status=:status")
    CcTaskEntity findTaskWithOperator(@Param("taskId") int taskId, @Param("operator") int operator, @Param("taskState") byte taskState, @Param("operationState") byte operationState, @Param("status") byte status);

}
