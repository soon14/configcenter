package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/6.
 */
public class UpOneConfigFileForUpdateVO implements BaseAppReqVO {
    private int envId;
    private int configId;
    private Integer strategyId;
    private UpConfigFileVO file;
    private String desc;

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public UpConfigFileVO getFile() {
        return file;
    }

    public void setFile(UpConfigFileVO file) {
        this.file = file;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    @Override
    public int getAppId() {
        return 0;
    }
}
