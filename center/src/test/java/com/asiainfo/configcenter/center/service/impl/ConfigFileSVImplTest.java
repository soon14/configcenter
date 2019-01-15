package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.JunitConstants;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.TaskDetailConfigRepository;
import com.asiainfo.configcenter.center.dao.repository.TaskRepository;
import com.asiainfo.configcenter.center.dao.mapper.ConfigFileMapper;
import com.asiainfo.configcenter.center.entity.CcConfigFileEntity;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.entity.CcTaskEntity;
import com.asiainfo.configcenter.center.service.SV.IJunitSV;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configfile.*;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.asiainfo.configcenter.center.vo.configfile.UpConfigFileVO;
import com.asiainfo.configcenter.center.vo.configfile.UpManyConfigFileVO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.util.List;

/**
 * Created by oulc on 2018/7/31.
 * 配置文件单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigFileSVImplTest {
    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private ConfigFileMapper configFileMapper;

    @Resource
    private IJunitSV iJunitSV;


    @Resource
    private TaskDetailConfigRepository taskDetailConfigRepository;

    @Resource
    private INetDiskSV iNetDiskSV;

    @Resource
    private ITaskSV iTaskSV;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private IGitSV iGitSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    /**
     * 测试异常 环境主键不能为空
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate1()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,环境主键不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 配置文件信息不能为空
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate2()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(1234);
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,配置文件信息不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 文件名称不能为空
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate3()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(1234);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upOneConfigFileVO.setFile(upConfigFileVO);
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,文件名称不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 文件内容不能为空
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate4()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(1234);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.txt");
            upConfigFileVO.setUrl("");
            upOneConfigFileVO.setFile(upConfigFileVO);
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,文件内容不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }



    /**
     * 测试异常 文件格式不正确
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate6()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(1234);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.txt");
            upConfigFileVO.setUrl("abs");
            upOneConfigFileVO.setFile(upConfigFileVO);
            upOneConfigFileVO.setFileSaveDir("/config");
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,文件格式不正确",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 创建配置文件目录失败，请咨询管理员
     * @throws Exception
     */
    @Test
    public void upOneConfigFileForCreate7()throws Exception{
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(1234);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.txt");
            upConfigFileVO.setUrl("test,abs");
            upOneConfigFileVO.setFile(upConfigFileVO);
            upOneConfigFileVO.setFileSaveDir("/|123");
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertTrue(e.getMessage().contains("创建配置文件目录失败，请咨询管理员:"));
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 配置文件已经存在
     * @throws Exception
     */
    @Transactional
    @Rollback
    @Test
    public void upOneConfigFileForCreate8()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        iConfigFileSV.createConfigFileDataToBaseInfo(appInfoVO.getEnvId(),"config/test.txt", null,"filedesc","1.0.0",ProjectConstants.ACCOUNT_ROOT);
        try {
            UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
            upOneConfigFileVO.setEnvId(appInfoVO.getEnvId());
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.txt");
            upConfigFileVO.setUrl("test,abs");
            upOneConfigFileVO.setFile(upConfigFileVO);
            upOneConfigFileVO.setFileSaveDir("/config");
            iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertTrue(e.getMessage().contains("已经存在同名的配置文件"));
            return;
        }
        Assert.fail();
    }

    /**
     * upOneConfigFileForCreate()
     * @throws Exception
     */
    @Transactional
    @Rollback
    @Test
    public void upOneConfigFileForCreateRight()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
        upOneConfigFileVO.setEnvId(appInfoVO.getEnvId());
        upOneConfigFileVO.setDesc("fileDesc");
        upOneConfigFileVO.setFileSaveDir("/");

        UpConfigFileVO upConfigFileVO  = new UpConfigFileVO();
        upConfigFileVO.setName("test.txt");
        byte []fileData ={49,50,51};
        String fileContentStr = "test,"+Base64.encodeBase64String(fileData);
        upConfigFileVO.setUrl(fileContentStr);

        upOneConfigFileVO.setFile(upConfigFileVO);
        iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);

        List<CcTaskEntity> taskList = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId = taskList.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);
        int detailId = detailList.get(0).getId();

        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(0));

        List<CcConfigFileEntity> configFileList = iConfigFileSV.getConfigFilesByEnvId(appInfoVO.getEnvId());
        Assert.assertNotNull(configFileList);
        Assert.assertEquals(1,configFileList.size());
        CcConfigFileEntity ccConfigFileEntity = configFileList.get(0);
        Assert.assertEquals("test.txt",ccConfigFileEntity.getFileName());
        Assert.assertNotNull(ccConfigFileEntity.getFileVersion());
        Assert.assertEquals(appInfoVO.getEnvId(),ccConfigFileEntity.getAppEnvId());
        Assert.assertEquals("fileDesc",ccConfigFileEntity.getDescription());
        Assert.assertEquals(ProjectConstants.ACCOUNT_ROOT,ccConfigFileEntity.getCreator());
        Assert.assertEquals(ProjectConstants.ACCOUNT_ROOT,ccConfigFileEntity.getModifier());
        Assert.assertEquals(ProjectConstants.STATUS_VALID,ccConfigFileEntity.getStatus());
        Assert.assertNotNull(ccConfigFileEntity.getCreateTime());
        Assert.assertNotNull(ccConfigFileEntity.getUpdateTime());

        Assert.assertTrue(ccServerZKManager.getConfigFileNodeOper().configFileNodeExist(appInfoVO.getAppName(),appInfoVO.getEnvName(),ccConfigFileEntity.getFileName()));

        //回退
        //删除git中的配置文件
        File configFile  = new File(iGitSV.getGitFullPath(appInfoVO,ccConfigFileEntity.getFileName()));
        Assert.assertTrue(configFile.exists());
        iGitSV.deleteConfigFileAndCommit(appInfoVO,ccConfigFileEntity.getFileName());
    }

    /**
     * 测试异常 环境主键不能为空
     * @throws Exception
     */
    @Test
    public void upManyConfigFileForCreate1()throws Exception{
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,环境主键不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 压缩包内容不能全部为空
     * @throws Exception
     */
    @Test
    public void upManyConfigFileForCreate2()throws Exception{
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            upManyConfigFileVO.setEnvId(123);
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,压缩包内容不能全部为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 压缩包内容不能全部为空
     * @throws Exception
     */
    @Test
    public void upManyConfigFileForCreate3()throws Exception{
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            upManyConfigFileVO.setEnvId(123);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upManyConfigFileVO.setZipFile(upConfigFileVO);
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,压缩文件名称不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 压缩文件内容不能为空
     * @throws Exception
     */
    @Test
    public void upManyConfigFileForCreate4()throws Exception{
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            upManyConfigFileVO.setEnvId(123);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.zip");
            upManyConfigFileVO.setZipFile(upConfigFileVO);
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,压缩文件内容不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 文件格式不正确
     * @throws Exception
     */
    @Test
    public void upManyConfigFileForCreate5()throws Exception{
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            upManyConfigFileVO.setEnvId(123);
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.zip");
            upManyConfigFileVO.setZipFile(upConfigFileVO);
            upConfigFileVO.setUrl("abs");
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,文件格式不正确",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试异常 判断配置文件名称没有重复
     * @throws Exception
     */
    @Transactional
    @Rollback
    @Test
    public void upManyConfigFileForCreate6()throws Exception{
        byte [] data = {
                80, 75, 3, 4, 10, 0, 0, 0, 0, 0, 42, -116, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 116, 101, 115, 116, 46, 116, 120, 116, 80, 75, 1, 2, 63, 0, 10, 0, 0, 0, 0, 0, 42, -116, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 36, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 116, 101, 115, 116, 46, 116, 120, 116, 10, 0, 32, 0, 0, 0, 0, 0, 1, 0, 24, 0, -102, -24, -20, -126, 104, 45, -44, 1, -102, -24, -20, -126, 104, 45, -44, 1, -102, -24, -20, -126, 104, 45, -44, 1, 80, 75, 5, 6, 0, 0, 0, 0, 1, 0, 1, 0, 90, 0, 0, 0, 38, 0, 0, 0, 0, 0
        };
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        iConfigFileSV.createConfigFileDataToBaseInfo(appInfoVO.getEnvId(),"test.txt", null, "filedesc","1.0.0",ProjectConstants.ACCOUNT_ROOT);
        try {
            UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
            upManyConfigFileVO.setEnvId(appInfoVO.getEnvId());
            UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
            upConfigFileVO.setName("test.zip");
            upConfigFileVO.setUrl("test,"+Base64.encodeBase64String(data));
            upManyConfigFileVO.setZipFile(upConfigFileVO);
            iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertTrue(e.getMessage().contains("已经存在同名的配置文件"));
            return;
        }
        Assert.fail();
    }

    @Transactional
    @Rollback
    @Test
    public void upManyConfigFileForCreate7()throws Exception{
        byte [] data = {
                80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 111, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 99, 111, 109, 47, 97, 115, 105, 97, 105, 110, 102, 111, 47, 80, 75, 3, 4, 10, 0, 0, 0, 0, 0, 111, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 99, 111, 109, 47, 97, 115, 105, 97, 105, 110, 102, 111, 47, 116, 101, 115, 116, 46, 116, 120, 116, 80, 75, 3, 4, 10, 0, 0, 0, 0, 0, 42, -116, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 116, 101, 115, 116, 46, 116, 120, 116, 80, 75, 3, 4, 20, 0, 0, 0, 0, 0, 108, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 99, 111, 109, 47, 80, 75, 1, 2, 63, 0, 20, 0, 0, 0, 0, 0, 111, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 36, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 99, 111, 109, 47, 97, 115, 105, 97, 105, 110, 102, 111, 47, 10, 0, 32, 0, 0, 0, 0, 0, 1, 0, 24, 0, -122, 65, 50, -18, 105, 45, -44, 1, 123, -89, -72, -15, 105, 45, -44, 1, -116, -71, 15, -22, 105, 45, -44, 1, 80, 75, 1, 2, 63, 0, 10, 0, 0, 0, 0, 0, 111, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 36, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 43, 0, 0, 0, 99, 111, 109, 47, 97, 115, 105, 97, 105, 110, 102, 111, 47, 116, 101, 115, 116, 46, 116, 120, 116, 10, 0, 32, 0, 0, 0, 0, 0, 1, 0, 24, 0, 73, -52, -119, -19, 105, 45, -44, 1, 73, -52, -119, -19, 105, 45, -44, 1, 73, -52, -119, -19, 105, 45, -44, 1, 80, 75, 1, 2, 63, 0, 10, 0, 0, 0, 0, 0, 42, -116, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 36, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 94, 0, 0, 0, 116, 101, 115, 116, 46, 116, 120, 116, 10, 0, 32, 0, 0, 0, 0, 0, 1, 0, 24, 0, -102, -24, -20, -126, 104, 45, -44, 1, -102, -24, -20, -126, 104, 45, -44, 1, -102, -24, -20, -126, 104, 45, -44, 1, 80, 75, 1, 2, 63, 0, 20, 0, 0, 0, 0, 0, 108, -115, 6, 77, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 36, 0, 0, 0, 0, 0, 0, 0, 16, 0, 0, 0, -124, 0, 0, 0, 99, 111, 109, 47, 10, 0, 32, 0, 0, 0, 0, 0, 1, 0, 24, 0, -60, -12, 7, -21, 105, 45, -44, 1, -3, -109, -72, -15, 105, 45, -44, 1, 22, -96, -85, -30, 105, 45, -44, 1, 80, 75, 5, 6, 0, 0, 0, 0, 4, 0, 4, 0, 118, 1, 0, 0, -90, 0, 0, 0, 0, 0
        };
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
        upManyConfigFileVO.setEnvId(appInfoVO.getEnvId());
        UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
        upConfigFileVO.setName("test.zip");
        upConfigFileVO.setUrl("test,"+Base64.encodeBase64String(data));
        upManyConfigFileVO.setZipFile(upConfigFileVO);
        iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);

        List<CcTaskEntity> taskList = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId = taskList.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);
        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(0));
        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(1));


        List<CcConfigFileEntity> configFileList = iConfigFileSV.getConfigFilesByEnvId(appInfoVO.getEnvId());
        Assert.assertEquals("com/asiainfo/test.txt",configFileList.get(0).getFileName());
        Assert.assertEquals("test.txt",configFileList.get(1).getFileName());
        Assert.assertEquals(2,configFileList.size());
        Assert.assertTrue(ccServerZKManager.getConfigFileNodeOper().configFileNodeExist(appInfoVO.getAppName(),appInfoVO.getEnvName(),configFileList.get(0).getFileName()));
        Assert.assertTrue(ccServerZKManager.getConfigFileNodeOper().configFileNodeExist(appInfoVO.getAppName(),appInfoVO.getEnvName(),configFileList.get(1).getFileName()));

        //回退
        //删除git中的配置文件
        File configFile1  = new File(iGitSV.getGitFullPath(appInfoVO,configFileList.get(0).getFileName()));
        File configFile2  = new File(iGitSV.getGitFullPath(appInfoVO,configFileList.get(1).getFileName()));
        Assert.assertTrue(configFile1.exists());
        Assert.assertTrue(configFile2.exists());
        /*iGitSV.deleteConfigFileAndCommit(appInfoVO,configFileList.get(0).getFileName());
        iGitSV.deleteConfigFileAndCommit(appInfoVO,configFileList.get(1).getFileName());*/
    }

    @Transactional
    @Rollback
    @Test
    public void upConfigFileForUpdate()throws Exception{
        //创建一个配置文件
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        UpOneConfigFileVO upOneConfigFileVO = new UpOneConfigFileVO();
        upOneConfigFileVO.setEnvId(appInfoVO.getEnvId());
        upOneConfigFileVO.setDesc("fileDesc");
        upOneConfigFileVO.setFileSaveDir("/config");

        UpConfigFileVO upConfigFileVO  = new UpConfigFileVO();
        upConfigFileVO.setName("test.txt");
        byte []fileData ={49,50,51};
        String fileContentStr = "test,"+Base64.encodeBase64String(fileData);
        upConfigFileVO.setUrl(fileContentStr);

        upOneConfigFileVO.setFile(upConfigFileVO);
        iConfigFileSV.upOneConfigFileForCreate(upOneConfigFileVO,ProjectConstants.ACCOUNT_ROOT);
        List<CcTaskEntity> taskList = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId = taskList.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);

        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(0));

        CcTaskEntity ccTaskEntity = taskList.get(0);
        ccTaskEntity.setStatus(ProjectConstants.STATUS_INVALID);
        taskRepository.save(ccTaskEntity);

        List<CcConfigFileEntity> configFileList = iConfigFileSV.getConfigFilesByEnvId(appInfoVO.getEnvId());
        Assert.assertEquals(1,configFileList.size());


        UpOneConfigFileForUpdateVO upOneConfigFileForUpdateVO = new UpOneConfigFileForUpdateVO();
        upOneConfigFileForUpdateVO.setConfigId(configFileList.get(0).getId());
        upOneConfigFileForUpdateVO.setDesc("newDesc");
        upOneConfigFileForUpdateVO.setEnvId(appInfoVO.getEnvId());
        UpConfigFileVO upConfigFileVOUpdate = new UpConfigFileVO();
        upConfigFileVOUpdate.setName("test.txt");
        byte [] newData = {52, 53, 54};
        upConfigFileVOUpdate.setUrl("test,"+Base64.encodeBase64String(newData));
        upOneConfigFileForUpdateVO.setFile(upConfigFileVOUpdate);
        iConfigFileSV.upConfigFileForUpdate(upOneConfigFileForUpdateVO,ProjectConstants.ACCOUNT_ROOT);

        List<CcTaskEntity> taskList1 = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId1 = taskList1.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList1 = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId1,ProjectConstants.STATUS_VALID);


        iConfigFileSV.saveOrUpdateFile(taskId1,appInfoVO.getEnvId(),detailList1.get(0));


        File configFile1  = new File(iGitSV.getGitFullPath(appInfoVO,configFileList.get(0).getFileName()));
        Assert.assertEquals("456",FileUtils.readFileToString(configFile1));
        Assert.assertTrue(configFile1.exists());
        iGitSV.deleteConfigFileAndCommit(appInfoVO,configFileList.get(0).getFileName());
    }

    @Transactional
    @Rollback
    @Test
    public void copyConfigFile()throws Exception{
        //************prepare start
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        AppInfoVO targetAppInfoVO = new AppInfoVO();
        targetAppInfoVO.setAppId(appInfoVO.getAppId());
        targetAppInfoVO.setAppName(appInfoVO.getAppName());
        targetAppInfoVO.setEnvName(JunitConstants.envName+2);
        iJunitSV.createEnv(targetAppInfoVO);
        //创建git仓库
        File gitFile1 = new File(iGitSV.getGitFullPath(appInfoVO,""));
        if(gitFile1.exists()){
            CcFileUtils.deleteDirectory(gitFile1);
            gitFile1.mkdirs();
        }
        iGitSV.initGitDir(gitFile1);
        File gitFile2 = new File(iGitSV.getGitFullPath(targetAppInfoVO,""));
        if(gitFile2.exists()){
            CcFileUtils.deleteDirectory(gitFile2);
            gitFile2.mkdirs();
        }
        iGitSV.initGitDir(gitFile2);

        iJunitSV.createConfigFile(appInfoVO);
        //************prepare end

        //复制文件
        List<CcConfigFileEntity> list = iConfigFileSV.getConfigFilesByEnvId(appInfoVO.getEnvId());
        Assert.assertNotNull(list);
        Assert.assertEquals(2,list.size());
        CopyConfigFileVO copyConfigFileVO = new CopyConfigFileVO();
        copyConfigFileVO.setEnvId(targetAppInfoVO.getEnvId());
        copyConfigFileVO.setOriginEnvId(appInfoVO.getEnvId());
        int [] configIds = {list.get(0).getId(),list.get(1).getId()};
        copyConfigFileVO.setConfigIds(configIds);
        iConfigFileSV.copyConfigFile(copyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);

        List<CcTaskEntity> taskList = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId = taskList.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);
        for(CcTaskDetailConfigEntity taskDetailConfigEntity:detailList){
            iConfigFileSV.saveOrUpdateFile(taskId,targetAppInfoVO.getEnvId(),taskDetailConfigEntity);
        }

        File file1 = new File(iGitSV.getGitFullPath(targetAppInfoVO,"test.txt"));
        File file2 = new File(iGitSV.getGitFullPath(targetAppInfoVO,"com/asiainfo/test.txt"));
        Assert.assertTrue(file1.exists());
        Assert.assertTrue(file2.exists());
        File gitFolder = new File(iGitSV.getGitFullPath(targetAppInfoVO,""));
        CcFileUtils.deleteDirectory(gitFolder);
    }

    @Test
    public void getConfigContents(){
        QueryConfigContentsVO queryConfigContentsVO = new QueryConfigContentsVO();
        queryConfigContentsVO.setEnvId(10000022);

        QueryConfigContentVO []queryConfigContentVO = new QueryConfigContentVO[1];
        QueryConfigContentVO queryConfigContentVO1 = new QueryConfigContentVO();
        queryConfigContentVO1.setConfigFileId(10000063);
        queryConfigContentVO[0] = queryConfigContentVO1;

        queryConfigContentsVO.setQueryConfigContentVO(queryConfigContentVO);
        List<ConfigFileContent> list = iConfigFileSV.getConfigContents(queryConfigContentsVO);
        System.out.println(1);
    }

    @Test
    public void test(){
        int envId = 10000022;
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        List<CcConfigFileEntity> list = iConfigFileSV.getConfigFilesByEnvId(envId);
        for(CcConfigFileEntity ccConfigFileEntity:list){
            System.out.println("*****************************************************************************************");            ;

            System.out.println(iGitSV.getFileContentByCommitName(appInfoVO,ccConfigFileEntity.getFileVersion(),ccConfigFileEntity.getFileName()));

            System.out.println("*****************************************************************************************");
        }

    }



}