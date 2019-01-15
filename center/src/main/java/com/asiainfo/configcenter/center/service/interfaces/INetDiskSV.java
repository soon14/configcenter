package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.task.TaskInfo;

import java.io.File;
import java.util.ArrayList;

public interface INetDiskSV {
    /**
     * 根据相对路径获取到网盘的绝对路径
     * @author oulc
     * @date 2018/8/8 15:05
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param path 相对路径
     * @return 网盘绝对路径
     */
    String getNetDiskFullFilePath(String projectName,String envName,String path);

    /**
     * 获取配置文件绝对路径
     * @param projectName 应用名称
     * @param envName 环境名称
     * @param taskId 任务标识
     * @param taskDetailId 任务详情
     * @param fileName 文件名
     * @return 文件绝对路径
     */
    String getNetDiskFullFilePath(String projectName, String envName, String taskId, String taskDetailId, String fileName);

    /**
     * 根据相对路径获取到网盘的绝对路径
     * @author oulc
     * @date 2018/8/8 15:03
     * @param appInfoVO 应用信息
     * @param path 相对路径
     * @return 网盘绝对路径
     */
    String getNetDiskFullFilePath(AppInfoVO appInfoVO, String path);

    /**
     * 保存配置文件至网盘中
     * @author oulc
     * @date 2018/8/6 11:07
     * @param configFile 配置文件
     * @param configFileRootPath 配置文件相对环境根目录的相对路径
     * @param appInfoVO 应用信息
     * @param taskInfo 任务信息
     */
    void saveConfigFileToNetDisk(File configFile, String configFileRootPath, AppInfoVO appInfoVO, TaskInfo taskInfo );

    /**
     * 从网盘里删除配置文件
     * @author oulc
     * @date 2018/8/8 15:03
     * @param taskId 任务主键
     * @param detailId 任务详情主键
     */
    void deleteConfigFileFromNetDisk(int taskId,int detailId);

    /**
     * 获取网盘文件的相对应用的相对路径
     * @author oulc
     * @date 2018/8/8 15:03
     * @param files 文件
     * @param appInfoVO 应用信息
     * @param taskId 任务主键
     * @param detailId 任务详情主键
     * @return 相对路径列表
     */
    ArrayList<String> getRelativeConfigFiles(ArrayList<String> files,AppInfoVO appInfoVO,int taskId,int detailId);

    /**
     * 获取网盘中的临时文件
     * @author bawy
     * @date 2018/8/9 16:58
     * @param appName 应用名称
     * @param envName 环境名称
     * @param taskId 任务标识
     * @param taskDetailId 任务详情标识
     * @param fileName 文件名称
     * @return 文件对象
     */
    File getTempFileFromNetDisk(String appName, String envName, int taskId, int taskDetailId, String fileName);

    /**
     * 获取网盘中临时文件的内容
     * @author bawy
     * @date 2018/8/9 16:58
     * @param appName 应用名称
     * @param envName 环境名称
     * @param taskId 任务标识
     * @param taskDetailId 任务详情标识
     * @param fileName 文件名称
     * @return 文件内容
     */
    String getTempFileContent(String appName, String envName, int taskId, int taskDetailId, String fileName);

}
