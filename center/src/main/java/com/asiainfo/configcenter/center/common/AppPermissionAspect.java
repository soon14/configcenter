package com.asiainfo.configcenter.center.common;

import com.asiainfo.configcenter.center.controller.BaseController;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionCheckSV;
import com.asiainfo.configcenter.center.vo.BaseAppReqVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;

/**
 * 应用权限校验切面类
 * Created by bawy on 2018/7/24 17:17.
 */
@Aspect
@Component
public class AppPermissionAspect extends BaseController{

    @Resource
    private IPermissionCheckSV permissionCheckSV;

    @Pointcut("@annotation(com.asiainfo.configcenter.center.common.AppPermission)")
    public void check() {

    }

    @Before(value = "check()")
    public void checkUserPermission(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object[] args = joinPoint.getArgs();
        if (args.length>0){
            BaseAppReqVO baseAppReqVO = null;
            if (args[0] instanceof BaseAppReqVO) {
                baseAppReqVO = (BaseAppReqVO) args[0];
            }else if (args[0] instanceof PageRequestContainer) {
                Object paramObj = ((PageRequestContainer)args[0]).getData();
                if (paramObj instanceof BaseAppReqVO){
                    baseAppReqVO = (BaseAppReqVO) paramObj;
                }
            }
            String url = attributes.getRequest().getRequestURI();
            //调用权限校验服务
            boolean hasPermission = permissionCheckSV.appPermissionCheck(baseAppReqVO, getCurrentUser().getId(), url);
            Assert4CC.isTrue(hasPermission, ResultCodeEnum.APP_PERMISSION_ERROR);
        }
    }



}
