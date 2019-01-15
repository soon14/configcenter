package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcMenuEntity;
import com.asiainfo.configcenter.center.entity.CcRolePermissionEntity;
import com.asiainfo.configcenter.center.entity.CcServiceUrlEntity;

import java.util.List;
import java.util.Map;

/**
 * 权限服务接口类型
 * Created by bawy on 2018/7/3.
 */
public interface IPermissionSV {

    /**
     * 获取所有不需要验证是否已经登陆的服务地址
     * @author bawy
     * @date 2018/7/3 16:56
     * @return 符合条件的服务地址列表
     */
    List<String> getUnInterceptUrls();

    /**
     * 获取菜单权限集合 key为权限标识，value为权限包含的菜单列表
     * @author bawy
     * @date 2018/7/16 18:02
     * @return 所有权限对应的菜单集合
     */
    Map<Integer, List<CcMenuEntity>> getMenuPermission();

    /**
     * 根据角色ID获取角色对应的所有权限
     * @author bawy
     * @date 2018/7/16 22:10
     * @param roleId 角色标识
     * @return 角色对应的所有权限
     */
    List<CcRolePermissionEntity> getRolePermissionByRoleId(int roleId);

    /**
     * 根据角色主键和权限主键查询关系
     * @param roleId 角色主键
     * @param permissionId 权限主键
     * @return 角色和权限关系
     * @author oulc
     * @date 2018/7/24 14:07
     */
    CcRolePermissionEntity getRolePermissionByRoleIdAndPermissionId(int roleId , int permissionId);

    /**
     * 获取服务地址权限集合，key为权限标识，value为服务地址集合
     * @author bawy
     * @date 2018/7/24 22:01
     * @return 服务地址列表
     */
    Map<Integer, List<CcServiceUrlEntity>> getServiceUrlPermission();

}
