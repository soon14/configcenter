package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 角色数据库访问接口
 * Created by bawy on 2018/7/27 13:50.
 */
public interface RoleRepository extends JpaRepository<CcRoleEntity, Integer> {


    List<CcRoleEntity> findByRoleTypeAndStatus(byte roleType, byte status);

    CcRoleEntity findByIdAndRoleTypeAndStatus(int id, byte roleType, byte status);

    CcRoleEntity findByRoleTypeAndRoleLevelAndStatus(byte roleType, byte roleLevel, byte status);

}
