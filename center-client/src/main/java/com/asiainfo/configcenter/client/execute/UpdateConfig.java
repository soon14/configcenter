package com.asiainfo.configcenter.client.execute;

import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.exception.FileException;
import com.asiainfo.configcenter.client.pojo.AllConfigItemPojo;
import com.asiainfo.configcenter.client.pojo.ConfigFilePojo;
import com.asiainfo.configcenter.client.pojo.ConfigItemPojo;
import com.asiainfo.configcenter.client.util.FileUtil;
import com.asiainfo.configcenter.client.util.JSONUtil;
import com.asiainfo.configcenter.client.util.StringUtil;
import com.asiainfo.configcenter.client.zookeeper.ZookeeperConnection;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigPushVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigResultVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKUpdateResultVO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新配置类
 * Created by bawy on 2018/9/10 16:43.
 */
public class UpdateConfig {

    private static Logger logger = LoggerFactory.getLogger(UpdateConfig.class);

    /**
     * 更新中心推送的配置
     * @author bawy
     * @param nodeInfo zk节点信息
     */
    public static void updateConfig(String nodeInfo) {
        ZKConfigPushVO configPushVO = JSONUtil.jsonStrToBean(nodeInfo, ZKConfigPushVO.class);
        List<ZKConfigResultVO> filesResult = updateConfigFile(configPushVO.getFiles());
        List<ZKConfigResultVO> itemsResult = updateConfigItem(configPushVO.getItems());
        ZKUpdateResultVO updateResult = new ZKUpdateResultVO();
        updateResult.setFiles(filesResult);
        updateResult.setItems(itemsResult);
        String resultNodeInfo = JSONUtil.obj2JsonStr(updateResult);
        try {
            ZookeeperConnection.writeDataToResultInfoChildNode(resultNodeInfo);
        } catch (Exception e) {
            logger.error("刷新结果写回失败",e);
        }
    }

    /**
     * 更新配置文件
     * @author bawy
     * @param files 待更新配置文件列表
     * @return 配置文件更新结果
     */
    private static List<ZKConfigResultVO> updateConfigFile(List<ZKConfigVO> files){
        List<ZKConfigResultVO> configResults = new ArrayList<ZKConfigResultVO>();
        createTempFileForBack();
        if (files!=null && files.size()>0) {
            boolean isFileExist = true;
            try {
                List<ConfigFilePojo> configFilePojos = DownLoadConfig.downLoadConfigFileAndStrategy(files);
                isFileExist = judgeFileExist(configFilePojos);
                if(!isFileExist) { throw new RuntimeException("待刷新的配置文件不存在当前项目下");}
                for (ConfigFilePojo configFilePojo : configFilePojos) {
                    backupAndFlushFile(configFilePojo);
                    ZKConfigResultVO configResult = new ZKConfigResultVO();
                    configResult.setName(configFilePojo.getFileName());
                    boolean executeResult = ExecuteStrategy.execute(configFilePojo.getStrategies(),null);
                    configResult.setResult(executeResult);
                    configResults.add(configResult);
                    if (!executeResult) { throw new RuntimeException("刷新策略执行失败"); }
                }
            } catch (Throwable e) {
                logger.error(StringUtil.printStackTraceToString(e));
                System.out.println("配置文件更新失败");
                logger.error("配置文件更新失败");
                if(isFileExist) {
                    try {
                        rollBackFiles();
                    } catch (IOException e1) {
                        System.out.println("回退文件失败");
                        logger.error("回退文件失败");
                    }
                }
            } finally {
                cleanTempDir();
                cleanTempDirBackup();
            }
        }
        return configResults;
    }

