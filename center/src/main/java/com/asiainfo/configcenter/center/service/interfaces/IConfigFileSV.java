package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcConfigFileEntity;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigFileEntity;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.client.ClientReqVO;
import com.asiainfo.configcenter.center.vo.configfile.*;

import java.io.File;
import java.util.List;
import java.util.Map;


public interface IConfigFileSV {
    /**
     * 根据环境查询关联的配置文件(表)
     * @author oulc
     * @date 2018/8/6 11:31
     * @param envId 环境主键
     * @return 配置文件列表
     */
    List<CcConfigFileEntity> getConfigFilesByEnvId(int envId);

    /**
     * 上传新增一个配置文件(提供给controller)
     * @author oulc
     * @date 2018/8/3 9:50
     * @param upOneConfigFileVO 配置文件
     * @param userId 用户主键
     */
    void upOneConfigFileForCreate(UpOneConfigFileVO upOneConfigFileVO, int userId);

    /**
     * 上传配置文件(zip)(提供给controller)
     * @author oulc
     * @date 2018/8/3 11:06
     * @param upManyConfigFileVO 配置文件信息
     * @param userId 用户主键
     */
    void upManyConfigFileForCreate(UpManyConfigFileVO upManyConfigFileVO,int userId);

    /**
     * 上传配置文件并更新
     * @author oulc
     * @date 2018/8/6 15:02
     * @param upOneConfigFileForUpdateVO 配置文件信息
     * @param userId 用户主键
     */
    void upConfigFileForUpdate(UpOneConfigFileForUpdateVO upOneConfigFileForUpdateVO,int userId);

    /**
     * 根据环境主键和配置文件名称查询配置文件
     * @param envId 环境主键
     * @param configFileName 配置文件名称
     * @return 配置文件实体
     * @author oulc
     * @date 2018/7/31 16:11
     */
    CcConfigFileEntity getConfigFileByEnvIdAndFileName(int envId,String configFileName);

    /**
     * 查询配置文件
     * @param pageRequestContainer 查询条件
     * @return 配置文件列表
     * @author oulc
     * @date 2018/8/1 15:24
     */
    PageResultContainer<CXCcConfigFileEntity> getConfigFiles(PageRequestContainer<QueryConfigFileVO> pageRequestContainer);

