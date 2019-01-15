package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcUserRoleRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系数据库访问接口
 * Created by bawy on 2018/7/16 23:16.
 */
@Repository
public interface UserRoleRelRepository extends JpaRepository<CcUserRoleRelEntity, Integer> {

    CcUserRoleRelEntity findByUserIdAndStatus(int userId, byte status);

    /**
     * 根据角色ID获取所有有效的用户角色关系
     * @author bawy
     * @date 2018/7/20 15:39
     * @param roleId 角色ID
     * @param status 状态
     * @return 所有该角色对应的用户角色关系
     */
    List<CcUserRoleRelEntity> findByRoleIdAndStatus(int roleId, byte status);

}
