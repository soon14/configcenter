package com.asiainfo.configcenter.client.vo;

public class ConfigFileContent {
    private int configFileId;
    private String configFileVersion;
    private String configFileContent;
    private String configFileName;
    private Integer strategyId;

    public int getConfigFileId() {
        return configFileId;
    }

    public void setConfigFileId(int configFileId) {
        this.configFileId = configFileId;
    }

    public String getConfigFileVersion() {
        return configFileVersion;
    }

    public void setConfigFileVersion(String configFileVersion) {
        this.configFileVersion = configFileVersion;
    }

    public String getConfigFileContent() {
        return configFileContent;
    }

    public void setConfigFileContent(String configFileContent) {
        this.configFileContent = configFileContent;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }
}
