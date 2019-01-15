package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.ConfigFileMapper;
import com.asiainfo.configcenter.center.dao.repository.ConfigFileRepository;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigFileEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.util.EncodingDetect;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configfile.*;
import com.asiainfo.configcenter.center.vo.task.TaskInfo;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oulc on 2018/7/25.
 * 配置文件业务层代码
 */
@Service
public class ConfigFileSVImpl implements IConfigFileSV{
    private static Logger logger = Logger.getLogger(ConfigFileSVImpl.class);

    @Resource
    private ConfigFileRepository configFileRepository;

    @Resource
    private IGitSV iGitSV;

    @Resource
    private ConfigFileMapper configFileMapper;

    @Resource
    private ITaskSV iTaskSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private INetDiskSV iNetDiskSV;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private IAppSV iAppSV;


    @Transactional
    @Override
    public void upOneConfigFileForCreate(UpOneConfigFileVO upOneConfigFileVO, int userId) {
        //基本校验
        File configRootFile = upOneConfigFileForCreateCheckParam(upOneConfigFileVO);

        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(upOneConfigFileVO.getEnvId());

        uploadConfigFileForCreate(configRootFile,upOneConfigFileVO.getStrategyId(),configRootFile.getAbsolutePath(),userId,appInfoVO,upOneConfigFileVO.getDesc());

        CcFileUtils.deleteDirectory(configRootFile);
    }


    @Transactional
    @Override
    public void upManyConfigFileForCreate(UpManyConfigFileVO upManyConfigFileVO, int userId) {
        //基本校验 解压后校验是否重名
        File configRootFile = upManyConfigFileForCreateCheckParam(upManyConfigFileVO);

        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(upManyConfigFileVO.getEnvId());

        uploadConfigFileForCreate(configRootFile,null,configRootFile.getAbsolutePath(),userId,appInfoVO,null);

        CcFileUtils.deleteDirectory(configRootFile);
    }

    @Transactional
    @Override
    public void upConfigFileForUpdate(UpOneConfigFileForUpdateVO upOneConfigFileForUpdateVO, int userId) {
        File configRootFile =  upConfigFileForUpdateCheckParam(upOneConfigFileForUpdateVO);

        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(upOneConfigFileForUpdateVO.getEnvId());

        uploadConfigFileForUpdate(configRootFile,upOneConfigFileForUpdateVO.getStrategyId(),configRootFile.getAbsolutePath(),upOneConfigFileForUpdateVO.getConfigId(),userId,appInfoVO,upOneConfigFileForUpdateVO.getDesc());

        CcFileUtils.deleteDirectory(configRootFile);
    }



