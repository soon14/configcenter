package com.asiainfo.configcenter.center.po;

/**
 * Created by oulc on 2018/7/27.
 */
public class InstanceConfigPojo {
    private int configType;
    private String configName;
    private String configVersion;
    private long commitTime;

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public long getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(long commitTime) {
        this.commitTime = commitTime;
    }
}
