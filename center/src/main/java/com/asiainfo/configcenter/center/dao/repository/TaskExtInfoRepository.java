package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcTaskExtInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务扩展信息表数据库访问接口
 * Created by bawy on 2018/8/1 20:37.
 */
@Resource
public interface TaskExtInfoRepository extends JpaRepository<CcTaskExtInfoEntity, Integer> {
    CcTaskExtInfoEntity findByTaskIdAndExtInfoKeyAndStatus(int taskId,String extInfoKey,byte status);

    List<CcTaskExtInfoEntity> findByTaskIdAndStatus(int taskId, byte status);
}
