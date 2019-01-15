package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ErrorCodeException;
import com.asiainfo.configcenter.center.common.ErrorInfo;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.service.interfaces.INetDiskSV;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.util.EncodingDetect;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.task.TaskInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oulc on 2018/8/2.
 * 网盘业务代码
 */
@Service
public class NetDiskSVImpl implements INetDiskSV {

    @Value("${config.project.netdisk.dir}")
    private String configProjectNetDiskPath;

    private Logger logger = Logger.getLogger(NetDiskSVImpl.class);


    @Override
    public void saveConfigFileToNetDisk(File configFile,String configFileRootPath,AppInfoVO appInfoVO,TaskInfo taskInfo ){
        String configFileFolder = CcFileUtils.getRelativeFilePath( configFile.getParent(),configFileRootPath);//配置文件的目录的相对项目的路径
        String configFileFolderInNetDisk = getNetDiskFullFilePath(appInfoVO,CcFileUtils.concatFilePath(""+taskInfo.getTaskId(),""+taskInfo.getDetailId(),configFileFolder));//在网盘预先创建目录

        File saveDir = new File(configFileFolderInNetDisk);//配置文件保存的目录
        if(! saveDir.exists()){
            Assert4CC.isTrue(saveDir.mkdirs(), ResultCodeEnum.CONFIG_COMMON_ERROR,"创建配置文件("+configFile.getName()+")的目录：" + configFileFolder + "失败");//创建保存的目录
        }

        //校验文件是否已经存在
        Assert4CC.isTrue(! new File(saveDir,configFile.getName()).exists(),"上传配置文件："+configFile.getName()+"失败，配置文件在网盘中已经存在，请联系管理员");
        //保存配置文件 copy文件
        CcFileUtils.copyFileToDirectory(configFile,saveDir);
    }

    @Override
    public String getNetDiskFullFilePath(String projectName,String envName,String path){
        return CcFileUtils.concatFilePath(configProjectNetDiskPath,projectName,envName,path);
    }

    @Override
    public String getNetDiskFullFilePath(String projectName, String envName, String taskId, String taskDetailId, String fileName) {
        return CcFileUtils.concatFilePath(configProjectNetDiskPath, projectName, envName, taskId, taskDetailId, fileName);
    }

    @Override
    public String getNetDiskFullFilePath(AppInfoVO appInfoVO, String path){
        return getNetDiskFullFilePath(appInfoVO.getAppName(),appInfoVO.getEnvName(),path);
    }

    @Override
    public void deleteConfigFileFromNetDisk(int taskId, int detailId) {
        CcFileUtils.deleteDirectory(new File(CcFileUtils.concatFilePath(configProjectNetDiskPath,""+taskId,""+detailId)));
    }

    @Override
    public ArrayList<String> getRelativeConfigFiles(ArrayList<String> files, AppInfoVO appInfoVO, int taskId, int detailId) {
        ArrayList<String> result = new ArrayList<>();
        for(String fileName:files){
            result.add(CcFileUtils.getRelativeFilePath(fileName,getNetDiskFullFilePath(appInfoVO,CcFileUtils.concatFilePath(""+taskId,""+detailId))));
        }
        return result;
    }

    @Override
    public File getTempFileFromNetDisk(String appName, String envName, int taskId, int taskDetailId, String fileName) {
        String fileFullPath = getNetDiskFullFilePath(appName, envName, taskId + "", taskDetailId + "", fileName);
        File file = new File(fileFullPath);
        Assert4CC.isTrue(file.exists(), "获取临时文件（文件路径：" + fileFullPath + "）失败");
        return file;
    }

    @Override
    public String getTempFileContent(String appName, String envName, int taskId, int taskDetailId, String fileName) {
        File file = getTempFileFromNetDisk(appName, envName, taskId, taskDetailId, fileName);
        try {
            String encode = EncodingDetect.getJavaEncode(file);
            FileInputStream in=new FileInputStream(file);
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            return new String(buffer, encode);
        } catch (IOException e) {
            logger.error(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.CONFIG_COMMON_ERROR, "读取临时配置文件失败");
        }
    }
}
