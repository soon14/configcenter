package com.asiainfo.configcenter.center.common;

import com.asiainfo.configcenter.center.entity.CcMenuEntity;
import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import com.asiainfo.configcenter.center.entity.CcRolePermissionEntity;
import com.asiainfo.configcenter.center.entity.CcServiceUrlEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOrgSV;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionSV;
import com.asiainfo.configcenter.center.service.interfaces.IRoleSV;
import com.asiainfo.configcenter.center.vo.org.OrgTreeVO;
import com.asiainfo.configcenter.center.vo.system.MenuVO;
import com.asiainfo.configcenter.center.vo.system.RoleVO;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限公共类
 * Created by bawy on 2018/7/16 21:16.
 */
@Component
public class PermissionCommon {


    private static IPermissionSV permissionSV;

    private static IRoleSV roleSV;

    private static IOrgSV orgSV;

    private static HashOperations<String, String, Object> hashOperations;

    private static ValueOperations<String, Object> valueOperations;

    private static Logger logger = Logger.getLogger(PermissionCommon.class);


    @Resource
    public void setPermissionSV(IPermissionSV permissionSV){
        PermissionCommon.permissionSV = permissionSV;
    }

    @Resource
    public void setRoleSV(IRoleSV roleSV){
        PermissionCommon.roleSV = roleSV;
    }

    @Resource
    public void setOrgSV(IOrgSV orgSV) {
        PermissionCommon.orgSV = orgSV;
    }

    @Resource
    public void setHashOperations(HashOperations<String, String, Object> hashOperations) {
        PermissionCommon.hashOperations = hashOperations;
    }

    @Resource
    public void setValueOperations(ValueOperations<String, Object> valueOperations) {
        PermissionCommon.valueOperations = valueOperations;
    }


    /**
     * 权限公共类初始化
     * @author bawy
     * @date 2018/7/16 21:18
     */
    @PostConstruct
    public static void init(){
        setMenuTreeForSysRole();
        setServicePermissionForAppRole();
        setOrgTree();
    }

    /**
     * 根据角色ID获取菜单树
     * @author bawy
     * @date 2018/7/16 22:06
     * @param roleId 角色标识
     * @return 菜单树
     */
    public static MenuVO getMenuTreeByRoleId(int roleId){
        return (MenuVO) hashOperations.get(ProjectConstants.REDIS_SYSTEM_ROLE_MENU_KEY, roleId+"");
    }

    /**
     * 根据应用角色获取角色对应的所有服务请求地址
     * @author bawy
     * @date 2018/7/24 22:34
     * @param roleId 角色标识
     * @return 该角色拥有权限的所有服务请求地址
     */
    public static List<String> getAllServiceUrlByAppRoleId(int roleId){
        return (List<String>) hashOperations.get(ProjectConstants.REDIS_APP_ROLE_SERVICE_KEY, roleId+"");
    }

    /**
     * 根据系统角色获取角色对应的所有服务请求地址
     * @param roleId 角色标识
     * @return 该角色用户权限的所有服务请求地址
     */
    public static List<String> getAllServiceUrlBySysRoleId(int roleId){
        return (List<String>) hashOperations.get(ProjectConstants.REDIS_SYS_ROLE_SERVICE_KEY, roleId+"");
    }

    /**
     * 父级组织标识获取组织树
     * @param orgId 组织标识
     * @return 组织树
     */
    public static OrgTreeVO getOrgTreeByOrgId(int orgId){
        OrgTreeVO rootOrgTree = (OrgTreeVO) hashOperations.get(ProjectConstants.REDIS_ORG_TREE_KEY, "0");
        if (orgId == 0){
            return rootOrgTree;
        }else {
            Map<Integer, List<Integer>> allOrgTreePath = (Map<Integer, List<Integer>>) valueOperations.get(ProjectConstants.REDIS_ORG_TREE_PATH_KEY);
            List<Integer> orgPath = allOrgTreePath.get(orgId);
            return getOrgTreeByPath(rootOrgTree, orgPath);
        }
    }

