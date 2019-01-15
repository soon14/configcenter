package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用环境数据库访问接口
 * Created by oulc on 2018/7/24.
 */
@Repository
public interface AppEnvRepository extends JpaRepository<CcAppEnvEntity, Integer>, JpaSpecificationExecutor<CcAppEnvEntity>, CrudRepository<CcAppEnvEntity, Integer> {
    CcAppEnvEntity findByIdAndStatus(int id , byte status);

    List<CcAppEnvEntity> findByAppIdAndEnvNameAndStatus(int appId,String envName , byte status);

    @Query(value = "select a from CcAppEnvEntity a where a.appId= :appId and a.status = :status")
    List<CcAppEnvEntity> findByAppIdAndStatus(@Param(value = "appId")int appId,@Param(value = "status")byte status);

    int countByAppIdAndStatus(int appId, byte status);



}
