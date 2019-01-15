package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户信息数据库访问接口
 * Created by bawy on 2018/7/3.
 */
@Repository
public interface UserRepository extends JpaRepository<CcUserEntity, Integer>, JpaSpecificationExecutor<CcUserEntity> {

    CcUserEntity findByUsernameAndPassword(String username, String password);

    CcUserEntity findByUsernameAndStatusNot(String username, byte status);

    CcUserEntity findByEmailAndStatusNot(String email, byte status);

    CcUserEntity findByIdAndStatus(int id , byte status);

    CcUserEntity findByUsernameAndEmailAndStatus(String username, String email, byte status);

    @Query("update CcUserEntity set password=:password,updateTime=:updateTime where id=:userId")
    @Modifying
    void updatePassword( @Param("userId") int userId, @Param("password") String password,  @Param("updateTime") Timestamp updateTime);

    @Query(value = "select a from CcUserEntity a,CcAppUserRelEntity b,CcRoleEntity c where a.id = b.userId and b.appId = :appId and b.roleId = c.id and c.roleLevel < 3 and a.status = 1 and b.status = 1")
    List<CcUserEntity> findAppManager(@Param(value = "appId") int appId);

    @Query("select a from CcUserEntity a,CcOrgUserRelEntity b where a.id=b.userId and b.orgId=:orgId and a.status=:status and b.status=:status")
    List<CcUserEntity> findUserByOrgId(@Param("orgId") int orgId, @Param("status") byte status);

    @Query("select a from CcUserEntity a,CcOrgUserRelEntity b where a.id=b.userId and b.orgId=:orgId and b.roleId=:roleId and a.status=:status and b.status=:status")
    List<CcUserEntity> findUserByOrgIdAndRoleId(@Param("orgId") int orgId, @Param("roleId") int roleId, @Param("status") byte status);
}