    /**
     * 根据树路径获取组织树中指定组织
     * @author bawy
     * @date 2018/9/6 14:14
     * @param rootOrg 根组织
     * @param orgPath 组织树路径
     * @return 对应组织
     */
    private static OrgTreeVO getOrgTreeByPath(OrgTreeVO rootOrg, List<Integer> orgPath) {
        if (orgPath == null){
            return null;
        }else {
            OrgTreeVO childOrgTree = rootOrg;
            for (int index: orgPath){
                if (index < childOrgTree.getChildren().size()){
                    childOrgTree = childOrgTree.getChildren().get(index);
                }else{
                    return null;
                }
            }
            return childOrgTree;
        }
    }

    /**
     * 设置应用角色对应服务权限缓存
     * @author bawy
     * @date 2018/7/27 14:11
     */
    private static void setServicePermissionForAppRole(){
        setServicePermissionForRole(ProjectConstants.ROLE_TYPE_APP, ProjectConstants.REDIS_APP_ROLE_SERVICE_KEY);
    }

    /**
     * 设置系统角色对应服务权限缓存
     * @author bawy
     * @date 2018/7/30 22:56
     */
    private static void setServicePermissionForSysRole(){
        setServicePermissionForRole(ProjectConstants.ROLE_TYPE_SYSTEM, ProjectConstants.REDIS_SYS_ROLE_SERVICE_KEY);
    }

    /**
     * 设置角色对应服务权限缓存
     * @author bawy
     * @date 2018/7/30 23:04
     * @param roleType 角色类型
     * @param redisKey 缓存key
     */
    private static void setServicePermissionForRole(byte roleType, String redisKey){
        List<RoleVO> roles = roleSV.getAllRoleByRoleType(roleType);
        Map<Integer, List<CcServiceUrlEntity>> serviceUrlPermissionMap = permissionSV.getServiceUrlPermission();
        for (RoleVO role:roles) {
            int roleId = role.getRoleId();
            List<String> serviceUrls = new ArrayList<>();
            List<CcRolePermissionEntity> rolePermissionEntities = permissionSV.getRolePermissionByRoleId(roleId);
            if (rolePermissionEntities != null) {
                for (CcRolePermissionEntity rolePermissionEntity : rolePermissionEntities) {
                    List<CcServiceUrlEntity> serviceUrlEntities = serviceUrlPermissionMap.get(rolePermissionEntity.getPermissionId());
                    if (serviceUrlEntities != null) {
                        for (CcServiceUrlEntity serviceUrlEntity : serviceUrlEntities) {
                            serviceUrls.add(serviceUrlEntity.getUrl());
                        }
                    }
                }
            }
            hashOperations.put(redisKey, roleId+"", serviceUrls);
        }
    }

    /**
     * 设置系统角色对应的菜单树
     * @author bawy
     * @date 2018/7/27 14:18
     */
    private static void setMenuTreeForSysRole(){
        List<RoleVO> roles = roleSV.getAllRoleByRoleType(ProjectConstants.ROLE_TYPE_SYSTEM);
        Map<Integer, List<CcMenuEntity>> menuPermissionMap = permissionSV.getMenuPermission();
        for (RoleVO role:roles) {
            int roleId = role.getRoleId();
            Map<Integer, MenuVO> menuVOMap = new HashMap<>();
            MenuVO menuVO = new MenuVO();
            menuVO.setMenuName("根菜单");
            menuVO.setMenuLink("/");
            menuVO.setMenuLevel((byte) 0);
            menuVOMap.put(ProjectConstants.ROOT_MENU_ID, menuVO);
            List<CcRolePermissionEntity> rolePermissionEntities = permissionSV.getRolePermissionByRoleId(roleId);
            if (rolePermissionEntities != null) {
                for (CcRolePermissionEntity rolePermissionEntity : rolePermissionEntities) {
                    List<CcMenuEntity> menuEntities = menuPermissionMap.get(rolePermissionEntity.getPermissionId());
                    if (menuEntities != null) {
                        for (CcMenuEntity menuEntity : menuEntities) {
                            int menuId = menuEntity.getId();
                            MenuVO childMenu = menuVOMap.get(menuId);
                            if (childMenu == null) {
                                childMenu = new MenuVO();
                            }
                            childMenu.setMenuId(menuEntity.getId());
                            childMenu.setMenuName(menuEntity.getMenuName());
                            childMenu.setMenuLink(menuEntity.getMenuLink());
                            childMenu.setMenuIcon(menuEntity.getMenuIcon());
                            childMenu.setMenuLevel(menuEntity.getMenuLevel());
                            childMenu.setDescription(menuEntity.getDescription());
                            boolean isLeaf = menuEntity.getIsLeaf() == 1;
                            int parentId = menuEntity.getParentId();
                            childMenu.setLeaf(isLeaf);
                            if (!isLeaf) {
                                menuVOMap.put(menuId, childMenu);
                            }
                            MenuVO parentMenu = menuVOMap.get(parentId);
                            if (parentMenu == null) {
                                parentMenu = new MenuVO();
                                menuVOMap.put(parentId, parentMenu);
                            }
                            parentMenu.addChildMenu(childMenu);
                        }
                    }
                }
            }
            hashOperations.put(ProjectConstants.REDIS_SYSTEM_ROLE_MENU_KEY, roleId+"", menuVO);
        }
    }

