package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.MenuRepository;
import com.asiainfo.configcenter.center.dao.repository.PermissionRepository;
import com.asiainfo.configcenter.center.dao.repository.RolePermissionRepository;
import com.asiainfo.configcenter.center.dao.repository.ServiceUrlRepository;
import com.asiainfo.configcenter.center.entity.CcMenuEntity;
import com.asiainfo.configcenter.center.entity.CcPermissionEntity;
import com.asiainfo.configcenter.center.entity.CcRolePermissionEntity;
import com.asiainfo.configcenter.center.entity.CcServiceUrlEntity;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionSV;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限服务实现类
 * Created by bawy on 2018/7/3.
 */
@Service
public class PermissionSVImpl implements IPermissionSV{

    @Resource
    private ServiceUrlRepository serviceUrlRepository;

    @Resource
    private PermissionRepository permissionRepository;

    @Resource
    private MenuRepository menuRepository;

    @Resource
    private RolePermissionRepository rolePermissionRepository;


    @Override
    public List<String> getUnInterceptUrls() {
        List<String> unInterceptUrls = new ArrayList<>();
        List<CcServiceUrlEntity> ccServiceUrlEntities = serviceUrlRepository.findByServiceTypeAndStatus(ProjectConstants.SERVICE_TYPE_NO_LOGIN, ProjectConstants.STATUS_VALID);
        for(CcServiceUrlEntity entity:ccServiceUrlEntities){
            unInterceptUrls.add(entity.getUrl());
        }
        return unInterceptUrls;
    }

    @Override
    public Map<Integer, List<CcMenuEntity>> getMenuPermission() {
        Map<Integer, List<CcMenuEntity>> menuPermissionMap = new HashMap<>();
        List<CcPermissionEntity> permissionEntities = permissionRepository.findByPermissionTypeAndStatus(ProjectConstants.PERMISSION_TYPE_MENU, ProjectConstants.STATUS_VALID);
        if(permissionEntities != null) {
            for (CcPermissionEntity entity : permissionEntities) {
                int permissionId = entity.getId();
                List<CcMenuEntity> menuEntities = menuRepository.findByPermissionId(permissionId);
                menuPermissionMap.put(permissionId, menuEntities);
            }
        }
        return menuPermissionMap;
    }

    @Override
    public List<CcRolePermissionEntity> getRolePermissionByRoleId(int roleId) {
        return rolePermissionRepository.findByRoleIdAndStatus(roleId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public Map<Integer, List<CcServiceUrlEntity>> getServiceUrlPermission() {
        Map<Integer, List<CcServiceUrlEntity>> serviceUrlPermissionMap = new HashMap<>();
        List<CcPermissionEntity> permissionEntities = permissionRepository.findByPermissionTypeAndStatus(ProjectConstants.PERMISSION_TYPE_SERVICE, ProjectConstants.STATUS_VALID);
        if (permissionEntities != null) {
            for (CcPermissionEntity entity : permissionEntities) {
                int permissionId = entity.getId();
                List<String> serviceUrls = new ArrayList<>();
                List<CcServiceUrlEntity> serviceUrlEntities = serviceUrlRepository.findServiceUrlByPermissionId(ProjectConstants.SERVICE_TYPE_NEED_LOGIN, permissionId);
                serviceUrlPermissionMap.put(permissionId, serviceUrlEntities);
            }
        }
        return serviceUrlPermissionMap;
    }

    @Override
    public CcRolePermissionEntity getRolePermissionByRoleIdAndPermissionId(int roleId, int permissionId) {
        return rolePermissionRepository.findByRoleIdAndPermissionIdAndStatus(roleId,permissionId,ProjectConstants.STATUS_VALID);
    }
}
