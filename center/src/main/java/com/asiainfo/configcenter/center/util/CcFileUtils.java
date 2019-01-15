package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.center.common.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by oulc on 2018/8/3.
 * 文件工具类
 */
public class CcFileUtils {
    private static Logger logger = Logger.getLogger(CcFileUtils.class);

    /**
     * 创建临时文件
     * @author oulc
     * @date 2018/8/3 11:25
     * @return 临时文件
     */
    public static File createTempFile(String fileType){
        try {
            return File.createTempFile(ProjectConstants.TEMP_FILE_PREFIX,""+System.currentTimeMillis()+"."+fileType);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"创建临时文件失败");
        }
    }

    /**
     * 创建一个临时目录
     * @author oulc
     * @date 2018/8/6 10:20
     * @return 目录
     */
    public static File createTempDir(){
        File file = new File(System.getProperty("java.io.tmpdir")+"/"+ProjectConstants.TEMP_FILE_PREFIX+System.currentTimeMillis());
        Assert4CC.isTrue(file.mkdir(),"创建临时文件夹失败");
        return file;
    }

    /**
     * 写文件
     * @author oulc
     * @date 2018/8/3 11:35
     * @param file 文件
     * @param data 数据
     */
    public static void writeByteArrayToFile(File file,byte []data){
        try {
            FileUtils.writeByteArrayToFile(file,data);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"写文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 解压zip压缩包
     * @author oulc
     * @date 2018/8/3 14:06
     * @param file 压缩文件
     * @return 目标文件夹
     */
    public static String unzip(File file){
        try {
            ZipFile zipFile = new ZipFile(file);
            Assert4CC.isTrue(zipFile.isValidZipFile(),ResultCodeEnum.SYSTEM_COMMON_ERROR,"解压文件失败,文件不是合法的zip压缩文件:"+file.getAbsolutePath() );//判断文件格式
            String targetFolder = file.getParentFile().getAbsolutePath()+File.separator+ProjectConstants.TEMP_FILE_PREFIX + System.currentTimeMillis();//解压后的目标目录
            zipFile.extractAll(targetFolder);//解压
            Assert4CC.isTrue(file.delete(),ResultCodeEnum.SYSTEM_COMMON_ERROR,"解压压缩包后，删除压缩包失败:"+file.getAbsolutePath());
            return convertFilePath(targetFolder);
        }catch (ZipException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"解压文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 压缩文件夹
     * @author oulc
     * @date 18-8-21 下午3:55
     * @param srcFile 被压缩文件夹
     * @return 压缩包
     */
    public static ZipFile zip(File srcFile){
        try {

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            ZipFile zipFile = new ZipFile(System.getProperty("java.io.tmpdir")+"/"+ProjectConstants.TEMP_FILE_PREFIX+System.currentTimeMillis()+".zip");
            File [] subFiles = srcFile.listFiles();
            for(File file:subFiles){
                if(!file.getName().equals(".git")){
                    if(file.isDirectory()){
                        zipFile.addFolder(file, parameters);
                    }else{
                        zipFile.addFile(file, parameters);
                    }

                }
            }

            return zipFile;
        }catch (ZipException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"解压文件失败:"+srcFile.getAbsolutePath());
        }
    }

    /**
     * 复制文件至目录中
     * @author oulc
     * @date 2018/8/3 15:12
     * @param originFile 源文件
     * @param targetDir 目标目录
     */
    public static void copyFileToDirectory(File originFile,File targetDir){
        try {
            FileUtils.copyFileToDirectory(originFile ,targetDir);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"复制文件到指定文件夹失败,源文件:"+originFile.getAbsolutePath()+",目标目录:"+targetDir.getAbsolutePath());
        }
    }

    /**
     * 把 \转换成 /
     * @author oulc
     * @date 2018/8/6 10:20
     * @param path 路径
     * @return 抓换成功字符串
     */
    public static String convertFilePath(String path){
        return path.replace("\\","/");
    }

    /**
     * 删除一个目录
     * @author oulc
     * @date 2018/8/6 10:19
     * @param file 目录
     */
    public static void deleteDirectory(File file){
        try {
            FileUtils.deleteDirectory(file);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"删除文件夹失败:"+file.getAbsolutePath());
        }
    }

    public static String getRelativeFilePath(String filePath,String rootPath){
        filePath = convertFilePath(filePath);
        rootPath = convertFilePath(rootPath);
        filePath = filePath.endsWith("/")?filePath.substring(0,filePath.length() - 1 ):filePath;
        rootPath = rootPath.endsWith("/")?rootPath.substring(0,rootPath.length() - 1 ):rootPath;
        if(filePath.equals(rootPath)){
            return "";
        }else{
            return filePath.substring( rootPath.length() + 1);
        }
    }

    /**
     * 连接文件路径
     * @author oulc
     * @date 2018/8/6 10:53
     * @param filePaths 文件路劲
     * @return 连接后的路径
     */
    public static String concatFilePath(String ... filePaths){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ;i < filePaths.length ;i ++){
            String filePath = filePaths[i];
            filePath = convertFilePath(filePath);
            if(filePath.length() != 1){
                if( i != filePaths.length - 1 ){
                    filePath = filePath.endsWith("/") ? filePath : filePath + "/";
                }else{
                    filePath = filePath.endsWith("/") ? filePath.substring(0,filePath.length() - 1 ) : filePath;
                }
                if( i != 0){
                    filePath = filePath.startsWith("/") ? filePath.substring(1,filePath.length()):filePath;
                }
            }
            sb.append(filePath);
        }
        return sb.toString();
    }

    /**
     * 获取配置文件的简称 /com/asiainfo/test.txt -> test.txt
     * @author oulc
     * @date 2018/8/7 14:54
     * @param fileName 配置文件名称
     * @return 配置文件简称
     */
    public static String getFileSampleName(String fileName){
        fileName = convertFilePath(fileName);
        int index = fileName.lastIndexOf("/");
        if(index != -1){
            return fileName.substring(index+1);
        }else{
            return fileName;
        }
    }

    /**
     * 读取文件至字符串
     * @author oulc
     * @date 2018/8/7 14:56
     * @param file 配置文件
     * @return 配置文件内容
     */
    public static String readFileToString(File file){
        try {
            return FileUtils.readFileToString(file);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"读取配置文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 读取文件至byte数组
     * @author oulc
     * @date 2018/8/7 14:57
     * @param file 配置文件
     * @return 内容
     */
    public static byte[] readFileToByteArray(File file){
        try {
            return FileUtils.readFileToByteArray(file);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"读取配置文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 把字符串写进文件
     * @author oulc
     * @date 2018/8/7 17:58
     * @param file 文件
     * @param data 内容
     */
    public static void writeStringToFile(File file,String data){
        try {
            String encode;
            if(file.exists()){
                encode = EncodingDetect.getJavaEncode(file.getAbsolutePath());
            }else{
                encode = ProjectConstants.DEFAULT_ENCODE_TYPE;
            }
            FileUtils.writeStringToFile(file,data,encode,false);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"写文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 把字符串写进文件
     * @author oulc
     * @date 2018/8/7 17:40
     * @param file 文件
     * @param data 内容
     * @param encode 编码格式
     */
    public static void  writeStringToFile(File file,String data,String encode){
        try {
            FileUtils.writeStringToFile(file,data,encode,false);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"写文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 复制一个目录至另一个目录
     * @author oulc
     * @date 2018/8/8 14:49
     * @param originFile 源目录
     * @param targetDir 目标目录
     */
    public static void copyDirectoryToDirectory(File originFile,File targetDir){
        try {
            FileUtils.copyDirectoryToDirectory(originFile,targetDir);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"复制文件失败："+originFile.getAbsolutePath());
        }
    }

    /**
     * 获取文件夹内的所有文件(非文件夹)
     * @author oulc
     * @date 2018/8/8 14:48
     * @param file 文件/文件夹
     * @param arrayList 保存文件全路径的数组
     */
    public static void getChildLeafFiles(File file, ArrayList<String> arrayList){
        if(file.isDirectory()){
            File [] childs = file.listFiles();
            if(childs != null && childs.length > 0){
                for(File child : childs){
                    getChildLeafFiles(child,arrayList);
                }
            }
        }else{
            arrayList.add(file.getAbsolutePath());
        }
    }

    /**
     * 把文件写进临时文件
     * @author oulc
     * @date 2018/8/8 15:53
     * @param data 数据
     * @param filePath 文件相对路径
     * @return 临时创建的目录
     */
    public static File writeByteArrayToTempDir(byte [] data,String filePath){
        File tempDir = createTempDir();
        File tempFile = new File(tempDir,filePath);
        fileMkdirs(tempFile.getParentFile());
        writeByteArrayToFile(tempFile,data);
        return tempDir;
    }

    /**
     * 把字符串写进临时文件
     * @author oulc
     * @date 2018/8/8 16:07
     * @param data 数据
     * @param filePath 文件路径
     * @param encode 编码格式
     * @return 临时创建的目录
     */
    public static File writeStringToTempDir(String data,String filePath,String encode){
        File tempDir = createTempDir();
        File tempFile = new File(tempDir,filePath);
        fileMkdirs(tempFile.getParentFile());
        writeStringToFile(tempFile,data,encode);
        return tempDir;
    }

    /**
     * 如果文件夹不存在就创建
     * @author oulc
     * @date 2018/8/8 15:52
     * @param file 目录文件
     */
    public static void fileMkdirs(File file){
        if( ! file.exists()){
            Assert4CC.isTrue(file.mkdirs(),ResultCodeEnum.SYSTEM_COMMON_ERROR,"创建目录失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 移动目录
     * @author oulc
     * @date 18-8-14 下午6:04
     * @param srcFile 源目录
     * @param destDir 目标目录
     */
    public static void moveDirectory(File srcFile,File destDir){
        try {
            FileUtils.moveDirectory(srcFile, destDir);
        }catch (IOException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.SYSTEM_COMMON_ERROR,"移动目录失败：srcFile:"+srcFile.getAbsolutePath() + ",destFile:"+destDir.getAbsolutePath());
        }

    }
}
