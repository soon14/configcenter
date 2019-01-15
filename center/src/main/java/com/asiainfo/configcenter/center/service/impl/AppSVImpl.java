package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.AppMapper;
import com.asiainfo.configcenter.center.dao.mapper.AppUserRelMapper;
import com.asiainfo.configcenter.center.dao.repository.AppEnvRepository;
import com.asiainfo.configcenter.center.dao.repository.AppRepository;
import com.asiainfo.configcenter.center.dao.repository.AppUserRelRepository;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import com.asiainfo.configcenter.center.entity.CcAppUserRelEntity;
import com.asiainfo.configcenter.center.entity.CcUserExtInfoEntity;
import com.asiainfo.configcenter.center.entity.complex.CXAppInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.IAppSV;
import com.asiainfo.configcenter.center.service.interfaces.IGitSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.app.AppReqVO;
import com.asiainfo.configcenter.center.vo.app.AppUserRelVO;
import com.asiainfo.configcenter.center.vo.app.AppUserReqVO;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.asiainfo.configcenter.zookeeper.cczk.ProjectNodeOper;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * app后端服务实现类
 * Created by bawy on 2018/7/24 11:27.
 */
@Service
public class AppSVImpl implements IAppSV{

    private Logger logger = Logger.getLogger(AppSVImpl.class);


    @Resource
    private AppRepository appRepository;
    @Resource
    private AppUserRelRepository appUserRelRepository;
    @Resource
    private IUserSV userSV;
    @Resource
    private CCServerZKManager ccServerZKManager;
    @Resource
    private AppEnvRepository appEnvRepository;
    @Resource
    private AppUserRelMapper appUserRelMapper;
    @Resource
    private IGitSV iGitSV;
    @Resource
    private AppMapper appMapper;

