package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.service.SV.IJunitSV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 应用单元测试类
 * Created by bawy on 2018/7/24 15:07.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppSVImplTest {
    @Resource
    private IJunitSV iJunitSV;

    /**
     * 创建测试用的应用
     * @throws Exception 异常
     */
    @Test
    public void createTestApp() throws Exception {
        createApp(1);
    }
    private void createApp(int num)throws Exception{
       /* AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName("config_center_test_project"+num);
        iJunitSV.createApp(appInfoVO);

        appInfoVO.setEnvName("env"+num);
        iJunitSV.createEnv(appInfoVO);

        iJunitSV.createInsAll(appInfoVO,num+1);
        iJunitSV.createInsAll(appInfoVO,num+2);*/
    }

}