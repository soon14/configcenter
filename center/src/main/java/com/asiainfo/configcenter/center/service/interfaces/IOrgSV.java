package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import com.asiainfo.configcenter.center.entity.CcOrgUserRelEntity;
import com.asiainfo.configcenter.center.vo.org.*;
import com.asiainfo.configcenter.center.vo.system.RoleVO;

import java.util.List;

/**
 * 组织服务接口类
 * Created by bawy on 2018/8/16 20:06.
 */
public interface IOrgSV {

    /**
     * 获取所有一级组织信息
     * @author bawy
     * @date 2018/8/16 20:07
     * @return 所有一级组织集合
     */
    List<CcOrgEntity> getAllLevelOneOrg();

    /**
     * 获取用户所在一级组织列表，如果是管理员用户可以获取所有以及组织列表
     * @author bawy
     * @date 2018/8/17 10:26
     * @param userId 用户标识
     * @return 以及组织列表
     */
    List<OrgTreeVO> getLevelOneOrg(int userId);

    /**
     * 获取所有组织树
     * @author bawy
     * @date 2018/8/23 18:15
     * @return 所有组织树
     */
    List<OrgTreeVO> getAllOrgTree();

    /**
     * 获取所有有效的组织
     * @author bawy
     * @date 2018/8/17 14:29
     * @return 结果
     */
    List<CcOrgEntity> getAllOrg();

    /**
     * 增加组织
     * @author bawy
     * @date 2018/8/17 16:23
     * @param addOrgReq 增加组织请求参数
     * @param creator 当前登录用户
     * @return 成功新增提示语
     */
    String addOrg(AddOrgReqVO addOrgReq, int creator);

    /**
     * 删除组织
     * @author bawy
     * @date 2018/8/17 16:24
     * @param delOrgReqVO 删除组织请求参数
     * @param modifier 当前登录用户
     * @return 成功删除提示语
     */
    String delOrg(DelOrgReqVO delOrgReqVO, int modifier);

    /**
     * 修改组织
     * @author bawy
     * @date 2018/8/17 16:24
     * @param modOrgReq 修改组织请求参数
     * @param modifier 当前登录用户
     * @return 成功修改提示语
     */
    String modOrg(ModOrgReqVO modOrgReq, int modifier);

    /**
     * 调整用户组织
     * @author bawy
     * @date 2018/8/17 16:25
     * @param moveUserToOrgReq 调整用户组织请求参数
     * @param modifier 当前登录用户
     * @return 成功调整提示语
     */
    String moveUser(MoveUserToOrgReqVO moveUserToOrgReq, int modifier);

    /**
     * 查询组织所有用户
     * @author bawy
     * @date 2018/8/17 16:26
     * @param requestContainer 查询组织中用户请求参数容器
     * @return 组织中所有用户
     */
    PageResultContainer<OrgUserRelVO> queryUsers(PageRequestContainer<QueryOrgUserRelReqVO> requestContainer);

    /**
     * 修改组织内用户角色
     * @author bawy
     * @date 2018/8/17 16:28
     * @param modOrgUserRelReq 修改用户角色请求参数
     * @param modifier 当前登录用户
     * @return 成功修改提示语
     */
    String modUserRole(ModOrgUserRelReqVO modOrgUserRelReq, int modifier);

    /**
     * 获取指定组织的上级组织
     * @author bawy
     * @date 2018/8/20 15:49
     * @param orgEntity 组织
     * @return 上级组织
     */
    CcOrgEntity getParentOrg(CcOrgEntity orgEntity);

    /**
     * 获取组织详细
     * @author bawy
     * @date 2018/8/21 9:26
     * @param orgId 组织标识
     * @param userId 当前登录用户
     * @return 组织信息
     */
    OrgDetailInfoVO getOrgInfo(int orgId, int userId);

    /**
     * 将用户加入一级组织
     * @author bawy
     * @date 2018/8/21 10:07
     * @param userId 用户标识
     * @param orgId 组织标识
     * @param roleId 角色标识
     */
    void addUserToLevelOrg(int userId, int orgId, int roleId);

    /**
     * 用户加入组织
     * @author bawy
     * @date 2018/8/21 10:14
     * @param userId 用户标识
     * @param orgId 组织标识
     * @param roleId 角色标识
     */
    void addUserToOrg(int userId, int orgId, int roleId);

    /**
     * 获取所有组织角色
     * @author bawy
     * @date 2018/8/21 10:56
     * @return 所有组织角色
     */
    List<RoleVO> getOrgRole();

    /**
     * 校验用户不是组织的领导
     * @author oulc
     * @date 18-8-22 下午2:54
     * @param userId 用户主键
     */
    void checkUserIsNotOrgLeader(int userId);

    /**
     * 获取组织所有用户数据
     * @author bawy
     * @date 2018/8/21 16:56
     * @param orgId 组织标识
     * @param userId 当前登录用户
     * @return 符合条件的所有用户数据
     */
    List<OrgUserVO> getOrgUsers(int orgId, int userId);

    /**
     * 获取组织指定角色所有用户数据
     * @param orgId 组织标识
     * @param roleId 角色标识
     * @param userId 当前登录用户
     * @return 用户数据
     */
    List<OrgUserVO> getOrgUsers(int orgId, int roleId, int userId);

    /**
     * 根据组织标识获取组织信息
     * @author bawy
     * @date 2018/8/23 15:57
     * @param orgId 组织标识
     * @return 组织信息
     */
    CcOrgEntity getOrgById(int orgId);

    /**
     * 获取用户所属组织
     * @author bawy
     * @date 2018/8/24 15:51
     * @param userId 用户标识
     * @return 组织用户关系
     */
    CcOrgUserRelEntity getOrgByUserId(int userId);

    /**
     * 判断用户是否在指定组织中并担任指定角色
     * @author bawy
     * @date 2018/8/24 16:00
     * @param orgId 组织标识
     * @param roleId 角色标识
     * @param userId 用户标识
     * @return 在此组织中返回true
     */
    boolean checkUserInOrg(int orgId, int roleId, int userId);
}
