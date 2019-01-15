package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcAppUserRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据库应用用户表关系访问接口
 * Created by bawy on 2018/7/24 14:59.
 */
@Repository
public interface AppUserRelRepository extends JpaRepository<CcAppUserRelEntity, Integer>{

    CcAppUserRelEntity findByAppIdAndUserIdAndStatus(int appId, int userId, byte status);

    List<CcAppUserRelEntity> findByUserIdAndRoleIdAndStatus(int userId,int roleId,byte status);

    CcAppUserRelEntity findByUserIdAndRoleIdAndAppIdAndStatus(int userId,int roleId,int appId,byte status);

    /**
     * 根据环境标识和用户标识查询应用用户关系
     * @author bawy
     * @date 2018/7/25 9:37
     * @param envId 环境标识
     * @param userId 用户标识
     * @param status 状态
     * @return 应用用户关系
     */
    @Query(value = "select a from CcAppUserRelEntity a,CcAppEnvEntity b where a.appId=b.appId and a.userId=:userId and b.id=:envId and a.status=:status and b.status=:status")
    CcAppUserRelEntity getByEnvIdAndUserId(@Param("envId") int envId, @Param("userId") int userId, @Param("status") byte status);

    /**
     * 根据配置项标识和用户标识查询应用用户关系
     * @author bawy
     * @date 2018/8/7 14:53
     * @param configItemId 配置项标识
     * @param userId 用户标识
     * @param status 状态
     * @return 应用用户关系
     */
    @Query(value = "select a from CcAppUserRelEntity a,CcAppEnvEntity b,CcConfigItemEntity c where a.appId=b.appId and b.id=c.appEnvId and a.userId=:userId and c.id=:configItemId and a.status=:status and b.status=:status and c.status=:status")
    CcAppUserRelEntity getByConfigItemIdIdAndUserId(@Param("configItemId") int configItemId, @Param("userId") int userId, @Param("status") byte status);

}
