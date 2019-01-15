package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcConfigItemHisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 配置项历史表数据库访问接口
 * Created by bawy on 2018/8/1 11:05.
 */
public interface ConfigItemHisRepository extends JpaRepository<CcConfigItemHisEntity, Integer>{

    @Query(value = "select a from CcConfigItemHisEntity a,CcConfigItemEntity b where a.itemId=b.id and a.id=:hisId and b.appEnvId=:envId and a.status=:status and b.status=:status")
    CcConfigItemHisEntity findByEnvIdAndHisId(@Param("envId") int envId, @Param("hisId") int hisId, @Param("status") byte status);

}
