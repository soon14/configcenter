package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcUserInfoEntity;
import com.asiainfo.configcenter.center.vo.user.*;

import java.util.List;

/**
 * 用户信息接口类
 * Created by bawy on 2018/7/3.
 */
public interface IUserSV {

    /**
     * 验证登录信息
     * @author bawy
     * @date 2018/7/6 11:28
     * @param username 用户名
     * @param password 用户名称
     * @return 用户信息
     */
    UserInfoVO login(String username, String password);

    /**
     * 根据用户标识获取角色标识
     * @author bawy
     * @date 2018/7/16 23:15
     * @param userId 用户标识
     * @return 返回对应角色标识
     */
    int getRoleIdByUserId(int userId);

    /**
     * 用户注册
     * @author bawy
     * @date 2018/7/17 13:54
     * @param registerReqVO 注册请求参数
     * @return 注册返回值
     */
    String register(RegisterReqVO registerReqVO);
    /**
     * 更新用户信息
     * @author oulc
     * @date 2018/7/16 14:15
     * @param userInfoReqVO 用户信息
     */
    void updateUserInfo(UserInfoReqVO userInfoReqVO, int userId);


    /**
     * 查询用户基本信息、扩展信息和角色信息
     * @param pageRequestContainer
     * @param userId 用户主键
     * @return 用户基本信息、扩展信息和角色信息
     * @author oulc
     * @date 2018/7/18 10:31
     */
    PageResultContainer<CXCcUserInfoEntity> getUserInfoAndRole(PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer, int userId);

    /**
     * 更新用户密码
     * @author bawy
     * @date 2018/7/18 20:32
     * @param passwordVO 密码对象
     * @param userId 用户id
     */
    void updatePassword(PasswordVO passwordVO, int userId);
    /**
     * 注销账户
     * @param closeAccountVO 账户信息
     * @param userId 操作用户
     * @author oulc
     * @date 2018/7/18 15:14
     */
    void closeAccounts(CloseAccountVO closeAccountVO, int userId);

    /**
     * 注销账户
     * @param userId 用户主键
     * @author oulc
     * @date 2018/7/18 15:23
     */
    void closeAccount(int userId);

    /**
     * 审核账户
     * @param auditAccountVO 账户信息
     * @param userId 操作用户
     * @author oulc
     * @date 2018/7/18 16:16
     */
    void auditAccounts(AuditAccountVO auditAccountVO, int userId);

    /**
     * 审核账户
     * @param userId 账户主键
     * @author oulc
     * @date 2018/7/18 16:17
     */
    void auditAccount(int userId,int operate);


    boolean isAdministrator(int userId);


    /** 获取邮箱验证码
     * @author bawy
     * @date 2018/7/19 16:29
     * @param username 用户名
     * @param email 邮箱帐号
     */
    void getAuthCode(String username, String email);

    /**
     * 重置密码
     * @author bawy
     * @date 2018/7/20 0:10
     * @param resetPasswordVO 重置密码对象
     */
    void resetPassword(ResetPasswordVO resetPasswordVO);

    /**
     * 根据用户名或昵称获取所有用户列表
     * @author bawy
     * @date 2018/7/23 15:47
     * @param pageRequestContainer 请求参数容器对象
     * @return 用户列表
     */
    PageResultContainer<CcUserEntity> getUserList(PageRequestContainer<UserInfoReqVO> pageRequestContainer);

    /**
     * 查询应用的管理人员
     * @param appId 应用主键
     * @return 用户信息
     * @author oulc
     * @date 2018/7/26 17:54
     */
    List<CcUserEntity> findAppManager(int appId);

    /**
     * 清理用户信息缓存
     * @author bawy
     * @date 2018/7/27 11:10
     */
    void cleanCache();

    /**
     * 根据用户主键查询用户
     * @author oulc
     * @date 18-8-16 下午5:55
     * @param userId 用户主键
     * @return 用户实体
     */
    CcUserEntity getUserById(int userId);

    /**
     * 注销账户之前的校验
     * @author oulc
     * @date 18-8-22 下午3:41
     * @param userId
     */
    void closeAccountPrepare(int userId);
    
    /**
     * 获取指定组织所有用户
     * @author bawy
     * @date 2018/8/24 17:38
     * @param orgId 组织标识
     * @return 用户信息
     */
    List<CcUserEntity> getAllUserByOrgId(int orgId);

    /**
     * 获取指定组织指定角色所有用户
     * @param orgId 组织标识
     * @param roleId 角色标识
     * @return 用户信息
     */
    List<CcUserEntity> getAllUserByOrgIdAndRoleId(int orgId, int roleId);


}
