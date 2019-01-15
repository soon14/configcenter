package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.RoleRepository;
import com.asiainfo.configcenter.center.entity.CcRoleEntity;
import com.asiainfo.configcenter.center.service.interfaces.IRoleSV;
import com.asiainfo.configcenter.center.vo.system.RoleVO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务实现类
 * Created by bawy on 2018/7/27 13:49.
 */
@Service
public class RoleSVImpl implements IRoleSV {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private HashOperations<String, String, List<RoleVO>> hashOperations;

    @Override
    public List<RoleVO> getAllRoleByRoleType(byte type) {
        List<RoleVO> roles = hashOperations.get(ProjectConstants.REDIS_ROLE_KEY, type+"");
        if (roles == null){
            roles = new ArrayList<>();
            List<CcRoleEntity> roleEntities = roleRepository.findByRoleTypeAndStatus(type, ProjectConstants.STATUS_VALID);
            if (roleEntities!=null) {
                for (CcRoleEntity roleEntity : roleEntities) {
                    RoleVO Role = new RoleVO();
                    Role.setRoleId(roleEntity.getId());
                    Role.setRoleName(roleEntity.getRoleName());
                    roles.add(Role);
                }
            }
            hashOperations.put(ProjectConstants.REDIS_ROLE_KEY, type+"", roles);
        }
        return roles;
    }

    @Override
    public CcRoleEntity getRoleByIdAndRoleType(int roleId, byte roleType) {
        return roleRepository.findByIdAndRoleTypeAndStatus(roleId, roleType, ProjectConstants.STATUS_VALID);
    }

    @Override
    public void cleanCache() {
        hashOperations.getOperations().delete(ProjectConstants.REDIS_ROLE_KEY);
    }

    @Override
    public int getLevelOneRoleIdByType(byte roleType) {
        CcRoleEntity roleEntity = roleRepository.findByRoleTypeAndRoleLevelAndStatus(roleType, (byte)1, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(roleEntity, ResultCodeEnum.DATA_ERROR, "角色类型为"+roleType+"的所有角色中不存在级别为一的角色");
        return roleEntity.getId();
    }


}