    /**
     * 设置组织结构树
     * @author bawy
     * @date 2018/7/27 14:18
     */
    private static void setOrgTree(){
        OrgTreeVO rootOrg = new OrgTreeVO();
        rootOrg.setId(0);
        Map<Integer, OrgTreeVO> orgMap = new HashMap<>();
        Map<Integer, List<Integer>> allOrgTreePath = new HashMap<>();
        orgMap.put(0, rootOrg);
        List<CcOrgEntity> orgEntities = orgSV.getAllOrg();
        if (orgEntities!=null){
            for (CcOrgEntity orgEntity:orgEntities) {
                OrgTreeVO orgTreeVO = new OrgTreeVO();
                orgTreeVO.setId(orgEntity.getId());
                orgTreeVO.setName(orgEntity.getName());
                orgTreeVO.setLevel(orgEntity.getLevel());
                orgTreeVO.setLeader(orgEntity.getLeader());
                orgMap.put(orgEntity.getId(), orgTreeVO);
                OrgTreeVO parentOrg = orgMap.get(orgEntity.getParentId());
                if(parentOrg != null){
                    parentOrg.addChild(orgTreeVO);
                    List<Integer> parentTreePath = allOrgTreePath.get(parentOrg.getId());
                    List<Integer> orgTreePath = null;
                    if (parentTreePath == null){
                        orgTreePath = new ArrayList<>();
                    }else{
                        orgTreePath = new ArrayList<>(parentTreePath);
                    }
                    orgTreePath.add(parentOrg.getChildren().size()-1);
                    allOrgTreePath.put(orgTreeVO.getId(), orgTreePath);
                }
            }
        }
        hashOperations.put(ProjectConstants.REDIS_ORG_TREE_KEY, "0", rootOrg);
        valueOperations.set(ProjectConstants.REDIS_ORG_TREE_PATH_KEY, allOrgTreePath);
        /*List<OrgTreeVO> levelOneOrgs = rootOrg.getChildren();
        //所有一级组织添加缓存
        if (levelOneOrgs!=null){
            for (OrgTreeVO orgTreeVO: levelOneOrgs){
                hashOperations.put(ProjectConstants.REDIS_ORG_TREE_KEY, orgTreeVO.getId()+"", orgTreeVO);
            }
        }*/
    }

