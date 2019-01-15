package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.PermissionCommon;
import com.asiainfo.configcenter.center.entity.CcAppUserRelEntity;
import com.asiainfo.configcenter.center.service.interfaces.IAppSV;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionCheckSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.vo.BaseAppReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限校验服务实现类
 * Created by bawy on 2018/8/23 19:37.
 */
@Service
public class PermissionCheckSVImpl implements IPermissionCheckSV{

    @Resource
    private IAppSV appSV;

    @Resource
    private IUserSV userSV;

    @Override
    public boolean appPermissionCheck(BaseAppReqVO baseAppReq, int userId, String url) {
        if (baseAppReq!=null){
            boolean hasPermission = false;
            CcAppUserRelEntity appUserRelEntity = null;
            if (baseAppReq.getAppId()!=0){
                appUserRelEntity = appSV.getAppRoleByAppId(baseAppReq.getAppId(), userId);
            }else if (baseAppReq.getEnvId()!=0){
                appUserRelEntity = appSV.getAppRoleByEnvId(baseAppReq.getEnvId(), userId);
            }
            List<String> allServiceUrls;
            if (appUserRelEntity == null){
                //用户不在应用中，判断系统角色是否有权限
                allServiceUrls = PermissionCommon.getAllServiceUrlBySysRoleId(userSV.getRoleIdByUserId(userId));
            }else {
                //用户在应用中，直接判断应用角色是否有权限
                allServiceUrls = PermissionCommon.getAllServiceUrlByAppRoleId(appUserRelEntity.getRoleId());
            }
            if (allServiceUrls != null){
                hasPermission = allServiceUrls.contains(url);
            }
            return hasPermission;
        }
        return true;
    }

}
