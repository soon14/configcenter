package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.service.interfaces.IGitSV;
import com.asiainfo.configcenter.center.service.interfaces.INetDiskSV;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.util.EncodingDetect;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configfile.ConfigHisVo;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oulc on 2018/8/1.
 * gitLab业务层代码
 */
@Service
public class GitSVImpl implements IGitSV {
    private static Logger logger = Logger.getLogger(GitSVImpl.class);

    @Value("${config.project.git.dir}")
    private String configProjectGitPath;

    @Value("${config.project.netdisk.dir}")
    private String configProjectNetDiskPath;

    @Resource
    private INetDiskSV iNetDiskSV;

    /**
     * 添加配置文件 并提交
     * @author oulc
     * @date 2018/8/8 14:54
     * @param filePath 文件路径 相对路径
     * @param appInfoVO 应用环境信息
     */
    @Override
    public String copyFileToGitAndCommit(String filePath,AppInfoVO appInfoVO,ArrayList<String> relativePath){
        //复制文件
        copyFileToGit(filePath, appInfoVO);
        //提交文件
        gitAddFile(filePath, appInfoVO,relativePath);
        //commit文件
        return gitCommitFile(appInfoVO);
    }



    @Override
    public void copyFileToGit(String filePath,AppInfoVO appInfoVO){
        File originFile = new File(filePath);
        Assert4CC.isTrue(originFile.exists(),ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件/目录:" + filePath + "不存在");
        File targetDir = new File(getGitFullPath(appInfoVO,""));
        if(originFile.isDirectory()){
            CcFileUtils.copyDirectoryToDirectory(originFile,targetDir);
        }else{
            CcFileUtils.copyFileToDirectory(originFile,targetDir);
        }
    }

    @Override
    public String getGitFullPath(AppInfoVO appInfoVO, String filePath) {
        return CcFileUtils.concatFilePath(configProjectGitPath,appInfoVO.getAppName(),appInfoVO.getEnvName(),filePath);
    }

    @Override
    public void gitAddFile(String fileName,AppInfoVO appInfoVO,ArrayList<String> relativePaths){
        try {
            Git git = getGit(appInfoVO);
            AddCommand addCommand = git.add();
            for(String relativePath : relativePaths){
                addCommand.addFilepattern(relativePath);
            }
            addCommand.call();
        }catch ( GitAPIException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
        }
    }

    @Override
    public String gitCommitFile(AppInfoVO appInfoVO){
        try {
            Git git = getGit(appInfoVO);
            return git.commit().setMessage("commitConfigFile").call().getName();
        }catch (GitAPIException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
        }
    }

    @Override
    public void deleteConfigFileAndCommit(AppInfoVO appInfoVO,String filePath){
        //删除配置文件
        deleteConfigFile(appInfoVO,filePath);
        //提交
        gitCommitFile(appInfoVO);
    }

    @Override
    public void deleteConfigFile(AppInfoVO appInfoVO,String filePath){
        String fileFullPath = getGitFullPath(appInfoVO,filePath);
        File file = new File(fileFullPath);
        Assert4CC.isTrue(file.exists(),ResultCodeEnum.CONFIG_COMMON_ERROR,"文件不存在:" + fileFullPath);
        deleteConfigFileRepeat(file);
    }

    @Override
    public void deleteConfigFileRepeat(File file){
        File parentFile = file.getParentFile();
        Assert4CC.isTrue(file.delete(),ResultCodeEnum.CONFIG_COMMON_ERROR,"文件删除失败:"+file.getAbsolutePath());
        if(parentFile != null){
            String  [] childFiles = parentFile.list();
            if(childFiles == null || childFiles.length == 0){
                deleteConfigFileRepeat(parentFile);
            }
        }
    }

    @Override
    public String copyFileToGitAndCommit(int taskId,int detailId,AppInfoVO appInfoVO){
        File configDirFile = new File(iNetDiskSV.getNetDiskFullFilePath(appInfoVO, CcFileUtils.concatFilePath(""+taskId,""+detailId)));
        Assert4CC.isTrue(configDirFile.exists(),ResultCodeEnum.CONFIG_COMMON_ERROR,"配置文件目录:" + configDirFile.getAbsolutePath() + "不存在");
        Assert4CC.isTrue(configDirFile.isDirectory(),"文件："+configDirFile.getAbsolutePath()+"不是目录");

        String filesName[] = configDirFile.list();
        Assert4CC.isTrue(filesName != null && filesName.length == 1,ResultCodeEnum.CONFIG_COMMON_ERROR,"任务详情目录:"+configDirFile.getAbsolutePath()+"下必须且只能存在一个文件或者目录");

        ArrayList<String> leafFiles = new ArrayList<>();
        CcFileUtils.getChildLeafFiles(configDirFile,leafFiles);
        ArrayList<String> relativePath =  iNetDiskSV.getRelativeConfigFiles(leafFiles,appInfoVO,taskId,detailId);

        return copyFileToGitAndCommit(CcFileUtils.concatFilePath(configDirFile.getAbsolutePath(),filesName[0]),appInfoVO,relativePath);
    }

    @Override
    public String getFileContentByCommitName(AppInfoVO appInfoVO , String commitName,String fileName){
        try {
            getFileByFileNameCheck(appInfoVO,fileName);//校验文件是否存在

            Repository repository = getGit(appInfoVO).getRepository();
            RevWalk walk = new RevWalk(repository);
            ObjectId objId = repository.resolve(commitName);
            Assert4CC.notNull(objId,"文件不存在:" + fileName + "," + commitName);
            RevCommit revCommit = walk.parseCommit(objId);
            RevTree revTree = revCommit.getTree();

            //child表示相对git库的文件路径
            TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, revTree);
            Assert4CC.notNull(treeWalk,"文件不存在:" + fileName + "," + commitName);
            ObjectId blobId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(blobId);
            return new String(loader.getBytes(),Charset.forName(EncodingDetect.getJavaEncode(getGitFullPath(appInfoVO,fileName))));
        }catch (IOException  e){
            logger.info(ErrorInfo.errorInfo(e));

        }throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
    }

