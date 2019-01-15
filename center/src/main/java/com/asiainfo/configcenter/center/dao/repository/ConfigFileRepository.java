package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcConfigFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by oulc on 2018/7/25.
 */
@Repository
public interface ConfigFileRepository extends JpaRepository<CcConfigFileEntity, Integer>, JpaSpecificationExecutor<CcConfigFileEntity> {
    List<CcConfigFileEntity> findByAppEnvIdAndStatus(int appEnvId,byte status);

    CcConfigFileEntity findByAppEnvIdAndFileNameAndStatus(int appEnvId,String fileName,byte status);

    CcConfigFileEntity findByIdAndStatus(int id,byte status);

    CcConfigFileEntity findByIdAndAppEnvIdAndStatus(int id,int appEnvId,byte status);

    List<CcConfigFileEntity> findByStrategyIdAndStatus(int startegyId,byte status);
}
