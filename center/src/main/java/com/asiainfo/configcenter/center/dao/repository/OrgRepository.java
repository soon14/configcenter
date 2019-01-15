package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织数据库访问接口
 * Created by bawy on 2018/8/16 20:09.
 */
@Repository
public interface OrgRepository extends JpaRepository<CcOrgEntity, Integer> {

    List<CcOrgEntity> findByLevelAndStatus(byte level, byte status);

    List<CcOrgEntity> findByStatusOrderByLevelAsc(byte status);

    CcOrgEntity findByIdAndStatus(int id, byte status);

    List<CcOrgEntity> findByParentIdAndStatus(int parentId, byte status);

    List<CcOrgEntity> findByLeaderAndStatus(int userId,byte status);
    CcOrgEntity findByIdAndLeaderAndStatus(int id, int leader, byte status);

    @Query("select a from CcOrgEntity a,CcOrgEntity b where a.id=b.parentId and b.id=:orgId and a.leader=:leader and a.status=:status and b.status=:status")
    CcOrgEntity findParentByOrgIdAndLeaderAndStatus(@Param("orgId") int orgId, @Param("leader") int leader, @Param("status") byte status);

    @Query("select a from CcOrgEntity a,CcOrgUserRelEntity b where a.id=b.orgId and b.userId=:userId and a.status=:status and b.status=:status")
    CcOrgEntity findParentByUserIdAndStatus(@Param("userId") int userId, @Param("status") byte status);

}