    /**
     * 更新组织树缓存
     * @author bawy
     * @date 2018/9/6 13:48
     * @param changeType 组织变更类型
     * @param orgEntity 组织实体
     */
    public static void updateOrgTree(byte changeType, CcOrgEntity orgEntity){
        OrgTreeVO rootOrg = (OrgTreeVO) hashOperations.get(ProjectConstants.REDIS_ORG_TREE_KEY, "0");
        Map<Integer, List<Integer>> allOrgTreePath = (Map<Integer, List<Integer>>) valueOperations.get(ProjectConstants.REDIS_ORG_TREE_PATH_KEY);
        int parentId = orgEntity.getParentId();
        OrgTreeVO parentOrgTree = rootOrg;
        OrgTreeVO orgTreeVO;
        List<Integer> orgTreePath;
        try {
            switch (changeType){
                case ProjectConstants.ORG_CHANGE_TYPE_ADD:
                    orgTreeVO = new OrgTreeVO();
                    orgTreeVO.setId(orgEntity.getId());
                    orgTreeVO.setLevel(orgEntity.getLevel());
                    orgTreeVO.setName(orgEntity.getName());
                    orgTreeVO.setLeader(orgEntity.getLeader());
                    if (parentId == 0){
                        orgTreePath = new ArrayList<>();
                    }else {
                        List<Integer> parentOrgTreePath = allOrgTreePath.get(parentId);
                        parentOrgTree = getOrgTreeByPath(parentOrgTree, parentOrgTreePath);
                        orgTreePath = new ArrayList<>(parentOrgTreePath);
                    }
                    parentOrgTree.addChild(orgTreeVO);
                    orgTreePath.add(parentOrgTree.getChildren().size()-1);
                    allOrgTreePath.put(orgEntity.getId(), orgTreePath);
                    break;
                case ProjectConstants.CONFIG_CHANGE_TYPE_MOD:
                    orgTreePath = allOrgTreePath.get(orgEntity.getId());
                    orgTreeVO = getOrgTreeByPath(parentOrgTree, orgTreePath);
                    orgTreeVO.setName(orgEntity.getName());
                    orgTreeVO.setLeader(orgEntity.getLeader());
                    break;
                case ProjectConstants.CONFIG_CHANGE_TYPE_DEL:
                    if (parentId != 0){
                        List<Integer> parentOrgTreePath = allOrgTreePath.get(parentId);
                        parentOrgTree = getOrgTreeByPath(parentOrgTree, parentOrgTreePath);
                    }
                    orgTreePath = allOrgTreePath.get(orgEntity.getId());
                    int index = orgTreePath.get(orgTreePath.size()-1);
                    parentOrgTree.getChildren().remove(index);
                    allOrgTreePath.remove(orgEntity.getId());
                    break;
                default:
            }
            hashOperations.put(ProjectConstants.REDIS_ORG_TREE_KEY, "0", rootOrg);
            valueOperations.set(ProjectConstants.REDIS_ORG_TREE_PATH_KEY, allOrgTreePath);
        }catch (Exception e){
            logger.error(ErrorInfo.errorInfo(e));
            logger.error("更新缓存失败，操作类型："+changeType+"组织标识："+orgEntity.getId());
        }
    }

    /**
     * 清理缓存
     * @author bawy
     * @date 2018/7/27 14:28
     */
    public static void cleanCache(){
        hashOperations.getOperations().delete(ProjectConstants.REDIS_SYSTEM_ROLE_MENU_KEY);
        logger.info("系统角色菜单缓存清理完成！key：" + ProjectConstants.REDIS_SYSTEM_ROLE_MENU_KEY);
        hashOperations.getOperations().delete(ProjectConstants.REDIS_APP_ROLE_SERVICE_KEY);
        logger.info("应用角色服务权限缓存清理完成！key：" + ProjectConstants.REDIS_APP_ROLE_SERVICE_KEY);
        hashOperations.getOperations().delete(ProjectConstants.REDIS_SYS_ROLE_SERVICE_KEY);
        logger.info("系统角色服务权限缓存清理完成！key：" + ProjectConstants.REDIS_SYS_ROLE_SERVICE_KEY);
        hashOperations.getOperations().delete(ProjectConstants.REDIS_ORG_TREE_KEY);
        logger.info("组织结构缓存清理完成！key：" + ProjectConstants.REDIS_ORG_TREE_KEY);
        valueOperations.getOperations().delete(ProjectConstants.REDIS_ORG_TREE_PATH_KEY);
        logger.info("组织树索引缓存清理完成！key：" + ProjectConstants.REDIS_ORG_TREE_PATH_KEY);
        setMenuTreeForSysRole();
        setServicePermissionForAppRole();
        setServicePermissionForSysRole();
        setOrgTree();
    }

}
