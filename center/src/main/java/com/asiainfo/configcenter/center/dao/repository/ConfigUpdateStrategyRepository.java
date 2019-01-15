package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcConfigUpdateStrategyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by oulc on 2018/8/9.
 * 配置更新策略jpa
 */
@Repository
public interface ConfigUpdateStrategyRepository extends JpaRepository<CcConfigUpdateStrategyEntity, Integer> {
    CcConfigUpdateStrategyEntity findByIdAndAppIdAndStatus(int id,int appId,byte status);
}
