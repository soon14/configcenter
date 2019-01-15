package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 配置项数据库访问接口
 * Created by oulc on 2018/7/25.
 */
@Repository
public interface ConfigItemRepository extends JpaRepository<CcConfigItemEntity, Integer>, JpaSpecificationExecutor<CcConfigItemEntity> {


    List<CcConfigItemEntity> findByAppEnvIdAndStatus(int appEnvId,byte status);

    CcConfigItemEntity findByAppEnvIdAndItemKeyAndStatus(int appEnvId, String itemKey, byte status);

    CcConfigItemEntity findByAppEnvIdAndIdAndStatus(int appEnvId, int id, byte status);

    List<CcConfigItemEntity> findByAppEnvIdAndIdInAndStatus(int appEnvId, List<Integer> id, byte status);

    List<CcConfigItemEntity> findByStrategyIdAndStatus(int strategyId,byte status);


}
