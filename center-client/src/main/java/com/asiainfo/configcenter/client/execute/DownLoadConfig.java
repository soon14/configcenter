package com.asiainfo.configcenter.client.execute;


import com.asiainfo.configcenter.client.common.ConfigLoader;
import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.exception.FileException;
import com.asiainfo.configcenter.client.execute.strategy.StrategyConverter;
import com.asiainfo.configcenter.client.pojo.AllConfigItemPojo;
import com.asiainfo.configcenter.client.pojo.ConfigFilePojo;
import com.asiainfo.configcenter.client.pojo.ConfigItemPojo;
import com.asiainfo.configcenter.client.util.FileUtil;
import com.asiainfo.configcenter.client.util.HTTPClientUtils;
import com.asiainfo.configcenter.client.util.JSONUtil;
import com.asiainfo.configcenter.client.vo.ClientReqVO;
import com.asiainfo.configcenter.client.vo.ClientRespVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigVO;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownLoadConfig {

    /**
     * 配置文件的内容和策略信息下载
     *
     * @author zhangxiangxin
     * @param configFiles 待下载配置文件集合
     * @return 处理后的配置文件对象
     * */
    public static List<ConfigFilePojo> downLoadConfigFileAndStrategy(List<ZKConfigVO> configFiles) throws IOException{
        List<ClientRespVO> clientRespVOS = downloadConfigAndStrategy(configFiles, ProjectConstants.DOWN_LOAD_FILE_URL);
        return dealConfigFile(clientRespVOS);
    }

    /**
     * 处理下载后的文件
     * 1.保存到临时文件夹
     * 2.转换刷新策略
     * @author bawy
     * @date 2018/9/11 16:11
     * @param clientRespList 下载后的配置文件集合
     * @return 处理后的配置文件对象
     */
    private static List<ConfigFilePojo> dealConfigFile(List<ClientRespVO> clientRespList){
        //创建临时目录
        String tempDirPath = System.getProperty("user.home") + File.separator + ProjectConstants.CONFIG_FILE_TEMP_DIR + File.separator + System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        File tempDir = new File(tempDirPath);
        if (!tempDir.exists()){
            if (!tempDir.mkdirs()){
                throw new FileException("创建临时目录“"+tempDirPath+"”失败");
            }
        }
        List<ConfigFilePojo> configFilePojos = new ArrayList<ConfigFilePojo>();
        for (ClientRespVO clientResp : clientRespList) {
            String fileName = clientResp.getName();
            File tempFile = new File(tempDir, fileName);
            FileUtil.writeStringToFile(tempFile, clientResp.getContent());
            ConfigFilePojo configFilePojo = new ConfigFilePojo();
            configFilePojo.setFileName(fileName);
            configFilePojo.setTempFilePath(tempFile.getAbsolutePath());
            configFilePojo.setStrategies( StrategyConverter.transferServerBeanToClientBean(clientResp.getConfigUpdateStrategyVO().getStrategyStepVOS()));
            configFilePojos.add(configFilePojo);
        }
        return configFilePojos;
    }

    /**
     * 配置项内容和策略信息下载
     *
     * @author Erick
     * @date 2018/9/3
     * @param configItems 配置项
     * @return itemSteps 配置项策略步骤
     * */
    public static AllConfigItemPojo downloadConfigItemAndStrategy(List<ZKConfigVO> configItems) throws IOException {
        //请求参数
        List<ClientRespVO> clientRespVOS = downloadConfigAndStrategy(configItems, ProjectConstants.DOWNLOAD_CONFIG_ITEM_URL);
        return dealConfigItem(clientRespVOS);
    }

    private static AllConfigItemPojo dealConfigItem(List<ClientRespVO> clientRespList){
        AllConfigItemPojo allConfigItemPojo = new AllConfigItemPojo();
        List<ConfigItemPojo> configItemPojos = new ArrayList<ConfigItemPojo>();
        Map<String, String> itemMap = new HashMap<String, String>();
        for (ClientRespVO clientResp : clientRespList) {
            ConfigItemPojo configItemPojo = new ConfigItemPojo();
            String itemKey = clientResp.getName();
            String itemValue = clientResp.getContent();
            configItemPojo.setItemKey(itemKey);
            configItemPojo.setItemValue(itemValue);
            configItemPojo.setStrategies( StrategyConverter.transferServerBeanToClientBean(clientResp.getConfigUpdateStrategyVO().getStrategyStepVOS()));
            itemMap.put(itemKey, itemValue);
            configItemPojos.add(configItemPojo);
        }
        allConfigItemPojo.setItemMap(itemMap);
        allConfigItemPojo.setConfigItemPojos(configItemPojos);
        return allConfigItemPojo;
    }

    private static List<ClientRespVO> downloadConfigAndStrategy(List<ZKConfigVO> configs, String url) throws IOException{
        String projectName = System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        String envName = System.getProperty(ProjectConstants.CLIENT_ENV_NAME);
        ClientReqVO clientReqVO = new ClientReqVO();
        clientReqVO.setAppName(projectName);
        clientReqVO.setEnvName(envName);
        clientReqVO.setZkConfigs(configs);
        JsonElement configInfo = HTTPClientUtils.post(getServerUrl() + url, clientReqVO);
        return JSONUtil.jsonStrToBean(configInfo.toString(), new TypeToken<ArrayList<ClientRespVO>>(){}.getType());
    }

    private static String getServerUrl() {
        return ConfigLoader.getConfigWithCheck(ProjectConstants.CONFIG_KEY_SERVER_HOST);
    }

}