    /**
     * 校验配置文件是否已经重复
     * @author oulc
     * @date 2018/8/6 10:43
     * @param file 配置文件(可能是配置文件，可能是配置文件目录)
     * @param envId 环境主键
     * @param rootPath 配置文件的根目录
     */
    private void checkConfigExist(File file,int envId,String rootPath){
        if(file.isDirectory()){
            File [] childFiles = file.listFiles();
            if(childFiles != null && childFiles.length >0){
                for(File childFile:childFiles){
                    checkConfigExist(childFile,envId,rootPath);
                }
            }
        }else{
            //校验文件
            String fileFullName = CcFileUtils.getRelativeFilePath(file.getAbsolutePath(),rootPath);
            Assert4CC.isTrue( getConfigFileByEnvIdAndFileName(envId, fileFullName) == null
                             , ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件：" + fileFullName + "已经存在同名的配置文件");
        }
    }

    @Transactional
    @Override
    public void uploadConfigFileForCreate(File configFile, Integer strategyId, String configFileRootPath,int userId,AppInfoVO appInfoVO,String fileDesc){
        if(configFile.isDirectory()){
            File [] files = configFile.listFiles();
            if(files != null && files.length > 0){
                for(File childFile:files){
                    uploadConfigFileForCreate(childFile,strategyId,configFileRootPath,userId,appInfoVO,fileDesc);
                }
            }
        }else{
            String configFileFullName = CcFileUtils.getRelativeFilePath(configFile.getAbsolutePath(),configFileRootPath);

            TaskInfo taskInfo = iTaskSV.createTaskForConfigFile(appInfoVO.getEnvId(),userId,ProjectConstants.CONFIG_CHANGE_TYPE_ADD,null,
                    strategyId,configFileFullName,null,fileDesc);

            iNetDiskSV.saveConfigFileToNetDisk(configFile,configFileRootPath,appInfoVO,taskInfo);
        }
    }

    @Transactional
    @Override
    public void uploadConfigFileForUpdate(File configFile, Integer strategyId, String configFileRootPath, int configId,int userId, AppInfoVO appInfoVO, String fileDesc) {
        if(configFile.isDirectory()){
            File [] files = configFile.listFiles();
            if(files != null && files.length > 0){
                for(File childFile:files){
                    uploadConfigFileForUpdate(childFile,strategyId,configFileRootPath,configId,userId,appInfoVO,fileDesc);
                }
            }
        }else{
            String configFileFullName = CcFileUtils.getRelativeFilePath(configFile.getAbsolutePath(),configFileRootPath);

            TaskInfo taskInfo = iTaskSV.createTaskForConfigFile(appInfoVO.getEnvId(),userId,ProjectConstants.CONFIG_CHANGE_TYPE_MOD,configId,
                    strategyId,configFileFullName,null,fileDesc);

            iNetDiskSV.saveConfigFileToNetDisk(configFile,configFileRootPath,appInfoVO,taskInfo);
        }
    }

    @Transactional
    @Override
    public void deleteConfigFile(DelConfigFileVO configFileVO, int userId) {
        Assert4CC.isTrue(configFileVO.getConfigIds() != null && configFileVO.getConfigIds().length != 0,ResultCodeEnum.CONFIG_FILE_DELETE_ERROR,"至少要选择一个配置文件");
        Assert4CC.isTrue(configFileVO.getEnvId() !=0 ,"环境主键不能为空");

        int []configIds = configFileVO.getConfigIds();
        CcConfigFileEntity [] ccConfigFileEntity = new CcConfigFileEntity[configIds.length];
        for(int i = 0 ; i <configIds.length ;i++){
            Assert4CC.isTrue(configIds[i] !=0 ,"配置文件主键不能为空");
            ccConfigFileEntity[i] = getConfigFileByIdCheck(configIds[i]);
        }

        for(int i = 0 ; i <configIds.length ;i++){
             iTaskSV.createTaskForConfigFile(configFileVO.getEnvId(),
                    userId,
                    ProjectConstants.CONFIG_CHANGE_TYPE_DEL,
                    configIds[i],
                    null,
                    ccConfigFileEntity[i].getFileName(),
                    null,
                    configFileVO.getReason());
        }
    }





    /**
     * 任务审核通过回调(新增、修改、删除配置文件)
     * @author oulc
     * @date 2018/8/3 9:51
     * @param taskId 任务主键
     * @param envId 环境主键
     * @param taskDetailConfigEntity 任务实体
     */
    @Transactional
    @Override
    public void saveOrUpdateFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity) {
        byte changeType = taskDetailConfigEntity.getChangeType();
        switch (changeType){
            case ProjectConstants.CONFIG_CHANGE_TYPE_ADD:
                //新增配置文件
                createConfigFile(taskId, envId, taskDetailConfigEntity);
                break;
            case ProjectConstants.CONFIG_CHANGE_TYPE_MOD:
                //修改配置文件
                updateConfigFile(taskId, envId, taskDetailConfigEntity);
                break;
            case ProjectConstants.CONFIG_CHANGE_TYPE_DEL:
                //删除配置文件
                deleteConfigFile(taskId, envId, taskDetailConfigEntity);
                break;
            case ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE:
                changeConfigFileStrategy(taskDetailConfigEntity);
                break;
            default:break;
        }
    }

