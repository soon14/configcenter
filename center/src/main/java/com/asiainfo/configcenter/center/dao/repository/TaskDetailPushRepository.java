package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskDetailPushEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务变更详情（配置推送）数据库访问接口
 * Created by bawy on 2018/8/2.
 */
@Repository
public interface TaskDetailPushRepository extends JpaRepository<CcTaskDetailPushEntity, Integer>,JpaSpecificationExecutor<CcTaskDetailPushEntity> {

    List<CcTaskDetailPushEntity> findByTaskIdAndStatusOrderByIdAsc(int taskId, byte status);

    CcTaskDetailPushEntity findByIdAndTaskIdAndCreatorAndStatus(int id, int taskId, int creator, byte status);

    CcTaskDetailPushEntity findByTaskIdAndConfigIdAndStatus(int taskId, int configId, byte status);

    CcTaskDetailPushEntity findByidAndTaskIdAndStatus(int id, int taskId, byte status);
}