    @Override
    @Transactional
    public void createApp(AppReqVO app, int userId) {
        String appName = app.getAppName();
        String description = app.getDescription();
        Assert4CC.notNull(appName, "app名称不可为空");
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_APP_NAME, appName), "app名称不符合规则");
        Assert4CC.isTrue(description == null || description.length()<=1000, "应用描述不能超过1000个字符");
        CcAppEntity appEntity = appRepository.findByAppNameAndStatus(appName, ProjectConstants.STATUS_VALID);
        Assert4CC.isNull(appEntity, ResultCodeEnum.APP_EXIST_ERROR, "请更换名称");
        Timestamp currentTime = TimeUtil.currentTime();
        //创建应用
        appEntity = new CcAppEntity();
        appEntity.setAppName(appName);
        appEntity.setDescription(description);
        appEntity.setCreator(userId);
        appEntity.setCreateTime(currentTime);
        appEntity.setModifier(userId);
        appEntity.setUpdateTime(currentTime);
        appEntity.setStatus(ProjectConstants.STATUS_VALID);
        appRepository.saveAndFlush(appEntity);
        //创建应用用户关系
        CcAppUserRelEntity appUserRelEntity = new CcAppUserRelEntity();
        appUserRelEntity.setAppId(appEntity.getId());
        appUserRelEntity.setUserId(userId);
        appUserRelEntity.setRoleId(ProjectConstants.ROLE_MANAGER);
        appUserRelEntity.setCreator(userId);
        appUserRelEntity.setCreateTime(currentTime);
        appUserRelEntity.setModifier(userId);
        appUserRelEntity.setUpdateTime(currentTime);
        appUserRelEntity.setStatus(ProjectConstants.STATUS_VALID);
        appUserRelRepository.saveAndFlush(appUserRelEntity);
        try {
            //获取zk应用操作实例
            ProjectNodeOper projectNodeOper = ccServerZKManager.getProjectNodeOper();
            Assert4CC.isTrue(!projectNodeOper.projectNodeExist(appName), ResultCodeEnum.APP_EXIST_ERROR, "请联系管理员");
            projectNodeOper.createProjectNode(appName, appEntity.getId()+"");
        }catch (KeeperException | InterruptedException e){
            logger.error(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR, "zookeeper连接异常,无法创建App");
        }
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName(appName);
        iGitSV.createAppGitDir(appInfoVO);
    }

    @Override
    public List<CXAppInfoEntity> getMyApps(String appName, int userId) {
        if (userSV.isAdministrator(userId)){
            return appMapper.getMyApps(appName,ProjectConstants.STATUS_VALID);
        }else{
            return appMapper.getMyAppsByUserId(appName, userId, ProjectConstants.STATUS_VALID);
        }
    }

    @Override
    public boolean appExist(int appId) {
        return getAppById(appId) != null;
    }

    @Override
    public CcAppEntity getAppById(int appId) {
        return appRepository.findByIdAndStatus(appId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcAppEntity getAppByIdCheck(int appId){
        CcAppEntity ccAppEntity = getAppById(appId);
        Assert4CC.notNull(ccAppEntity,ResultCodeEnum.APP_COMMON_ERROR,"应用不存在，应用主键:"+appId);
        return ccAppEntity;
    }

    @Override
    @Transactional
    public void delApp(int appId, int userId) {
        Assert4CC.isTrue(appId>0, "应用标识“"+appId+"”不合法");
        CcAppEntity appEntity = appRepository.findByIdAndStatus(appId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(appEntity, ResultCodeEnum.APP_NOT_EXIST_ERROR);
        int envCount = appEnvRepository.countByAppIdAndStatus(appId, ProjectConstants.STATUS_VALID);
        Assert4CC.isTrue(envCount <= 0, ResultCodeEnum.APP_COMMON_ERROR, "该应用中存在环境标签节点，无法删除");
        try {
            //获取zk应用操作实例
            ProjectNodeOper projectNodeOper = ccServerZKManager.getProjectNodeOper();
            projectNodeOper.deleteProjectNode(appEntity.getAppName());
        }catch (KeeperException | InterruptedException e){
            logger.error(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR, "zookeeper连接异常,无法删除App");
        }
        Timestamp currentTime = TimeUtil.currentTime();
        appEntity.setStatus(ProjectConstants.STATUS_INVALID);
        appEntity.setModifier(userId);
        appEntity.setUpdateTime(currentTime);
        appRepository.saveAndFlush(appEntity);
    }

    @Override
    public CcAppUserRelEntity getAppRoleByAppId(int appId, int userId) {
        return appUserRelRepository.findByAppIdAndUserIdAndStatus(appId, userId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcAppUserRelEntity getAppRoleByEnvId(int envId, int userId) {
        return appUserRelRepository.getByEnvIdAndUserId(envId, userId, ProjectConstants.STATUS_VALID);
    }

    @Override
    @Transactional
    public void modApp(AppReqVO app, int userId) {
        int appId = app.getAppId();
        String description = app.getDescription();
        String newAppName = app.getAppName();
        Assert4CC.isTrue(appId>0, "应用标识“"+appId+"”不合法");
        Assert4CC.isTrue(description == null || description.length()<=1000, "应用描述不能超过1000个字符");
        CcAppEntity appEntity = appRepository.findByIdAndStatus(appId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(appEntity, ResultCodeEnum.APP_NOT_EXIST_ERROR);
        String oldAppName = appEntity.getAppName();
        if (!newAppName.equals(oldAppName)){
            //修改应用名称，需要删除旧节点，并且创建新节点
            Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_APP_NAME, newAppName), "app名称不符合规则");
            CcAppEntity existAppEntity = appRepository.findByAppNameAndStatus(newAppName, ProjectConstants.STATUS_VALID);
            Assert4CC.isNull(existAppEntity, ResultCodeEnum.APP_EXIST_ERROR, "请更换名称");
            try {
                //获取zk应用操作实例
                ProjectNodeOper projectNodeOper = ccServerZKManager.getProjectNodeOper();
                Assert4CC.isTrue(!projectNodeOper.projectNodeExist(newAppName), ResultCodeEnum.APP_EXIST_ERROR, "请联系管理员");
                projectNodeOper.createProjectNode(newAppName, appId+"");
                projectNodeOper.deleteProjectNode(oldAppName);
            }catch (KeeperException | InterruptedException e){
                logger.error(ErrorInfo.errorInfo(e));
                throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR, "zookeeper连接异常,无法修改App");
            }
            appEntity.setAppName(newAppName);
        }
        appEntity.setDescription(description);
        appEntity.setModifier(userId);
        appEntity.setUpdateTime(TimeUtil.currentTime());
        appRepository.saveAndFlush(appEntity);
    }

    @Override
    public String getAppNameByEnvId(int envId){
        CcAppEntity ccAppEntity = getAppByEnvId(envId);
        Assert4CC.notNull(ccAppEntity , ResultCodeEnum.APP_ENV_NOT_EXIST_ERROR);
        return ccAppEntity.getAppName();
    }

    @Override
    public CcAppEntity getAppByEnvId(int envId) {
        //查询环境实体
        CcAppEnvEntity ccAppEnvEntity = appEnvRepository.findByIdAndStatus(envId,ProjectConstants.STATUS_VALID);

        Assert4CC.notNull(ccAppEnvEntity,ResultCodeEnum.APP_NOT_EXIST_ERROR);

        return getAppById(ccAppEnvEntity.getAppId());
    }

    @Override
    public String getAppNameByAppId(int appId) {
        CcAppEntity ccAppEntity = getAppById(appId);
        Assert4CC.notNull(ccAppEntity,ResultCodeEnum.APP_NOT_EXIST_ERROR);
        return ccAppEntity.getAppName();
    }
    @Transactional
    public void addUser(AppUserReqVO appUser, int creator) {
        int appId = appUser.getAppId();
        int userId = appUser.getUserId();
        int roleId = appUser.getRoleId();
        Assert4CC.isTrue(appId>0, "应用Id不可为空");
        Assert4CC.isTrue(userId>0, "用户Id不可为空");
        Assert4CC.isTrue(roleId>0, "角色Id不可为空");
        CcAppUserRelEntity appUserRelEntity = appUserRelRepository.findByAppIdAndUserIdAndStatus(appId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.isNull(appUserRelEntity, ResultCodeEnum.APP_USER_EXIST_ERROR);
        appUserRelEntity = new CcAppUserRelEntity();
        appUserRelEntity.setAppId(appId);
        appUserRelEntity.setUserId(userId);
        appUserRelEntity.setRoleId(roleId);
        Timestamp currentTime = TimeUtil.currentTime();
        appUserRelEntity.setCreator(creator);
        appUserRelEntity.setCreateTime(currentTime);
        appUserRelEntity.setModifier(creator);
        appUserRelEntity.setUpdateTime(currentTime);
        appUserRelEntity.setStatus(ProjectConstants.STATUS_VALID);
        appUserRelRepository.saveAndFlush(appUserRelEntity);
    }

    @Override
    @Transactional
    public void delUser(AppUserReqVO appUser, int modifier) {
        int appId = appUser.getAppId();
        int userId = appUser.getUserId();
        Assert4CC.isTrue(appId>0, "应用Id不可为空");
        Assert4CC.isTrue(userId>0, "用户Id不可为空");
        CcAppUserRelEntity appUserRelEntity = appUserRelRepository.findByAppIdAndUserIdAndStatus(appId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(appUserRelEntity, ResultCodeEnum.APP_USER_NOT_EXIST_ERROR);
        appUserRelEntity.setStatus(ProjectConstants.STATUS_INVALID);
        appUserRelEntity.setUpdateTime(TimeUtil.currentTime());
        appUserRelEntity.setModifier(modifier);
        appUserRelRepository.saveAndFlush(appUserRelEntity);
    }

    @Override
    @Transactional
    public void modUser(AppUserReqVO appUser, int modifier) {
        int appId = appUser.getAppId();
        int userId = appUser.getUserId();
        int roleId = appUser.getRoleId();
        Assert4CC.isTrue(appId>0, "应用Id不可为空");
        Assert4CC.isTrue(userId>0, "用户Id不可为空");
        Assert4CC.isTrue(roleId>0, "角色Id不可为空");
        CcAppUserRelEntity appUserRelEntity = appUserRelRepository.findByAppIdAndUserIdAndStatus(appId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(appUserRelEntity, ResultCodeEnum.APP_USER_NOT_EXIST_ERROR);
        appUserRelEntity.setRoleId(roleId);
        appUserRelEntity.setUpdateTime(TimeUtil.currentTime());
        appUserRelEntity.setModifier(modifier);
        appUserRelRepository.saveAndFlush(appUserRelEntity);
    }

    @Override
    public PageResultContainer<AppUserRelVO> getAppUsers(PageRequestContainer<AppUserReqVO> requestContainer) {
        ValidateUtil.checkPageParam(requestContainer);
        AppUserReqVO appUserReqVO = requestContainer.getData();
        int appId = appUserReqVO.getAppId();
        Assert4CC.isTrue(appId>0, "应用Id不可为空");
        String nickname = appUserReqVO.getNickName();
        int roleId = appUserReqVO.getRoleId();
        List<AppUserRelVO> result = appUserRelMapper.getAppUsers(appId, nickname, roleId, requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = appUserRelMapper.getAppUsersCount(appId, nickname, roleId);
        PageResultContainer<AppUserRelVO> resultContainer = new PageResultContainer<>();
        resultContainer.setCount(count);
        resultContainer.setEntities(result);
        return resultContainer;
    }

    @Override
    public CcAppUserRelEntity getAppRoleByConfigItemId(int configItemId, int userId) {
        return appUserRelRepository.getByConfigItemIdIdAndUserId(configItemId, userId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public void checkUserIsNotAppManager(int userId) {
        List<CcAppEntity> list = appRepository.findByUserIdAndRoleId(userId,ProjectConstants.ROLE_MANAGER,ProjectConstants.STATUS_VALID);
        boolean result = list ==  null || list.size() == 0;
        if( ! result){
            String []appNames = new String[list.size()];
            for( int i = 0 ;i < list.size() ;i ++){
                appNames[i] = list.get(i).getAppName();
            }
            throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR,"用户还是应用:"+ Arrays.toString(appNames)+"的项目经理,无法注销用户");
        }
    }

    @Override
    public long getMyAppCount(int userId) {
        if (userSV.isAdministrator(userId)){
            return appRepository.countByStatus(ProjectConstants.STATUS_VALID);
        }else{
            return appMapper.getMyAppCountByUserId(userId,ProjectConstants.STATUS_VALID);
        }
    }

    @Override
    public List<CcAppEntity> getUserRecentProject(int userId) {
        return appMapper.getUserRecentProject(userId);
    }

    @Override
    public CcAppEntity getAppByName(String appName) {
        return appRepository.findByAppNameAndStatus(appName,ProjectConstants.STATUS_VALID);
    }
}