    /**
     * 变更配置文件刷新策略
     * @author bawy
     * @date 2018/8/22 15:59
     * @param taskDetailConfigEntity 任务详情
     */
    @Transactional
    public void changeConfigFileStrategy(CcTaskDetailConfigEntity taskDetailConfigEntity){
        int configId = taskDetailConfigEntity.getConfigId();
        CcConfigFileEntity configFileEntity = configFileRepository.findByIdAndStatus(configId, ProjectConstants.STATUS_VALID);
        configFileEntity.setStrategyId(taskDetailConfigEntity.getStrategyId());
        configFileEntity.setModifier(taskDetailConfigEntity.getModifier());
        configFileEntity.setUpdateTime(TimeUtil.currentTime());
        configFileRepository.save(configFileEntity);
    }


    @Transactional
    @Override
    public int createConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity) {
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);

        //保存至git
        String fileVersion = iGitSV.copyFileToGitAndCommit(taskId,taskDetailConfigEntity.getId(),appInfoVO);

        //创建zk信息
        createConfigFileZKNode(appInfoVO.getAppName(),appInfoVO.getEnvName(),taskDetailConfigEntity.getConfigName(),"");

        //保存至数据库
        int configId = createConfigFileDataToBaseInfo(envId,taskDetailConfigEntity.getConfigName(),taskDetailConfigEntity.getStrategyId(),taskDetailConfigEntity.getConfigDesc(),fileVersion,taskDetailConfigEntity.getCreator());

        //删除网盘文件
        iNetDiskSV.deleteConfigFileFromNetDisk(taskId,taskDetailConfigEntity.getId());

        return configId;
    }



    @Transactional
    @Override
    public int createConfigFileDataToBaseInfo(int envId, String fileName, Integer strategyId, String fileDesc, String fileVersion, int creator) {
        CcConfigFileEntity ccConfigFileEntity = new CcConfigFileEntity();
        ccConfigFileEntity.setAppEnvId(envId);
        ccConfigFileEntity.setFileName(fileName);
        ccConfigFileEntity.setStrategyId(strategyId);
        ccConfigFileEntity.setFileVersion(fileVersion);
        ccConfigFileEntity.setDescription(fileDesc);
        ccConfigFileEntity.setCreator(creator);
        ccConfigFileEntity.setCreateTime(TimeUtil.currentTime());
        ccConfigFileEntity.setModifier(creator);
        ccConfigFileEntity.setUpdateTime(TimeUtil.currentTime());
        ccConfigFileEntity.setStatus(ProjectConstants.STATUS_VALID);
        configFileRepository.save(ccConfigFileEntity);
        return ccConfigFileEntity.getId();
    }

    @Transactional
    @Override
    public void updateConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity) {
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);

        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdCheck(taskDetailConfigEntity.getConfigId());

        String fileVersion = iGitSV.copyFileToGitAndCommit(taskId,taskDetailConfigEntity.getId(),appInfoVO);

        updateConfigFileDataBaseInfo(ccConfigFileEntity,taskDetailConfigEntity.getCreator(),taskDetailConfigEntity.getConfigDesc(),fileVersion);

        iNetDiskSV.deleteConfigFileFromNetDisk(taskId,taskDetailConfigEntity.getId());
    }

    @Transactional
    @Override
    public void updateConfigFileDataBaseInfo(CcConfigFileEntity ccConfigFileEntity, int userId, String fileDesc, String fileVersion) {
        ccConfigFileEntity.setFileVersion(fileVersion);
        ccConfigFileEntity.setDescription(fileDesc);
        ccConfigFileEntity.setUpdateTime(TimeUtil.currentTime());
        ccConfigFileEntity.setModifier(userId);
        configFileRepository.save(ccConfigFileEntity);
    }

    @Transactional
    @Override
    public void deleteConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity) {
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);

        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdCheck(taskDetailConfigEntity.getConfigId());

        //删除配置文件 如果文件删除之后没有任何其他文件 则直接删除整个目录
        iGitSV.deleteConfigFileAndCommit(appInfoVO,ccConfigFileEntity.getFileName());

        //从数据库中删除配置文件
        deleteConfigFileDataBaseInfo(ccConfigFileEntity,taskDetailConfigEntity.getCreator());

        //删除zk信息
        deleteConfigFileZKNode(appInfoVO.getAppName(),appInfoVO.getEnvName(),taskDetailConfigEntity.getConfigName());
    }

    @Transactional
    @Override
    public void deleteConfigFileDataBaseInfo(CcConfigFileEntity ccConfigFileEntity,int userId) {
        ccConfigFileEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccConfigFileEntity.setModifier(userId);
        ccConfigFileEntity.setUpdateTime(TimeUtil.currentTime());
        configFileRepository.save(ccConfigFileEntity);
    }

    @Override
    public CcConfigFileEntity getConfigFileByEnvIdAndFileName(int envId, String configFileName) {
        return configFileRepository.findByAppEnvIdAndFileNameAndStatus(envId,configFileName,ProjectConstants.STATUS_VALID);
    }

    @Override
    public PageResultContainer<CXCcConfigFileEntity> getConfigFiles(PageRequestContainer<QueryConfigFileVO> pageRequestContainer) {
        //校验基本参数
        getConfigFilesCheckParam(pageRequestContainer);

        PageResultContainer<CXCcConfigFileEntity> pageResultContainer = new PageResultContainer<>();
        QueryConfigFileVO queryConfigFileVO = pageRequestContainer.getData();
        int envId = queryConfigFileVO.getEnvId();
        String fileName = queryConfigFileVO.getFileName();
        String creatorName = queryConfigFileVO.getCreatorName();
        int currentPage = pageRequestContainer.getCurrentPage();
        int size = pageRequestContainer.getPageSize();
        String startDate = queryConfigFileVO.getStartDate()== 0 ? null : TimeUtil.timeFormat(queryConfigFileVO.getStartDate());
        String endDate = queryConfigFileVO.getEndDate() == 0 ? null : TimeUtil.timeFormat(queryConfigFileVO.getEndDate());

        //查询数据
        List<CXCcConfigFileEntity> ccConfigFileEntities = configFileMapper.findConfigFile(envId,fileName,creatorName,startDate,endDate,currentPage*size,size);
        //查询数据长度
        long count = configFileMapper.findConfigFileCount(envId, fileName, creatorName, startDate, endDate);

        pageResultContainer.setEntities(ccConfigFileEntities);
        pageResultContainer.setCount(count);
        return pageResultContainer;
    }




    @Override
    public CcConfigFileEntity getConfigFileById(int id){
        return configFileRepository.findByIdAndStatus(id,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcConfigFileEntity getConfigFileByIdCheck(int id){
        CcConfigFileEntity ccConfigFileEntity = getConfigFileById(id);
        Assert4CC.notNull(ccConfigFileEntity,ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件不存在，配置文件主键:"+id);
        return ccConfigFileEntity;
    }

    @Override
    public CcConfigFileEntity getConfigFileByIdAndEnvId(int configId, int envId) {
        return configFileRepository.findByIdAndAppEnvIdAndStatus(configId,envId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcConfigFileEntity getConfigFileByIdAndEnvIdCheck(int configId, int envId) {
        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdAndEnvId(configId,envId);
        Assert4CC.notNull(ccConfigFileEntity,ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件不存在，配置文件主键:" + configId);
        return ccConfigFileEntity;
    }

    @Override
    public ConfigFileContent getLastVersion(int configId, int envId) {
        Assert4CC.isTrue(configId>0, "配置文件标识不可为空");
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        CcConfigFileEntity configFileEntity = getConfigFileByIdAndEnvIdCheck(configId, envId);
        ConfigFileContent configFileContent = new ConfigFileContent();
        configFileContent.setConfigFileId(configFileEntity.getId());
        configFileContent.setConfigFileName(configFileEntity.getFileName());
        configFileContent.setConfigFileVersion(configFileEntity.getFileVersion());
        configFileContent.setConfigFileContent(iGitSV.getFileContentByCommitName(appInfoVO,configFileContent.getConfigFileVersion(),configFileEntity.getFileName()));
        configFileContent.setStrategyId(configFileEntity.getStrategyId());
        return configFileContent;
    }

    @Override
    public ConfigFileContent getTempFileContent(int envId, int taskDetailId) {
        Assert4CC.isTrue(taskDetailId>0, "任务详情标识不可为空");
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        CcTaskDetailConfigEntity taskDetailConfigEntity = iTaskSV.getConfigChangeTaskInfo(envId, taskDetailId);
        String fileName = taskDetailConfigEntity.getConfigName();
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        String fileStr = iNetDiskSV.getTempFileContent(appInfoVO.getAppName(), appInfoVO.getEnvName(), taskDetailConfigEntity.getTaskId(), taskDetailId, fileName);
        ConfigFileContent fileContent = new ConfigFileContent();
        fileContent.setConfigFileName(fileName);
        fileContent.setConfigFileContent(fileStr);
        return fileContent;
    }

    @Override
    public void createConfigFileZKNode(String projectName, String envName, String configFileName, String value) {
        try {
            if(ccServerZKManager.getConfigFileNodeOper().configFileNodeExist(projectName, envName, configFileName)){
                throw new ErrorCodeException(ResultCodeEnum.ZK_COMMON_ERROR,"在zookeeper中创建配置文件数据节点失败,配置文件节点已经存在:" + configFileName);
            }
            ccServerZKManager.getConfigFileNodeOper().createConfigFileNode(projectName,envName,configFileName,value);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    @Override
    public void deleteConfigFileZKNode(String projectName, String envName, String configFileName) {
        try {
            if(! ccServerZKManager.getConfigFileNodeOper().configFileNodeExist(projectName, envName, configFileName)){
                throw new ErrorCodeException(ResultCodeEnum.ZK_COMMON_ERROR,"在zookeeper中删除配置文件数据节点失败,配置文件节点不存在:" + configFileName);
            }
            ccServerZKManager.getConfigFileNodeOper().deleteConfigFileNode(projectName,envName,configFileName);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    @Override
    public List<CcConfigFileEntity> getConfigFilesByEnvId(int envId) {
        return configFileRepository.findByAppEnvIdAndStatus(envId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public void rollback(int taskId, CcTaskDetailConfigEntity ccTaskDetailConfigEntity) {
        //直接删除网盘里的文件
        iNetDiskSV.deleteConfigFileFromNetDisk(taskId,ccTaskDetailConfigEntity.getId());
    }

    @Transactional
    @Override
    public void copyConfigFile(CopyConfigFileVO copyConfigFileVO , int userId){
        copyConfigFileCheckParam(copyConfigFileVO);
        AppInfoVO originAppInfoVO = iAppEnvSV.getAppInfoByEnvId(copyConfigFileVO.getOriginEnvId());
        AppInfoVO targetAppInfo = iAppEnvSV.getAppInfoByEnvId(copyConfigFileVO.getEnvId());
        Assert4CC.isTrue(originAppInfoVO.getAppId() == targetAppInfo.getAppId(),"源环境和目标环境必须是同一个应用");
        //获取文件
        int[] configIds = copyConfigFileVO.getConfigIds();
        if(configIds != null && configIds.length > 0){
            for(int configId : configIds){
                CcConfigFileEntity configFileEntity = getConfigFileByIdCheck(configId);
                File configFile = new File(iGitSV.getGitFullPath(originAppInfoVO,configFileEntity.getFileName()));
                uploadConfigFileForCreate(configFile,configFileEntity.getStrategyId(),iGitSV.getGitFullPath(originAppInfoVO,""),userId,targetAppInfo,"copy from "+targetAppInfo.getEnvName());
            }
        }
    }



    @Override
    public List<ConfigFileContent> getConfigContents(QueryConfigContentsVO queryConfigContentsVO) {
        ArrayList<ConfigFileContent> result = new ArrayList<>();

        getConfigContentsCheckParam(queryConfigContentsVO);

        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(queryConfigContentsVO.getEnvId());

        QueryConfigContentVO[] queryConfigContentVOs = queryConfigContentsVO.getQueryConfigContentVO();

        for(QueryConfigContentVO queryConfigContentVO : queryConfigContentVOs){
            result.add( getConfigContents(queryConfigContentVO,appInfoVO ));
        }

        return result;
    }



    @Override
    public ConfigFileContent getConfigContents(QueryConfigContentVO queryConfigContentVO,AppInfoVO appInfoVO) {
        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdCheck(queryConfigContentVO.getConfigFileId());
        String configFileVersion = StringUtils.isNotBlank(queryConfigContentVO.getConfigFileVersion())?queryConfigContentVO.getConfigFileVersion():ccConfigFileEntity.getFileVersion();

        ConfigFileContent configFileContent = new ConfigFileContent();
        configFileContent.setConfigFileId(queryConfigContentVO.getConfigFileId());
        configFileContent.setConfigFileName(ccConfigFileEntity.getFileName());
        configFileContent.setConfigFileVersion(configFileVersion);
        configFileContent.setConfigFileContent(iGitSV.getFileContentByCommitName(appInfoVO,configFileVersion,ccConfigFileEntity.getFileName()));

        return configFileContent;
    }

    @Transactional
    @Override
    public void editConfigContent(EditConfigContentVO editConfigContentVO, int userId) {
        //基本校验
        editConfigContentCheckParam(editConfigContentVO);
        //查询配置文件
        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdAndEnvIdCheck(editConfigContentVO.getConfigId(),editConfigContentVO.getEnvId());
        String filePath = ccConfigFileEntity.getFileName();
        //查询应用信息
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(editConfigContentVO.getEnvId());
        //获取git仓库中的文件
        File configFileInGit = iGitSV.getFileByFileNameCheck(appInfoVO,filePath);
        String encode = EncodingDetect.getJavaEncode(configFileInGit.getAbsolutePath());
        //文件保存到临时目录
        File tempDir = CcFileUtils.writeStringToTempDir(editConfigContentVO.getConfigFileContent(),filePath,encode);
        //放进待提交任务
        uploadConfigFileForUpdate(tempDir,ccConfigFileEntity.getStrategyId(),tempDir.getAbsolutePath(),editConfigContentVO.getConfigId(),userId,appInfoVO,"");
    }

    @Override
    public List<ConfigHisVo> getConfigFileHis(int configId,int envId){
        getConfigFileHisCheckParam(configId, envId);
        CcConfigFileEntity ccConfigFileEntity = getConfigFileByIdAndEnvIdCheck(configId,envId);
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        return iGitSV.getFileHis(appInfoVO,ccConfigFileEntity.getFileName());
    }

    @Override
    public File downLoadEnvConfigFiles(int envId,int userId) {
        Assert4CC.isTrue(envId != 0,"环境主键不能为空");
        //校验用户是否有权限下载
        CcAppUserRelEntity ccAppUserRelEntity = iAppSV.getAppRoleByEnvId(envId, userId);
        Assert4CC.notNull(ccAppUserRelEntity,ResultCodeEnum.APP_COMMON_ERROR,"用户没有权限下载,用户不是该应用成员");
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        File dirFile  = new File(iGitSV.getGitFullPath(appInfoVO,""));
        Assert4CC.isTrue(dirFile.exists(),ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件git仓库目录不存在:envId:"+envId);
        return CcFileUtils.zip(dirFile).getFile();
    }

    @Override
    public EnvAllConfigFileRepVO getProjectAllConfigFile(int envId) {
        Assert4CC.isTrue(envId != 0,"环境主键不能为空");

        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        File dirFile  = new File(iGitSV.getGitFullPath(appInfoVO,""));
        Assert4CC.isTrue(dirFile.exists(),ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件git仓库目录不存在:envId:"+envId);
        File zipFile = CcFileUtils.zip(dirFile).getFile();
        EnvAllConfigFileRepVO envAllConfigFileRepVO = new EnvAllConfigFileRepVO();
        envAllConfigFileRepVO.setConfigFileZipData(CcFileUtils.readFileToByteArray(zipFile));
        Assert4CC.isTrue(zipFile.delete(),"临时文件删除失败:"+zipFile.getAbsolutePath());
        return envAllConfigFileRepVO;
    }

    @Override
    @Transactional
    public void changeStrategy(ChangeStrategyReqVO changeStrategyReq, int modifier) {
        int envId = changeStrategyReq.getEnvId();
        int configId = changeStrategyReq.getConfigId();
        Integer strategyId = changeStrategyReq.getStrategyId();
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        Assert4CC.isTrue(configId>0, "配置文件标识不可为空");
        Assert4CC.isTrue(strategyId!=null&&strategyId>0, "刷新策略不可为空");
        CcConfigFileEntity configFileEntity = configFileRepository.findByIdAndStatus(configId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(configFileEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "此环境中不存在配置文件标识为“"+configId+"”的配置文件");
        iTaskSV.createTaskForConfigFile(envId, modifier, ProjectConstants.CONFIG_CHANGE_TYPE_STRATEGY_CHANGE, configId, strategyId, configFileEntity.getFileName(), null, null);
    }

    @Override
    public List<CcConfigFileEntity> getConfigFileByStrategyId(int strategyId) {
        return configFileRepository.findByStrategyIdAndStatus(strategyId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public String[] getConfigFileNamesByStrategyId(int strategyId) {
        List <CcConfigFileEntity> configFileEntityList = getConfigFileByStrategyId(strategyId);
        if(configFileEntityList != null && configFileEntityList.size() > 0){
            String []configFileNames = new String[configFileEntityList.size()];
            for( int i = 0 ;i < configFileNames.length ;i ++){
                configFileNames[i] = configFileEntityList.get(i).getFileName();
            }
            return configFileNames;
        }
        return null;
    }

    //******************************************************************************参数校验/

    private File upOneConfigFileForCreateCheckParam(UpOneConfigFileVO upOneConfigFileVO){
        Assert4CC.isTrue(upOneConfigFileVO.getEnvId() != 0 ,"环境主键不能为空");
        UpConfigFileVO upConfigFileVO = upOneConfigFileVO.getFile();
        Assert4CC.notNull(upConfigFileVO,"配置文件信息不能为空");
        Assert4CC.hasLength(upConfigFileVO.getName(),"文件名称不能为空");
        Assert4CC.hasLength(upConfigFileVO.getUrl(),"文件内容不能为空");
        String []fileContentStrs =  upConfigFileVO.getUrl().split(",");
        Assert4CC.isTrue(fileContentStrs.length == 2,"文件格式不正确");
        //把文件复制到临时文件夹
        byte[] configData = Base64.decodeBase64(fileContentStrs[1]);
        String fileSaveDir = upOneConfigFileVO.getFileSaveDir() == null ? "" : upOneConfigFileVO.getFileSaveDir();
        File tempDir = CcFileUtils.writeByteArrayToTempDir(configData,CcFileUtils.concatFilePath(fileSaveDir,upConfigFileVO.getName()));
        checkConfigExist(tempDir,upOneConfigFileVO.getEnvId(),tempDir.getAbsolutePath());
        return tempDir;
    }

    private File upConfigFileForUpdateCheckParam(UpOneConfigFileForUpdateVO upOneConfigFileForUpdateVO){
        Assert4CC.isTrue(upOneConfigFileForUpdateVO.getEnvId() != 0 ,"环境主键不能为空");
        Assert4CC.isTrue(upOneConfigFileForUpdateVO.getConfigId() != 0,"配置文件主键不能为空");
        UpConfigFileVO upConfigFileVO = upOneConfigFileForUpdateVO.getFile();
        Assert4CC.hasLength(upConfigFileVO.getName(),"文件名称不能为空");
        Assert4CC.hasLength(upConfigFileVO.getUrl(),"文件内容不能为空");
        String []fileContentStrs =  upConfigFileVO.getUrl().split(",");
        Assert4CC.isTrue(fileContentStrs.length == 2,"文件格式不正确");
        byte[] configData = Base64.decodeBase64(fileContentStrs[1]);
        CcConfigFileEntity ccConfigFileEntity =  getConfigFileByIdAndEnvIdCheck(upOneConfigFileForUpdateVO.getConfigId(),upOneConfigFileForUpdateVO.getEnvId());
        //校验配置文件名称
        String configFileName = upConfigFileVO.getName();
        Assert4CC.isTrue(CcFileUtils.getFileSampleName(ccConfigFileEntity.getFileName()).equals(configFileName),"上传的配置文件名称和需要修改的配置文件名称不一致");
        //复制配置文件至临时文件
        return CcFileUtils.writeByteArrayToTempDir(configData,ccConfigFileEntity.getFileName());
    }

    private File upManyConfigFileForCreateCheckParam(UpManyConfigFileVO upManyConfigFileVO){
        Assert4CC.isTrue(upManyConfigFileVO.getEnvId() != 0 ,"环境主键不能为空");
        UpConfigFileVO upConfigFileVO = upManyConfigFileVO.getZipFile();
        Assert4CC.notNull(upConfigFileVO,"压缩包内容不能全部为空");
        Assert4CC.hasLength(upConfigFileVO.getName(),"压缩文件名称不能为空");
        Assert4CC.hasLength(upConfigFileVO.getUrl(),"压缩文件内容不能为空");
        String []fileContentStrs =  upConfigFileVO.getUrl().split(",");
        Assert4CC.isTrue(fileContentStrs.length == 2,"文件格式不正确");
        //压缩包复制到临时文件夹中
        File tempZipFile = CcFileUtils.createTempFile("zip");
        CcFileUtils.writeByteArrayToFile(tempZipFile,Base64.decodeBase64(fileContentStrs[1]));
        //解压缩包 并直接删除压缩包
        File tempConfigDir = new File(CcFileUtils.unzip(tempZipFile));
        //判断配置文件名称没有重复
        checkConfigExist(tempConfigDir,upManyConfigFileVO.getEnvId(),tempConfigDir.getAbsolutePath());
        return tempConfigDir;
    }

    private void getConfigFilesCheckParam(PageRequestContainer<QueryConfigFileVO> pageRequestContainer){
        ValidateUtil.checkPageParam(pageRequestContainer);
        QueryConfigFileVO queryConfigFileVO = pageRequestContainer.getData();
        Assert4CC.isTrue(queryConfigFileVO.getEnvId() != 0 ,"环境主键不能为空");
        long startDate = queryConfigFileVO.getStartDate();
        long endDate = queryConfigFileVO.getEndDate();
        if(startDate != 0 &&  endDate!= 0){
            Assert4CC.isTrue(startDate < endDate,"开始时间不能大于结束时间");
        }
    }

    private void copyConfigFileCheckParam(CopyConfigFileVO copyConfigFileVO){
        Assert4CC.isTrue(copyConfigFileVO.getOriginEnvId() != 0,"源环境不能为空");
        Assert4CC.isTrue(copyConfigFileVO.getEnvId() != 0,"目标环境不能为空");
        Assert4CC.isTrue(copyConfigFileVO.getConfigIds() != null && copyConfigFileVO.getConfigIds().length > 0,"配置文件不能为空");
    }

    private void getConfigContentsCheckParam(QueryConfigContentsVO queryConfigContentsVO){
        Assert4CC.isTrue(queryConfigContentsVO.getEnvId() != 0,"环境主键不能为空");
        QueryConfigContentVO [] queryConfigContentVOS = queryConfigContentsVO.getQueryConfigContentVO();
        Assert4CC.isTrue(queryConfigContentVOS != null && queryConfigContentVOS.length >0,"配置文件信息不能为空" );
        for(QueryConfigContentVO queryConfigContentVO : queryConfigContentVOS){
            Assert4CC.isTrue(queryConfigContentVO.getConfigFileId() != 0,"配置文件主键不能为空");
        }
    }

    private void editConfigContentCheckParam(EditConfigContentVO editConfigContentVO){
        Assert4CC.isTrue(editConfigContentVO.getEnvId()!= 0,"环境主键不能为空");
        Assert4CC.isTrue(editConfigContentVO.getConfigId()!= 0,"配置文件主键不能为空");
        Assert4CC.hasLength(editConfigContentVO.getConfigFileContent(),"配置文件内容不能为空");
    }

    private void getConfigFileHisCheckParam(int configId,int envId){
        Assert4CC.isTrue(configId != 0,"配置主键不能为空");
        Assert4CC.isTrue(envId != 0,"环境主键不能为空");
    }
    //******************************************************************************参数校验/


}
