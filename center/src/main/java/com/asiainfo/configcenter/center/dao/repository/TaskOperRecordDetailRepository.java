package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskOperRecordDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 任务操作记录详情数据库访问接口
 * Created by bawy on 2018/8/2 17:13.
 */
@Repository
public interface TaskOperRecordDetailRepository extends JpaRepository<CcTaskOperRecordDetailEntity, Integer> {


}
