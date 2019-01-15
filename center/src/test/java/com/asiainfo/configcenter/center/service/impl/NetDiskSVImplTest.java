package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.service.interfaces.INetDiskSV;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.task.TaskInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.io.File;

/**
 * Created by oulc on 2018/8/6.
 * 网盘业务单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NetDiskSVImplTest {
    @Resource
    private INetDiskSV iNetDiskSV;

    @Value("${config.project.netdisk.dir}")
    private String configProjectNetDiskPath;

    /**
     * 测试 saveConfigFileToNetDisk(File configFile,String configFileFolder,AppInfoVO appInfoVO,TaskInfo taskInfo )
     * @throws Exception 异常
     */
    @Test
    public void saveConfigFileToNetDisk() throws Exception {
        File file = new File("G:\\testdir\\config1\\test.txt");
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName("testApp");
        appInfoVO.setEnvName("testEnv");
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(123456);
        taskInfo.setDetailId(456789);
        iNetDiskSV.saveConfigFileToNetDisk(file,"G:\\testdir",appInfoVO,taskInfo);
    }

    /**
     * 测试getNetDiskFullFilePath(String projectName,String envName,String path)
     * @throws Exception 异常
     */
    @Test
    public void getNetDiskFullFilePath() throws Exception {
        String projectName =  "testapp";
        String envName = "testEnv";
        String path = "/testPath/test1";
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName(projectName);
        appInfoVO.setEnvName(envName);
        Assert.assertEquals(configProjectNetDiskPath+"/testapp/testEnv/testPath/test1",iNetDiskSV.getNetDiskFullFilePath(appInfoVO,path));
    }

    /**
     * 测试getNetDiskFullFilePath(AppInfoVO appInfoVO, String path)
     * @throws Exception 异常
     */
    @Test
    public void getNetDiskFullFilePath1() throws Exception {
        String projectName =  "testapp";
        String envName = "testEnv";
        String path = "/testPath/test1";
        Assert.assertEquals(configProjectNetDiskPath+"/testapp/testEnv/testPath/test1",iNetDiskSV.getNetDiskFullFilePath(projectName,envName,path));
    }
    /**
     * 测试deleteConfigFileFromNetDisk(int taskId, int detailId)
     * @throws Exception 异常
     */
    @Test
    public void deleteConfigFileFromNetDisk() throws Exception {
    }

}