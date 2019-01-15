package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configfile.ConfigHisVo;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public interface IGitSV {

    /**
     * 添加配置文件 并提交
     * @param filePath 文件路径
     * @param appInfoVO 应用环境信息
     */
    String copyFileToGitAndCommit(String filePath, AppInfoVO appInfoVO, ArrayList<String> relativePath);

    /**
     * 复制文件(文件夹)至git工作空间
     * @param filePath 配置文件（文件夹）路径
     * @param appInfoVO 应用信息
     */
     void copyFileToGit(String filePath,AppInfoVO appInfoVO);

    /**
     * 根据文件相对路径获取文件在git的绝对路径
     * @param appInfoVO 应用和环境名称
     * @param filePath 文件路径
     * @return 文件绝对路径
     */
    String getGitFullPath(AppInfoVO appInfoVO,String filePath);

    /**
     * git add 文件
     * @param appInfoVO 应用信息
     * @param fileName 文件名称
     */
    void gitAddFile(String fileName,AppInfoVO appInfoVO,ArrayList<String> relativePath);

    /**
     * 提交配置文件变更
     * @param appInfoVO 项目信息
     * @return commit name
     */
    String gitCommitFile(AppInfoVO appInfoVO);

    /**
     * 删除一个配置文件并提交
     * @param appInfoVO 应用信息
     * @param filePath 配置文件目录（相对路径）
     */
    void deleteConfigFileAndCommit(AppInfoVO appInfoVO,String filePath);

    /**
     * 删除配置文件
     * @param appInfoVO 应用信息
     * @param filePath 配置文件目录（相对路径）
     */
    void deleteConfigFile(AppInfoVO appInfoVO,String filePath);

    /**
     * 删除配置文件 如果父目录为空 也直接删除
     * @param file 文件
     */
    void deleteConfigFileRepeat(File file);

    /**
     * 添加配置文件 并提交 删除网盘中的文件
     * @author oulc
     * @date 2018/8/3 9:52
     * @param taskId 任务主键
     * @param detailId 详情主键
     * @param appInfoVO 应用名称和环境名称
     */
    String copyFileToGitAndCommit(int taskId,int detailId,AppInfoVO appInfoVO);

    /**
     * 初始化一个git仓库
     * @author oulc
     * @date 2018/8/7 11:21
     * @param file 仓库目录
     * @return git
     */
    Git initGitDir(File file);

    /**
     * 根据commitName获取配置文件内容
     * @author oulc
     * @date 2018/8/7 17:41
     * @param appInfoVO 应用信息
     * @param commitName commitName
     * @param fileName 配置文件名称
     * @return 配置文件内容
     */
    String getFileContentByCommitName(AppInfoVO appInfoVO , String commitName,String fileName);

    /**
     * 根据文件名称获取文件
     * @author oulc
     * @date 2018/8/8 14:59
     * @param appInfoVO 应用信息
     * @param fileName 文件名称
     * @return 文件对象
     */
    File getFileByFileName(AppInfoVO appInfoVO,String fileName);

    /**
     * 根据应用信息获取git仓库对象
     * @author oulc
     * @date 2018/8/8 14:57
     * @param appInfoVO 应用信息
     * @return git仓库对象
     */
    Git getGit(AppInfoVO appInfoVO);

    /**
     * 根据配置文件名称和应用信息获取文件
     * @author oulc
     * @date 2018/8/8 14:58
     * @param appInfoVO 应用信息
     * @param fileName 文件名称
     * @return 文件对象
     */
    File getFileByFileNameCheck(AppInfoVO appInfoVO,String fileName);

    /**
     * 获取文件历史
     * @author oulc
     * @date 2018/8/8 10:29
     * @param appInfoVO 应用信息
     * @param fileName 文件名称
     * @return 历史列表
     */
    List<ConfigHisVo> getFileHis(AppInfoVO appInfoVO, String fileName);

    /**
     * 创建应用git目录
     * @author oulc
     * @date 2018/8/8 14:59
     * @param appInfoVO 应用信息
     */
    void createAppGitDir(AppInfoVO appInfoVO);

    /**
     * 创建环境git目录并初始化git仓库
     * @author oulc
     * @date 2018/8/8 15:00
     * @param appInfoVO 应用信息
     */
    void createEnvGitDirAndInit(AppInfoVO appInfoVO);

    long getFileCommitTimeByCommitName(AppInfoVO appInfoVO , String commitName,String fileName);


}