    /*private static boolean judgeFileExist(List<ConfigFilePojo> configFilePojos) {
        boolean flag = true;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = loader.getResource("").getPath();
        for(ConfigFilePojo configFilePojo:configFilePojos) {
            InputStream resource = loader.getResourceAsStream(FileUtil.removeHeadChar(configFilePojo.getFileName()));
            if(resource == null) {
                System.out.println(configFilePojo.getFileName()+":不在classPath目录下");
                logger.error("{}在项目中不存在",configFilePojo.getFileName());
                flag = false;
                break;
            }
        }
        return flag;
    }*/

    /**
     * 判断classpath下是否存在待刷新的配置文件
     * @author zhangxiangxin
     * @param configFilePojos 待刷新的文件列表
     * @return 是否存在配置文件
     */
    private static boolean judgeFileExist(List<ConfigFilePojo> configFilePojos) {
        System.out.println("judgeFileExist.....");
        boolean flag = true;
        List<File> userClassPaths = getSpecialClassPaths();
        for (ConfigFilePojo configFilePojo:configFilePojos) {
            boolean flag1 = false;
            for (File userClassPath:userClassPaths) {
                File file = new File(userClassPath, configFilePojo.getFileName());
                if (file.exists()) {
                    flag1 = true;
                }
            }
            if (!flag1) {
                logger.error(configFilePojo.getFileName() + "：配置文件不存在");
                System.out.println(configFilePojo.getFileName()+"：文件不存在");
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取配置文件所在的路径
     * 如果用户指定了-classpath 目录所保存的文件路径，就去用户指定的
     * 如果没有指定，则去项目中的classPath目录
     * */
    private static List<File> getSpecialClassPaths() {
        System.out.println("getSpecialClassPaths");
        String userClassPath = System.getProperty(ProjectConstants.USER_CLASS_PATH);
        //切割字符：在Windows下用; 在Linux下用:
        String[] userClassPaths = userClassPath.split(":");
        ArrayList<File> userClassDirs = new ArrayList<File>();
        if (userClassPaths.length>0) {
            for(String classPath:userClassPaths) {
                if (!classPath.endsWith(".jar")) {
                    File file = new File(classPath);
                    if(file.isDirectory()) {
                        userClassDirs.add(file);
                    }
                }
            }
        }
        if (userClassDirs.size() == 0) {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("");
            if (resource != null) {
                String classPath = resource.getPath();
                File file = new File(classPath);
                if (file.isDirectory()) {
                    userClassDirs.add(file);
                }
            }
        }
        if (userClassDirs.size() == 0) {
            throw new RuntimeException("获取类路径出错");
        }
        System.out.println(userClassDirs);
        return userClassDirs;
    }



    /**
     * 创建备份文件的临时目录
     * */
    private static String createTempFileForBack() {
        String tempDirPathBak = System.getProperty("user.home") + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR_BACKUP + File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        File tempDir = new File(tempDirPathBak);
        if (!tempDir.exists()){
            if (!tempDir.mkdirs()){
                throw new FileException("创建临时目录“"+tempDirPathBak+"”失败");
            }
        }
        return tempDirPathBak;
    }


    /*private static void backupAndFlushFile(ConfigFilePojo configFilePojo) throws IOException {
        String tempDirPathBak = createTempFileForBack();
//        String targetFileDir = isJarProject()?getConfigFileDir():Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String targetFileDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("当前的classPath目录为 "+targetFileDir);
        File fileDir = new File(targetFileDir);
        if(!fileDir.exists()) {
            throw new RuntimeException("获取配置文件存储路径不正确");
        }
        FileUtils.copyFile(new File(targetFileDir,configFilePojo.getFileName()),new File(tempDirPathBak,configFilePojo.getFileName()));
        FileUtils.copyFile(new File(configFilePojo.getTempFilePath()),new File(targetFileDir,configFilePojo.getFileName()));
    }*/

    /**
     * 备份文件
     * @author zhangxiangxin
     * @param configFilePojo 待刷新的文件
     */
    private static void backupAndFlushFile(ConfigFilePojo configFilePojo) throws IOException {
        System.out.println("backupAndFlushFile");
    String tempDirPathBak = createTempFileForBack();//备份文件夹
    List<File> userClassPaths = getSpecialClassPaths();
        for (int i=0;i<userClassPaths.size();i++) {
        File file = new File(userClassPaths.get(i), configFilePojo.getFileName());
        if (file.exists()) {
            FileUtils.copyFile(file,new File(tempDirPathBak,"backupDir"+ i + File.separator +configFilePojo.getFileName()));
            FileUtils.copyFile(new File(configFilePojo.getTempFilePath()),file);
            break;
        }
    }
}

    /*private static String getConfigFileDir() {
        String classPath = System.getProperty(ProjectConstants.BOOT_STRAP_CLASS_PATH);
        String[] classPaths = classPath.split(";");
        if (classPaths.length < 1) {
            throw new RuntimeException("指定配置配置文件路径出错");
        } else {
            return classPaths[classPaths.length-1];
        }
    }*/


    /*private static void rollBackFiles() {
        String tempDirPathBak = System.getProperty("user.home") + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR_BACKUP + File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        File file = new File(tempDirPathBak);
//        String configFileDir = isJarProject()?getConfigFileDir():Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configFileDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        try {
            rollBackFilesToClassPath(file,file.getAbsolutePath(),configFileDir);
            logger.info("文件回退成功");
        } catch (IOException e) {
            logger.error("文件回退失败", e);
        }
    }*/

    /**
     * 回退文件
     * @author zhangxiangxin
     */
    private static void rollBackFiles() throws IOException {
        String tempDirPathBak = System.getProperty("user.home") +
                File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR_BACKUP +
                File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        File tempDirFile = new File(tempDirPathBak);
        List<File> userClassPaths = getSpecialClassPaths();//获取到的classPath路径
        File[] files = tempDirFile.listFiles();
        if (files != null && files.length>0) {
            for (File file:files) {
                String canonicalPath = file.getCanonicalPath();
                if (canonicalPath.contains("backupDir")) {
                    int index = Integer.parseInt(canonicalPath.substring(canonicalPath.indexOf("backupDir"), canonicalPath.indexOf("backupDir") + 1));
                    File userClassPath = userClassPaths.get(index);
                    rollBackFilesToClassPath(file,file.getCanonicalPath(),userClassPath.getCanonicalPath());
                }
            }
        } else {
            throw new RuntimeException("备份文件获取失败");
        }

    }

    /**
     * 将文件写会classPath
     * @author zhangxiangxin
     * @param file 保存临时文件的目录
     * @param rootPath 临时目录的全路径
     * @param classPath 项目中存放配置文件的根路径
     */
    private static void rollBackFilesToClassPath(File file,String rootPath,String classPath) throws IOException {
        if(file.isDirectory()) {
            File [] childFiles = file.listFiles();
            if(childFiles != null && childFiles.length >0){
                for(File childFile:childFiles){
                    rollBackFilesToClassPath(childFile,rootPath,classPath);
                }
            }
        } else {
            String fileRelativePath = FileUtil.getRelativeFilePath(file.getAbsolutePath(),rootPath);
            FileUtils.copyFile(file,new File(classPath,fileRelativePath));
        }
    }

    /**
     * 清理临时目录
     * @author bawy
     */
    private static void cleanTempDir(){
        try {
            String tempDirPath = System.getProperty("user.home") + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR + File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
            FileUtils.deleteDirectory(new File(tempDirPath));
        }catch (Exception e){
            logger.error(StringUtil.printStackTraceToString(e));
            logger.error("清理临时文件目录失败");
        }
    }

    /**
     * 清理备份临时目录
     * @author zhangxiangxin
     */
    private static void cleanTempDirBackup() {
        try {
            String tempDirPathBak = System.getProperty("user.home") + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR_BACKUP + File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
            File tempDir = new File(tempDirPathBak);
            if(tempDir.exists()) {
                FileUtils.deleteDirectory(tempDir);
            }
        } catch (Exception e) {
            logger.error("清理备份临时目录失败",e);
        }
    }

    /*private static boolean isJarProject() {
        String protocol = Thread.currentThread().getContextClassLoader().getResource("").getProtocol();
        return "jar".equals(protocol);
    }*/

    /**
     * 更新配置项
     * @author bawy
     * @param items 待更新配置项列表
     * @return 配置项更新结果
     */
    private static List<ZKConfigResultVO> updateConfigItem(List<ZKConfigVO> items){
        List<ZKConfigResultVO> configResults = new ArrayList<ZKConfigResultVO>();
        if (items!=null && items.size()>0) {
            try {
                AllConfigItemPojo allConfigItemPojo = DownLoadConfig.downloadConfigItemAndStrategy(items);
                for (ConfigItemPojo configItemPojo: allConfigItemPojo.getConfigItemPojos()) {
                    ZKConfigResultVO configResult = new ZKConfigResultVO();
                    configResult.setName(configItemPojo.getItemKey());
                    configResult.setResult(ExecuteStrategy.execute(configItemPojo.getStrategies(),allConfigItemPojo.getItemMap()));
                    configResults.add(configResult);
                }
            } catch (Throwable e) {
                logger.error(StringUtil.printStackTraceToString(e));
                logger.error("配置项更新失败");
            }
        }
        return configResults;
    }

    /**
     * 应用刚启动时，下载并刷新文件
     * */
    public static void pullAndUpdateFiles() {
        String appName = System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        String envName = System.getProperty(ProjectConstants.CLIENT_ENV_NAME);
        try {
            DownloadEnvFiles.getDownloadFiles(appName,envName);
            backFilesAndReplaceFiles();
            System.out.println("启动时获取最新配置文件成功");
            logger.info("启动时获取最新配置文件成功");
        } catch (IOException e) {
            System.out.println("获取配置文件出错");
            try {
                rollBackFiles();
            } catch (IOException e1) {
                System.out.println("回退文件失败");
                logger.error("回退文件失败");
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            cleanTempDir();
            cleanTempDirBackup();
        }
    }

    /**
     * 把classpath下的文件备份，并将下载下来的文件写到classPath下
     * */
    private static void backFilesAndReplaceFiles() throws IOException {
        List<File> userClassPaths = getSpecialClassPaths();//类路径集合
        String tempSaveDir = System.getProperty("user.home") +
                File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR +
                File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);//下载下来的文件保存的文件
        File fileDir = new File(tempSaveDir);
        for (int i=0;i<userClassPaths.size();i++) {
            saveAndReplaceFiles(fileDir,tempSaveDir,userClassPaths.get(i).getCanonicalPath(),createTempFileForBack(),i);
        }
    }

    /**
     * 将文件保存并且备份
     * @param file 待刷新的文件或者文件夹
     * @param rootPath 待刷新的根目录
     * @param classPath 待刷新的类路径
     * @param backupDirectory 备份目录
     * @param index classPath所在的索引
     * */
    private static void saveAndReplaceFiles(File file,String rootPath,String classPath,String backupDirectory,int index) throws IOException {
        if (file.isDirectory()) {
            File [] childFiles = file.listFiles();
            if(childFiles != null && childFiles.length >0){
                for(File childFile:childFiles){
                    saveAndReplaceFiles(childFile,rootPath,classPath,backupDirectory,index);
                }
            }
        } else {
            String fileRelativePath = FileUtil.getRelativeFilePath(file.getAbsolutePath(),rootPath);
            File classPathFile = new File(classPath, fileRelativePath);
            if (classPathFile.exists()) {//判断当前这个文件是否在文件下
                FileUtils.copyFile(classPathFile,new File(backupDirectory,"backupDir"+ index +File.separator+fileRelativePath));//把文件copy到备份目录
                FileUtils.copyFile(file,classPathFile);//把目标文件copy到当前的classPath下
            }
        }
    }
}
