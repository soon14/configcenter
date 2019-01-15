package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcRoleEntity;
import com.asiainfo.configcenter.center.vo.system.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 * Created by bawy on 2018/7/27 13:47.
 */
public interface IRoleSV {

    /**
     * 根据角色类型获取所有角色列表
     * @param type 角色类型
     * @return 角色列表
     */
    List<RoleVO> getAllRoleByRoleType(byte type);

    /**
     * 根据角色标识和角色类型获取角色信息
     * @author bawy
     * @date 2018/8/17 17:54
     * @param roleId 角色标识
     * @param roleType 角色类型
     * @return 角色信息
     */
    CcRoleEntity getRoleByIdAndRoleType(int roleId, byte roleType);

    /**
     * 清除角色缓存
     * @author bawy
     * @date 2018/8/21 11:04
     */
    void cleanCache();

    /**
     * 获取指定角色类型的最高级别角色
     * @author bawy
     * @date 2018/8/21 16:03
     * @param roleType 角色类型
     * @return 符合条件的角色Id
     */
    int getLevelOneRoleIdByType(byte roleType);
}
