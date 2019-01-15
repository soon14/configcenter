package com.asiainfo.configcenter.client.execute;

import com.asiainfo.configcenter.client.common.ConfigLoader;
import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.exception.FileException;
import com.asiainfo.configcenter.client.util.HTTPClientUtils;
import com.asiainfo.configcenter.client.util.ZipUtil;
import com.google.gson.JsonElement;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DownloadEnvFiles {

    private static final Logger logger = LoggerFactory.getLogger(DownloadEnvFiles.class);

    /**
     * 下载配置文件到本地
     * 将App 环境下的所有配置文件下载到本地
     * Author: Erick
     * Date: 2018/9/7
     *
     * @param appName   应用名称
     * @param envName   环境名称
     *
     * */
    public static void getDownloadFiles(String appName, String envName) throws IOException {
        if (StringUtils.isEmpty(appName) || StringUtils.isEmpty(envName)) {
            throw new Error("应用名称或环境名称不能为空!");
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appName", appName);
        paramMap.put("envName", envName);
        String url = ConfigLoader.getConfigWithCheck(ProjectConstants.CONFIG_KEY_SERVER_HOST)
                + ProjectConstants.DOWNLOAD_APP_ENV_FILE_URL;
        JsonElement zipFiles = HTTPClientUtils.get(url,paramMap);
        File tempDir = new File(System.getProperty("user.home")
                + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR
                + File.separator + appName);
        if (!tempDir.exists()) {
            if(!tempDir.mkdirs()) {
                throw new RuntimeException("创建临时文件出错");
            }
        }
        File tempFile = new File(tempDir, envName+".zip");
        if (!"null".equals(zipFiles.toString())) {
            System.out.println("下载压缩文件");
            if (tempDir.exists()) {
                String content = zipFiles.getAsJsonObject().get(ProjectConstants.CONFIG_FILE_ZIP).toString();
                FileUtils.writeByteArrayToFile(tempFile, Base64.decodeBase64(content.getBytes("UTF-8")));
                if (tempFile.length() == 0) {
                    System.out.println("文件写入失败");
                    throw new FileException("文件写入失败！" + tempFile.getAbsolutePath());
                }
                System.out.println("开始解压");
                unZipFiles(tempFile.getAbsolutePath(), tempDir.getAbsolutePath());
                if(!tempFile.delete()) {
                    throw new RuntimeException("删除压缩包失败");
                }
            } else {
                throw new RuntimeException("获取压缩文件失败");
            }
        }

    }

    /**
     * 全量刷新配置文件
     * 使用ZipUtil将配置文件压缩到指定路径
     * Author Erick
     * date 2018/09/10
     *
     * @param srcUrl   压缩包路径
     * @param desUrl   目标路径
     *
     * */
    private static ZipFile flushfAllFiles(File srcUrl, String desUrl){
        return ZipUtil.zip(srcUrl, desUrl);
    }

    /**
     * 解压文件夹
     * Author Erick
     * date 2018/09/11
     *
     * @param srcUrl 压缩文件路径
     *
     * */
    private static void unZipFiles(String srcUrl, String destUrl){
        try {
            ZipFile zipFile = new ZipFile(srcUrl);
            if (!zipFile.isValidZipFile()) {
                throw new ZipException("不支持的压缩文件格式！");
            }
            zipFile.extractAll(destUrl);
        } catch (ZipException e) {
            logger.error("解压失败",e);
            e.printStackTrace();
        }
    }

}
