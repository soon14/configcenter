package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcServiceUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务请求地址数据库访问接口
 * Created by bawy on 2018/7/3.
 */
@Repository
public interface ServiceUrlRepository extends JpaRepository<CcServiceUrlEntity, Integer> {

    List<CcServiceUrlEntity> findByServiceTypeAndStatus(byte serviceType, byte status);

    @Query("select a from CcServiceUrlEntity a,CcPermissionEntityRelEntity b where a.id=b.entityId and b.permissionId=:permissionId and a.serviceType=:serviceType and a.status=1 and b.status=1")
    List<CcServiceUrlEntity> findServiceUrlByPermissionId(@Param("serviceType") byte serviceType, @Param("permissionId") int permissionId);

}
