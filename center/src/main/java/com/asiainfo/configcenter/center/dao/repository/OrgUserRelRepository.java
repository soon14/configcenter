package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcOrgUserRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织用户关系数据库访问接口
 * Created by bawy on 2018/8/17 15:13.
 */
@Repository
public interface OrgUserRelRepository extends JpaRepository<CcOrgUserRelEntity, Integer> {

    CcOrgUserRelEntity findByUserIdAndOrgLevelAndStatus(int userId, byte orgLevel, byte status);

    CcOrgUserRelEntity findByOrgIdAndUserIdAndStatus(int orgId, int userId, byte status);

    CcOrgUserRelEntity findByUserIdAndStatus(int userId, byte status);

    CcOrgUserRelEntity findByOrgIdAndUserIdAndRoleIdAndStatus(int orgId, int userId, int roleId, byte status);

    List<CcOrgUserRelEntity> findByOrgIdAndStatus(int orgId, byte status);

}
