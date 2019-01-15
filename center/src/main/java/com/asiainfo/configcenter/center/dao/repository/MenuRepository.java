package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单数据数据库访问接口
 * Created by bawy on 2018/7/3.
 */
@Repository
public interface MenuRepository extends JpaRepository<CcMenuEntity, Integer> {

    /**
     * 根据权限标识获取该权限对应的所有菜单列表
     * @author bawy
     * @date 2018/7/16 23:21
     * @param permissionId 权限标识
     * @return 角色列表
     */
    @Query("select a from CcMenuEntity a,CcPermissionEntityRelEntity b where a.id = b.entityId and b.permissionId=:permissionId and a.status=1 and b.status=1")
    List<CcMenuEntity> findByPermissionId(@Param("permissionId") int permissionId);

}
