package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskAuditStrategyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务审核策略数据库访问接口
 * Created by bawy on 2018/8/2 9:25.
 */
@Repository
public interface TaskAuditStrategyRepository extends JpaRepository<CcTaskAuditStrategyEntity, Integer> {

    List<CcTaskAuditStrategyEntity> findByAppEnvIdAndStatusOrderByStepAsc(int appEnvId, byte status);

    List<CcTaskAuditStrategyEntity> findByAppEnvIdAndStatusOrderByStepDesc(int appEnvId, byte status);

    CcTaskAuditStrategyEntity findByAppEnvIdAndOrgIdAndStatus(int appEnvId, int orgId, byte status);

    CcTaskAuditStrategyEntity findByIdAndAppEnvIdAndStatus(int id, int appEnvId, byte status);

    CcTaskAuditStrategyEntity findByAppEnvIdAndStepAndStatus(int appEnvId, byte step, byte status);

    @Query("update CcTaskAuditStrategyEntity a set a.step=a.step-1 where a.appEnvId=:envId and a.status=:status and a.step>:step")
    @Modifying
    void updateStep(@Param("envId") int envId, @Param("step") byte step, @Param("status") byte status);

}
