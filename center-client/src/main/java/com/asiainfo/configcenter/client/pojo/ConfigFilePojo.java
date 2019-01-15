package com.asiainfo.configcenter.client.pojo;

import java.util.List;

/**
 * 处理后的配置文件对象
 * Created by bawy on 2018/9/11 16:02.
 */
public class ConfigFilePojo {

    private String fileName;
    private String tempFilePath;
    private List<Strategy> strategies;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }
}
