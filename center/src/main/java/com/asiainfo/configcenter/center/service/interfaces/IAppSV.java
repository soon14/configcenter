package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.CcAppUserRelEntity;
import com.asiainfo.configcenter.center.entity.complex.CXAppInfoEntity;
import com.asiainfo.configcenter.center.vo.app.AppReqVO;
import com.asiainfo.configcenter.center.vo.app.AppUserRelVO;
import com.asiainfo.configcenter.center.vo.app.AppUserReqVO;
import com.asiainfo.configcenter.center.vo.home.AppInfo;

import java.util.List;


public interface IAppSV {
    /**
     * 判断项目是否存在
     * @param appId 项目主键
     * @return 存在：true  不存在:fase
     * @author oulc
     * @date 2018/7/24 14:26
     */
    boolean appExist(int appId);

    /**
     * 根据项目主键查询项目
     * @param appId 项目主键
     * @return 项目实体
     * @author oulc
     * @date 2018/7/24 14:26
     */
    CcAppEntity getAppById(int appId);

    /**
     * 创建新的APP，包括app名称、app描述
     * @author bawy
     * @date 2018/7/24 11:28
     * @param app 创建app请求参数对象
     * @param userId 创建app的用户标识
     */
    void createApp(AppReqVO app, int userId);

    /**
     * 获取用户所有app基本信息
     * @author bawy
     * @date 2018/7/24 16:14
     * @param appName 应用名称
     * @param userId 用户标识
     * @return 返回app列表
     */
    List<CXAppInfoEntity> getMyApps(String appName, int userId);

    /**
     * 删除指定应用
     * @author bawy
     * @date 2018/7/24 16:14
     * @param appId 应用标识
     * @param userId 当前登录用户标识
     */
    void delApp(int appId, int userId);

    /**
     * 根据应用标识和用户标识获取应用用户关系
     * @author bawy
     * @date 2018/7/24 22:40
     * @param appId 应用标识
     * @param userId 用户标识
     * @return 有权限返回true
     */
    CcAppUserRelEntity getAppRoleByAppId(int appId, int userId);

    /**
     * 根据环境标识和用户标识获取应用用户关系
     * @author bawy
     * @date 2018/7/24 22:40
     * @param envId 环境标识
     * @param userId 用户标识
     * @return 有权限返回true
     */
    CcAppUserRelEntity getAppRoleByEnvId(int envId, int userId);

    /**
     * 修改应用
     * @param app 应用基本信息
     * @param userId 用户标识
     */
    void modApp(AppReqVO app, int userId);

    /**
     * 根据环境查询app的名称
     * @param envId 环境标识
     * @return 环境名称
     * @author oulc
     * @date 2018/7/25 14:52
     */
    String getAppNameByEnvId(int envId);

    /**
     * 根据环境主键查询项目
     * @param envId 环境标识
     * @return 项目实体
     * @author oulc
     * @date 2018/7/25 14:52
     */
    CcAppEntity getAppByEnvId(int envId);

    /**
     * 根据项目主键查询项目名称
     * @param appId 项目主键
     * @return 项目名称
     * @author oulc
     * @date 2018/7/25 16:13
     */
    String getAppNameByAppId(int appId);

     /** 应用添加用户
     * @author bawy
     * @date 2018/7/25 16:20
     * @param appUser 应用用户请求参数
     * @param creator 当前登录用户标识
     */
    void addUser(AppUserReqVO appUser, int creator);

    /**
     * 应用移除用户
     * @author bawy
     * @date 2018/7/25 16:29
     * @param appUser 应用用户请求参数
     * @param modifier 当前登录用户标识
     */
    void delUser(AppUserReqVO appUser, int modifier);

    /**
     * 修改应用中指定用户信息
     * @author bawy
     * @date 2018/7/25 16:40
     * @param appUser 用户请求参数
     * @param modifier 当前登录的用户标识
     */
    void modUser(AppUserReqVO appUser, int modifier);

    /**
     * 查询app中所有用户
     * @author bawy
     * @date 2018/7/25 23:52
     * @param requestContainer 请求参数
     */
    PageResultContainer<AppUserRelVO> getAppUsers(PageRequestContainer<AppUserReqVO> requestContainer);

    CcAppEntity getAppByIdCheck(int appId);

    /**
     * 根据配置项标识获取应用用户关系
     * @author bawy
     * @date 2018/8/7 14:49
     * @param configItemId 配置项标识
     * @param userId 用户标识
     * @return 应用用户关系
     */
    CcAppUserRelEntity getAppRoleByConfigItemId(int configItemId, int userId);

    /**
     * 校验用户不是应用的经理
     * @author oulc
     * @date 18-8-22 下午2:56
     * @param userId 用户主键
     */
    void checkUserIsNotAppManager(int userId);

    /**
     * 获取应用数量
     * @author oulc
     * @date 18-8-28 上午11:21
     * @param userId 用户主键
     * @return 应用数量
     */
    long getMyAppCount(int userId);

    /**
     * 查询用户最近访问的项目
     * @author oulc
     * @date 18-8-28 下午2:09
     * @param userId 用户主键
     * @return 最近访问项目列表
     */
    List<CcAppEntity> getUserRecentProject(int userId);

    /**
     * 通过应用名称来获取应用
     * @param appName 应用名称
     * @return 返回获取的应用
     * */
    CcAppEntity getAppByName(String appName);
}