    /**
     * 任务审核通过回调(新增、修改、删除配置文件)
     * @author oulc
     * @date 2018/8/3 9:51
     * @param taskId 任务主键
     * @param envId 环境主键
     * @param taskDetailConfigEntity 任务实体
     */
    void saveOrUpdateFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity);

    /**
     * 创建一个配置文件
     * @author oulc
     * @date 2018/8/3 9:52
     * @param taskId 任务主键
     * @param envId 环境主键
     * @param taskDetailConfigEntity 任务详情
     * @return 配置文件主键
     */
    int createConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity);

    /**
     * 更新配置文件(包括数据库和zk节点)
     * @author oulc
     * @date 2018/8/7 14:14
     * @param taskId 任务主键
     * @param envId 环境主键
     * @param taskDetailConfigEntity 任务详情实体
     */
    void updateConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity);

    /**
     * 删除配置文件
     * @author oulc
     * @date 2018/8/7 14:14
     * @param taskId 任务主键
     * @param envId 环境主键
     * @param taskDetailConfigEntity 任务详细实体
     */
    void deleteConfigFile(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity);

    /**
     * 创建配置文件数据库信息
     * @author oulc
     * @date 2018/8/6 14:10
     * @param envId 环境主键
     * @param fileName 配置文件名称
     * @param strategyId 审核策略
     * @param fileDesc 配置文件描述
     * @param fileVersion 配置文件版本
     * @param creator 创建人
     * @return 配置文件主键
     */
    int createConfigFileDataToBaseInfo(int envId,String fileName,Integer strategyId, String fileDesc,String fileVersion,int creator);



    /**
     * 删除一个配置文件
     * @param configFileVO 配置文件主键
     * @param userId 用户主键
     * @author oulc
     * @date 2018/8/2 16:11
     */
    void deleteConfigFile(DelConfigFileVO configFileVO,int userId);

    /**
     * 根据主键查询配置文件
     * @author oulc
     * @date 2018/8/3 10:25
     * @param id 配置文件主键
     * @return 配置文件实体
     */
    CcConfigFileEntity getConfigFileById(int id);

    /**
     * 根据主键查询配置文件,并校验配置文件存在
     * @author oulc
     * @date 2018/8/3 10:24
     * @param id 配置文件主键
     * @return 配置文件实体
     */
    CcConfigFileEntity getConfigFileByIdCheck(int id);

    /**
     * 删除数据库中的配置文件信息
     * @author oulc
     * @date 2018/8/3 9:44
     * @param ccConfigFileEntity 配置文件实体
     * @param userId 用户主键 
     */
    void deleteConfigFileDataBaseInfo(CcConfigFileEntity ccConfigFileEntity,int userId);

    /**
     * 创建配置文件zk节点
     * @author oulc
     * @date 2018/8/3 9:43
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param configFileName 配置文件名称
     * @param value 节点数据
     */
    void createConfigFileZKNode(String projectName,String envName,String configFileName,String value);

    /**
     * 删除配置文件zk节点
     * @author oulc
     * @date 2018/8/3 9:43
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param configFileName 配置文件名称
     *                       
     */
    void deleteConfigFileZKNode(String projectName,String envName,String configFileName);

    /**
     * 上传配置文件
     * @author oulc
     * @date 2018/8/3 15:23
     * @param configFile 配置文件(可以是一个配置文件，也可以是存放配置文件的目录，)
     * @param strategyId 刷新策略编码
     * @param configFileRootPath 配置文件的根目录
     * @param fileDesc 配置文件描述
     * @param userId 用户主键
     * @param appInfoVO 应用信息
     */
    void uploadConfigFileForCreate(File configFile, Integer strategyId, String configFileRootPath, int userId, AppInfoVO appInfoVO, String fileDesc);

    /**
     * 更新配置文件
     * @author oulc
     * @date 2018/8/7 14:13
     * @param configFile 配置文件
     * @param strategyId 刷新策略编码
     * @param configFileRootPath 配置文件相对项目的相对路径
     * @param configId 配置文件主键
     * @param userId 用户主键
     * @param appInfoVO 应用信息
     * @param fileDesc 配置文件描述
     */
    void uploadConfigFileForUpdate(File configFile, Integer strategyId, String configFileRootPath, int configId, int userId, AppInfoVO appInfoVO, String fileDesc);

    /**
     * 从网盘回退配置文件
     * @author oulc
     * @date 2018/8/7 14:13
     * @param taskId 任务主键
     * @param ccTaskDetailConfigEntity 任务详情主题
     */
    void rollback(int taskId, CcTaskDetailConfigEntity ccTaskDetailConfigEntity);

    /**
     * 更新配置文件的数据库信息
     * @author oulc
     * @date 2018/8/7 14:12
     * @param ccConfigFileEntity 配置文件实体
     * @param userId 用户主键
     * @param fileDesc 文件描述
     * @param fileVersion 文件本本
     */
    void updateConfigFileDataBaseInfo(CcConfigFileEntity ccConfigFileEntity,int userId,String fileDesc,String fileVersion);

    /**
     * 复制配置文件(提供给controller)
     * @author oulc
     * @date 2018/8/7 14:11
     * @param copyConfigFileVO 配置文件信息
     * @param userId 用户主键
     */
    void copyConfigFile(CopyConfigFileVO copyConfigFileVO,int userId);

    /**
     * 获取多个配置文件内容
     * @author oulc
     * @date 2018/8/7 14:41
     * @param queryConfigContentsVO 配置文件信息
     * @return 配置文件内容实体列表
     */
    List<ConfigFileContent> getConfigContents(QueryConfigContentsVO queryConfigContentsVO);

    /**
     * 获取配置文件内容
     * @author oulc
     * @date 2018/8/7 14:48
     * @param queryConfigContentVO 配置文件信息
     * @return 配置文件内容实体
     */
    ConfigFileContent getConfigContents(QueryConfigContentVO queryConfigContentVO,AppInfoVO appInfoVO);

    /**
     * 在线编辑配置文件
     * @author oulc
     * @date 2018/8/7 17:47
     * @param editConfigContentVO 配置文件内容
     */
    void editConfigContent(EditConfigContentVO editConfigContentVO,int userId);

    /**
     * 获取配置文件的历史纪录
     * @author oulc
     * @date 2018/8/8 17:19
     * @param configId 配置文件主键
     * @param envId 环境主键
     * @return 历史信息
     */
    List<ConfigHisVo> getConfigFileHis(int configId,int envId);

    /**
     * 根据主键和环境主键查询配置文件实体
     * @author oulc
     * @date 2018/8/8 17:20
     * @param configId 配置文件主键
     * @param envId 环境主键
     * @return 配置文件实体
     */
    CcConfigFileEntity getConfigFileByIdAndEnvId(int configId,int envId);

    /**
     * 根据主键和环境主键查询配置文件实体，并校验
     * @author oulc
     * @date 2018/8/8 17:20
     * @param configId 配置文件主键
     * @param envId 环境主键
     * @return 配置文件实体
     */
    CcConfigFileEntity getConfigFileByIdAndEnvIdCheck(int configId,int envId);

    /**
     * 获取指定配置文件最新版本数据
     * @author bawy
     * @date 2018/8/9 15:33
     * @param configId 配置标识
     * @param envId 环境标识
     * @return 该配置文件最新版本的数据
     */
    ConfigFileContent getLastVersion(int configId, int envId);

    /**
     * 获取指定配置文件临时文件数据
     * @author bawy
     * @date 2018/8/9 16:19
     * @param envId 环境标识
     * @param taskDetailId 任务详情标识
     * @return 文件内容
     */
    ConfigFileContent getTempFileContent(int envId, int taskDetailId);

    /**
     * 获取应用环境的所有配置文件
     * @author oulc
     * @date 18-8-21 下午3:38
     * @param envId 环境主键
     * @return 配置压缩包
     */
    EnvAllConfigFileRepVO getProjectAllConfigFile(int envId);

    /**
     * 获取应用环境的所有配置文件
     * @author oulc
     * @date 18-8-21 下午3:38
     * @param envId 环境主键
     * @return 配置压缩包
     */
    File downLoadEnvConfigFiles(int envId,int userId);

    /**
     * 修改配置文件的刷新策略
     * @author bawy
     * @date 2018/8/22 15:42
     * @param changeStrategyReq 修改刷新策略请求参数
     * @param modifier 修改人
     */
    void changeStrategy(ChangeStrategyReqVO changeStrategyReq, int modifier);

    /**
     * 根据策主键查询配置文件
     * @author oulc
     * @date 18-9-3 上午10:53
     * @param strategyId 策略主键
     * @return 配置文件
     */
    List<CcConfigFileEntity> getConfigFileByStrategyId(int strategyId);

    /**
     * 根据策略主键查询配置文件名称
     * @author oulc
     * @date 18-9-3 上午11:00
     * @param strategyId 策略主键
     * @return 配置文件名称
     */
    String[] getConfigFileNamesByStrategyId(int strategyId);

}
