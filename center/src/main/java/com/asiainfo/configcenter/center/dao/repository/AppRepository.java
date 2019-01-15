package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcAppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 数据库应用表访问接口
 * Created by bawy on 2018/7/24 13:49.
 */
@Repository
public interface AppRepository extends JpaRepository<CcAppEntity, Integer>, JpaSpecificationExecutor<CcAppEntity> {
    CcAppEntity findByAppNameAndStatus(String appName, byte status);

    @Query(value = "select a from CcAppEntity a,CcAppUserRelEntity b where a.id = b.appId and b.userId = :userId and b.roleId = :roleId and a.status = :status and b.status = :status")
    List<CcAppEntity> findByUserIdAndRoleId(@Param("userId")int userId,@Param("roleId")int roleId,@Param("status")byte status);

    /**
     * 获取用户加入的所有App
     * @author bawy
     * @param appName 应用名称
     * @param userId 用户标识
     * @param status app状态
     * @return App列表
     */
    @Query(value = "select a from CcAppEntity a,CcAppUserRelEntity b,CcUserEntity c where a.id=b.appId and b.userId=c.id and b.userId=:userId and a.appName like :appName and a.status=:status and b.status=:status order by a.id asc")
    List<CcAppEntity> getMyApps(@Param("appName") String appName, @Param("userId") int userId, @Param("status") byte status);

    List<CcAppEntity> findAllByStatusAndAppNameLikeOrderByIdAsc(byte status, String appName);

    CcAppEntity findByIdAndStatus(int id , byte status);

    /**
     * 更新应用状态
     * @param appId 应用标识
     * @param userId 用户标识
     * @param updateTime 更新时间
     * @param status 状态
     */
    @Query(value = "update CcAppEntity a set a.status=:status,a.modifier=:userId,a.updateTime=:updateTime where a.id=:appId")
    @Modifying
    void updateAppStatusByAppId(@Param("appId") int appId, @Param("userId") int userId, @Param("updateTime") Timestamp updateTime, @Param("status") byte status);


    long countByStatus(@Param("status") byte status);



}
