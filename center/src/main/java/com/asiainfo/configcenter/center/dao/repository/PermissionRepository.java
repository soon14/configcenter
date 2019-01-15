package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限信息数据库访问接口
 * Created by bawy on 2018/7/3.
 */
@Repository
public interface PermissionRepository extends JpaRepository<CcPermissionEntity, Integer> {

    List<CcPermissionEntity> findByPermissionTypeAndStatus(byte permissionType, byte status);

}
