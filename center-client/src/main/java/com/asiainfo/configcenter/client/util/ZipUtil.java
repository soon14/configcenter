package com.asiainfo.configcenter.client.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class ZipUtil {
    /**
     * 压缩文件夹 可以直接使用这个方法把配置文件压缩进jar包中
     * @author oulc
     * @date 18-8-21 下午3:55
     * @param srcFile 被压缩文件夹
     * @return 压缩包
     */
    public static ZipFile zip(File srcFile,String targetZipFile){
        try {

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            ZipFile zipFile = new ZipFile(targetZipFile);
            File [] subFiles = srcFile.listFiles();
            for(File file:subFiles){
                if(file.isDirectory()){
                    zipFile.addFolder(file, parameters);
                }else{
                    zipFile.addFile(file, parameters);
                }
            }
            return zipFile;
        }catch (ZipException e){
            e.printStackTrace();
        }
        return null;
    }
}
