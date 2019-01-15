package com.asiainfo.configcenter.center.vo.configfile;

/**
 * Created by oulc on 2018/8/7.
 */
public class QueryConfigContentVO {
    private int configFileId;
    private String configFileVersion;

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
}
