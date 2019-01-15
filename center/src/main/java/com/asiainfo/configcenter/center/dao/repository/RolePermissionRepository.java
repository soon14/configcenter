package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcRolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限服务请求地址数据库访问接口
 * Created by bawy on 2018/7/3.
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<CcRolePermissionEntity, Integer> {

    List<CcRolePermissionEntity> findByRoleIdAndStatus(int roleId, byte status);

    CcRolePermissionEntity findByRoleIdAndPermissionIdAndStatus(int roleId , int permissionId,byte status);

}
