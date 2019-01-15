package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.UserExtInfoRepository;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.CcUserExtInfoEntity;
import com.asiainfo.configcenter.center.entity.complex.CXAppInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.vo.home.AppInfo;
import com.asiainfo.configcenter.center.vo.home.HomePageRepVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class HomeSVImpl implements IHomeSV {

    @Resource
    private IAppSV iAppSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private IInstanceSV iInstanceSV;

    @Resource
    private ITaskSV iTaskSV;

    @Resource
    private UserExtInfoRepository userExtInfoRepository;

    @Override
    public HomePageRepVO getHomePageData(int userId) {
        HomePageRepVO homePageRepVO = new HomePageRepVO();
        //获取应用数量
        homePageRepVO.setAppNum(iAppSV.getMyAppCount(userId));
        //获取最近访问的app信息
        homePageRepVO.setAppInfos(createRecentProject(iAppSV.getUserRecentProject(userId)));
        //获取环境数量
        homePageRepVO.setEnvNum(iAppEnvSV.getEnvCountByUserId(userId));
        //获取实例数量
        homePageRepVO.setInsNum(iInstanceSV.getInstanceCountByUserId(userId));
        //获取通过任务数量
        homePageRepVO.setPassTaskNum(iTaskSV.getPassTaskCount(userId));
        //获取需要审核任务数量
        homePageRepVO.setAuditTaskNum(iTaskSV.getAuditTaskCount(userId));
        //获取回退任务数量
        homePageRepVO.setRollBackTaskNum(iTaskSV.getRollBackTaskCount(userId));
        return homePageRepVO;
    }

    /**
     * 创建最近访问应用的信息
     * @author oulc
     * @date 18-8-28 下午2:24
     * @param recentProjectList 最近访问应用实体
     * @return 应用信息实体列表
     */
    private AppInfo[] createRecentProject(List<CcAppEntity> recentProjectList){
        AppInfo[] appInfoArray = null;
        if( recentProjectList != null && recentProjectList.size() > 0 ){
            appInfoArray = new AppInfo[recentProjectList.size()];
            for( int i = 0 ;i < appInfoArray.length ; i++ ){
                CcAppEntity ccAppEntity = recentProjectList.get(i);
                AppInfo appInfo = new AppInfo();
                appInfo.setAppId(ccAppEntity.getId());
                appInfo.setAppName(ccAppEntity.getAppName());
                appInfoArray[i] = appInfo;
            }
        }
        return appInfoArray;
    }

    @Override
    @Transactional
    public void createRecentVisitedApp(int appId, int userId) {
        Assert4CC.isTrue(appId>0, "应用标识“"+appId+"”不合法");
        CcUserExtInfoEntity entity =  userExtInfoRepository.findByUserIdAndExtInfoKeyAndExtInfoValueAndStatus(userId,ProjectConstants.USER_ACCESS_RECENT_PROJECT,appId+"",ProjectConstants.STATUS_VALID);
        if(entity == null) {
            CcUserExtInfoEntity entityInfo = new CcUserExtInfoEntity();
            entityInfo.setExtInfoKey(ProjectConstants.USER_ACCESS_RECENT_PROJECT);
            entityInfo.setExtInfoValue(appId + "");
            entityInfo.setUserId(userId);
            entityInfo.setStatus(ProjectConstants.STATUS_VALID);
            Timestamp currentTime = TimeUtil.currentTime();
            entityInfo.setCreateTime(currentTime);
            entityInfo.setUpdateTime(currentTime);
            userExtInfoRepository.save(entityInfo);
        }
    }

}
