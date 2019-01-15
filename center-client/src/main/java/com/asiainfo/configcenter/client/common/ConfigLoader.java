package com.asiainfo.configcenter.client.common;

import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.util.ParseFileUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置加载类
 * Created by bawy on 2018/9/10 17:33.
 */
public class ConfigLoader {

    private static Properties properties;

    /**
     * 读取配置
     * @param key 配置关键字
     * @return 配置内容
     */
    public static String getConfig(String key){
        if (properties == null){
            try {
                properties = ParseFileUtil.parseFileToProperties(ProjectConstants.CONFIG_FILE_NAME);
            } catch (IOException e) {
                throw new RuntimeException("无法读取配置文件“"+ProjectConstants.CONFIG_FILE_NAME+"”，无法启动客户端");
            }
        }
        return properties.getProperty(key);
    }

    /**
     * 读取配置，如果不存在则使用默认值
     * @author bawy
     * @date 2018/9/10 17:47
     * @param key 配置关键字
     * @param defaultValue 默认值
     * @return 配置内容
     */
    public static String getConfigDefault(String key, String defaultValue){
        String value = getConfig(key);
        if (value == null){
            return defaultValue;
        }else {
            return value;
        }
    }

    /**
     * 读取配置，并且校验配置是否存在
     * @author bawy
     * @date 2018/9/10 17:53
     * @param key 配置关键字
     * @return 配置内容
     */
    public static String getConfigWithCheck(String key) {
        String value = getConfig(key);
        if (value == null) {
            throw new RuntimeException("无法获取配置“"+key+"”的内容");
        }
        return value;
    }


}
