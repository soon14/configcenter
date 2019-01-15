package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskOperRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作记录表数据库访问接口
 * Created by bawy on 2018/8/1 21:18.
 */
@Repository
public interface TaskOperRecordRepository extends JpaRepository<CcTaskOperRecordEntity, Integer> {

    CcTaskOperRecordEntity findByTaskIdAndStepAndStatus(int taskId, byte step, byte status);

    CcTaskOperRecordEntity findByTaskIdAndStepAndOperatorAndStatus(int taskId, byte step, int operator, byte status);

    List<CcTaskOperRecordEntity> findByTaskIdAndOperatorAndStatus(int taskId, int operator, byte status);

}
