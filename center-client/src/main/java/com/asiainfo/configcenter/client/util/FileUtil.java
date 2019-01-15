package com.asiainfo.configcenter.client.util;

import com.asiainfo.configcenter.client.common.ConfigLoader;
import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.exception.FileException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    private static Logger logger  = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建临时文件
     * @author zhangxiangxin
     * @date 2018/9/5
     * @return 临时文件
     */
    public static File createFileDir() {
        return new File(ProjectConstants.FILE_DIR_PREFIX);
    }

    /**
     * 将字符串写入到文件中
     * @author zhangxiangxin
     * @date 2018/9/5
     * @param file 文件
     * @param data 数据
     */
    public static void  writeStringToFile(File file,String data){
        try {
            String encoding = ConfigLoader.getConfigDefault(ProjectConstants.CONFIG_KEY_FILE_ENCODING, ProjectConstants.DEFAULT_ENCODING_TYPE);
            FileUtils.writeStringToFile(file, data, encoding,false);
        }catch (IOException e){
            logger.info(e.getMessage());
            throw new FileException("写文件失败:"+file.getAbsolutePath());
        }
    }

    /**
     * 根据文件名创建文件，写入内容
     * @author zhangxiangxin
     * @date 2018/9/5
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void writeContentToFile(String fileName, String content) {
        File fileDir = createFileDir();
        if(!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir.getAbsolutePath(),fileName);
        writeStringToFile(file,content);
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
     * 得到相对于根路径的相对路径
     * @author zhangxiangxin
     * @date 2018/9/12 16:35
     * @param filePath 文件的全路径
     * @param rootPath  文件的根路径
     */
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
     * 去除文件路径首字符为/
     * @param filePath 待处理的字符串
     * @return 处理完成的字符串
     * */
    public static String removeHeadChar(String filePath) {
        return filePath.startsWith("/")?filePath.substring(1):filePath;
    }
}