    @Override
    public Git initGitDir(File file){
        try {
            return Git.init().setDirectory(file).call();
        }catch (GitAPIException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
        }
    }

    @Override
    public File getFileByFileName(AppInfoVO appInfoVO, String fileName) {
        return new File(getGitFullPath(appInfoVO,fileName));
    }

    @Override
    public Git getGit(AppInfoVO appInfoVO){
        try {
            return Git.open(new File(CcFileUtils.concatFilePath(configProjectGitPath,appInfoVO.getAppName(),appInfoVO.getEnvName())));
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
        }
    }

    @Override
    public File getFileByFileNameCheck(AppInfoVO appInfoVO,String fileName){
        File file = new File(getGitFullPath(appInfoVO,fileName));
        Assert4CC.isTrue(file.exists(),"文件在git仓库中不存在："+file.getAbsolutePath());
        return file;
    }

    @Override
    public List<ConfigHisVo> getFileHis(AppInfoVO appInfoVO, String fileName){
        try {
            ArrayList<ConfigHisVo> arrayList = new ArrayList<>();
            Git git = getGit(appInfoVO);
            Iterable<RevCommit> iterable = git.log().setMaxCount(ProjectConstants.CONFIG_HIS_DATA_SIZE).addPath(fileName).call();

            for(RevCommit revCommit : iterable){
                ConfigHisVo configHisVo = new ConfigHisVo();
                configHisVo.setCommitId(revCommit.getName());
                configHisVo.setCommitTime(revCommit.getCommitTime()*1000L);
                arrayList.add(configHisVo);
            }
            return arrayList;
        }catch (GitAPIException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.GIT_ERROR);
        }
    }

    @Override
    public void createAppGitDir(AppInfoVO appInfoVO) {
        File file = new File(CcFileUtils.concatFilePath(configProjectGitPath,appInfoVO.getAppName()));
        Assert4CC.isTrue(file.mkdirs(),"创建应用目录失败:"+file.getAbsolutePath());
    }

    @Override
    public void createEnvGitDirAndInit(AppInfoVO appInfoVO) {
        File file = new File(getGitFullPath(appInfoVO,""));
        Assert4CC.isTrue(file.mkdirs(),"创建环境目录失败:"+file.getAbsolutePath());
        initGitDir(file);
    }

    @Override
    public long getFileCommitTimeByCommitName(AppInfoVO appInfoVO, String commitName, String fileName) {
        getFileByFileNameCheck(appInfoVO,fileName);//校验文件是否存在

        Repository repository = getGit(appInfoVO).getRepository();
        RevWalk walk = new RevWalk(repository);
        ObjectId objId = null;
        try {
            objId = repository.resolve(commitName);
            Assert4CC.notNull(objId,"文件不存在:" + fileName + "," + commitName);
            RevCommit revCommit = walk.parseCommit(objId);
            long commitTime = revCommit.getCommitTime()*1000L;
            return commitTime;
        } catch (IOException e) {
            e.printStackTrace();
        }
     return 0;
    }
}
