package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.JunitConstants;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import com.asiainfo.configcenter.center.service.SV.IJunitSV;
import com.asiainfo.configcenter.center.service.interfaces.IAppEnvSV;
import com.asiainfo.configcenter.center.vo.env.AppEnvReqVO;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.env.UpdateAppEnvReqVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppEnvSVImplTest {
    @Resource
    private IJunitSV iJunitSV;

    @Resource
    private IAppEnvSV iAppEnvSV;




    /**
     * 创建应用环境 项目主键为空
     */
    @Test
    public void createAppEnv1()throws Exception{
        try {
            AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,项目主键不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    //环境名称不能为空

    /**
     * 创建应用环境 环境名称不能为空
     */
    @Test
    public void createAppEnv2()throws Exception{
        try {
            AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
            appEnvReqVO.setAppId(100);
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,环境名称不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 创建应用环境 环境名称不能为空
     */
    @Test
    public void createAppEnv3()throws Exception{
        try {
            AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
            appEnvReqVO.setAppId(100);
            appEnvReqVO.setEnvName("a");
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,项目名称不合法，环境名称长度为2-64个字符，只能由英文字母和数字加下划线组成，首字符只能为字母",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 创建应用环境 项目不存在
     */
    @Test
    public void createAppEnv4()throws Exception{
        try {
            AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
            appEnvReqVO.setAppId(100);
            appEnvReqVO.setEnvName("dev");
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("项目不存在",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 创建应用环境 app已经存在相同名称的环境
     */
    @Transactional
    @Rollback
    @Test
    public void createAppEnv5()throws Exception{
        try {
            int appId = iJunitSV.createApp();
            AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
            appEnvReqVO.setAppId(appId);
            appEnvReqVO.setEnvName("dev");
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
            iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("app已经存在相同名称的环境",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 创建应用环境 正常保存
     */
    @Transactional
    @Rollback
    @Test
    public void createAppEnv6()throws Exception{
        int appId = iJunitSV.createApp();
        AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
        appEnvReqVO.setAppId(appId);
        appEnvReqVO.setEnvName("dev");
        int envId = iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        CcAppEnvEntity ccAppEnvEntity = iAppEnvSV.getEnvByEnvId(envId);
        Assert.assertNotNull(ccAppEnvEntity);
        Assert.assertEquals("dev",ccAppEnvEntity.getEnvName());
    }

    @Transactional
    @Rollback
    @Test
    public void getByAppEnvId()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        CcAppEnvEntity ccAppEnvEntity = iAppEnvSV.getEnvByEnvId(appInfoVO.getEnvId());
        Assert.assertNotNull(ccAppEnvEntity);
        Assert.assertEquals(JunitConstants.envName,ccAppEnvEntity.getEnvName());
    }
    @Transactional
    @Rollback
    @Test
    public void getAppEnvByAppIdAndEnvName()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        List<CcAppEnvEntity> list = iAppEnvSV.getEnvByAppIdAndEnvName(appInfoVO.getAppId(),appInfoVO.getEnvName());
        Assert.assertNotNull(list);
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(appInfoVO.getEnvName(),list.get(0).getEnvName());
    }

    @Transactional
    @Rollback
    @Test
    public void saveAppEnv()throws Exception{
        int appId = iJunitSV.createApp();
        CcAppEnvEntity ccAppEnvEntity = iAppEnvSV.saveAppEnv(appId, JunitConstants.envName,JunitConstants.envDesc,ProjectConstants.ACCOUNT_ROOT);
        Assert.assertTrue(ccAppEnvEntity.getId() != 0);
    }

    @Transactional
    @Rollback
    @Test
    public void updateAppEnv()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        UpdateAppEnvReqVO updateAppEnvReqVO = new UpdateAppEnvReqVO();
        updateAppEnvReqVO.setEnvId(appInfoVO.getEnvId());
        updateAppEnvReqVO.setEnvName("testEnv");
        updateAppEnvReqVO.setDesc("testDesc");

        iAppEnvSV.updateAppEnv(updateAppEnvReqVO);

        CcAppEnvEntity ccAppEnvEntity = iAppEnvSV.getEnvByEnvId(appInfoVO.getEnvId());
        Assert.assertNotNull(ccAppEnvEntity);
        Assert.assertEquals("testEnv",ccAppEnvEntity.getEnvName());
        Assert.assertEquals("testDesc",ccAppEnvEntity.getDescription());
    }

}