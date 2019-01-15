package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.JunitConstants;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.AppRepository;
import com.asiainfo.configcenter.center.dao.repository.TaskDetailConfigRepository;
import com.asiainfo.configcenter.center.dao.repository.TaskRepository;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.entity.CcTaskEntity;
import com.asiainfo.configcenter.center.po.InstanceConfigPojo;
import com.asiainfo.configcenter.center.po.InstanceInfoPojo;
import com.asiainfo.configcenter.center.service.SV.IJunitSV;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.util.JSONUtil;
import com.asiainfo.configcenter.center.vo.env.AppEnvReqVO;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.app.AppReqVO;
import com.asiainfo.configcenter.center.vo.configfile.UpConfigFileVO;
import com.asiainfo.configcenter.center.vo.configfile.UpManyConfigFileVO;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * Created by oulc on 2018/7/30.
 */
@Service
public class JunitSVImpl implements IJunitSV {
    @Resource
    private IAppSV iAppSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private AppRepository appRepository;

    @Resource
    private IInstanceSV iInstanceSV;

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskDetailConfigRepository taskDetailConfigRepository;

    @Resource
    private IGitSV iGitSV;


    @Transactional
    @Override
    public int createApp()throws Exception{
        //如果zk节点存在 则删除
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName(JunitConstants.appName);
        createApp(appInfoVO);
        return appInfoVO.getAppId();
    }

    @Transactional
    @Override
    public int createEnv(int appId,String appName) throws Exception {
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppId(appId);
        appInfoVO.setEnvName(JunitConstants.envName);
        appInfoVO.setAppName(appName);
        createEnv(appInfoVO);
        return appInfoVO.getEnvId();
    }

    @Transactional
    @Override
    public AppInfoVO createAppAndEnv() throws Exception {
        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppName(JunitConstants.appName);
        appInfoVO.setAppId(createApp());

        appInfoVO.setEnvName(JunitConstants.envName);
        appInfoVO.setEnvId(createEnv(appInfoVO.getAppId(),JunitConstants.appName));

        return appInfoVO;
    }

    @Override
    public void createInstanceNode(AppInfoVO appInfoVO,int insNum) throws Exception {
        String insName  = JunitConstants.insName + insNum;

        InstanceInfoPojo insPojos = new InstanceInfoPojo();
        insPojos.setIp(JunitConstants.insIp);

        InstanceConfigPojo[] configPojos = new InstanceConfigPojo[2];

        InstanceConfigPojo configFilePojo = new InstanceConfigPojo();
        configFilePojo.setConfigType(1);
        configFilePojo.setConfigName(JunitConstants.configFileName);
        configFilePojo.setConfigVersion(JunitConstants.configFileVersion);
        configPojos[0] = configFilePojo;

        InstanceConfigPojo configItemPojo = new InstanceConfigPojo();
        configItemPojo.setConfigType(2);
        configItemPojo.setConfigName(JunitConstants.configItemName);
        configItemPojo.setConfigVersion(JunitConstants.configItemVersion);
        configPojos[1] = configItemPojo;

        insPojos.setInstanceConfigPojos(configPojos);

        ccServerZKManager.getInstanceNodeOper().createInstanceNode(appInfoVO.getAppName(),appInfoVO.getEnvName(),insName, JSONUtil.obj2JsonStr(insPojos));
    }

    @Transactional
    @Override
    public int  createInsDataBaseInfo(AppInfoVO appInfoVO, int insNum) throws Exception {
        return iInstanceSV.createInstance(appInfoVO.getAppName(),appInfoVO.getEnvName(),JunitConstants.insName + insNum,appInfoVO.getEnvId());
    }

    @Override
    public void rollBack() throws Exception {
        String appNodePath = "/apps/"+JunitConstants.appName;
        if(ccServerZKManager.getZKManager().existNode(appNodePath)){
            ccServerZKManager.getZKManager().deleteNodeRecursion(appNodePath);
        }

    }

    @Transactional
    @Override
    public int createInsAll(AppInfoVO appInfoVO, int insNum) throws Exception {
        createInstanceNode(appInfoVO,insNum);
        return createInsDataBaseInfo(appInfoVO,insNum);
    }

    @Override
    public void createApp(AppInfoVO appInfoVO) throws Exception {
        //如果zk节点存在 则删除
        String appNodePath = "/apps/"+appInfoVO.getAppName();
        if(ccServerZKManager.getZKManager().existNode(appNodePath)){
            ccServerZKManager.getZKManager().deleteNodeRecursion(appNodePath);
        }

        AppReqVO appReqVO = new AppReqVO();
        appReqVO.setAppName(appInfoVO.getAppName());
        appReqVO.setDescription(JunitConstants.appDesc);
        iAppSV.createApp(appReqVO, ProjectConstants.ACCOUNT_ROOT);
        int appId = appRepository.findByAppNameAndStatus(appInfoVO.getAppName(),ProjectConstants.STATUS_VALID).getId();
        appInfoVO.setAppId(appId);
    }

    private void deleteAllConfigFileFromGit(AppInfoVO appInfoVO){
        //如果git中存在目录直接删除
        File gitRootFile = new File(iGitSV.getGitFullPath(appInfoVO,""));
        File[] files = gitRootFile.listFiles();
        if(files != null && files.length > 0){
            for(File file:files){
                if(file.isDirectory()){
                    CcFileUtils.deleteDirectory(file);
                }else{
                    file.delete();
                }
            }
        }
    }

    @Transactional
    @Override
    public void createEnv(AppInfoVO appInfoVO) throws Exception {
        AppEnvReqVO appEnvReqVO = new AppEnvReqVO();
        appEnvReqVO.setAppId(appInfoVO.getAppId());
        appEnvReqVO.setEnvName(appInfoVO.getEnvName());
        appEnvReqVO.setDesc(JunitConstants.envDesc);
        int envId = iAppEnvSV.createAppEnv(appEnvReqVO, ProjectConstants.ACCOUNT_ROOT);
        appInfoVO.setEnvId(envId);
        deleteAllConfigFileFromGit(appInfoVO);
    }

    @Override
    public void createConfigFile(AppInfoVO appInfoVO) {
        UpManyConfigFileVO upManyConfigFileVO = new UpManyConfigFileVO();
        upManyConfigFileVO.setEnvId(appInfoVO.getEnvId());
        UpConfigFileVO upConfigFileVO = new UpConfigFileVO();
        upConfigFileVO.setName(JunitConstants.configZipName);
        upConfigFileVO.setUrl("test,"+ Base64.encodeBase64String(JunitConstants.configFileData));
        upManyConfigFileVO.setZipFile(upConfigFileVO);
        iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,ProjectConstants.ACCOUNT_ROOT);

        List<CcTaskEntity> taskList = taskRepository.findByCreatorAndStatus(ProjectConstants.ACCOUNT_ROOT,ProjectConstants.STATUS_VALID);
        int taskId = taskList.get(0).getId();
        List<CcTaskDetailConfigEntity> detailList = taskDetailConfigRepository.findByTaskIdAndStatusOrderByIdAsc(taskId,ProjectConstants.STATUS_VALID);
        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(0));
        iConfigFileSV.saveOrUpdateFile(taskId,appInfoVO.getEnvId(),detailList.get(1));

        //任务置为失效
        CcTaskEntity ccTaskEntity = taskList.get(0);
        ccTaskEntity.setStatus(ProjectConstants.STATUS_INVALID);
        taskRepository.save(ccTaskEntity);

    }
}
