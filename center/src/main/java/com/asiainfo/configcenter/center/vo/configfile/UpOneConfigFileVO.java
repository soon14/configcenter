package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/1.
 */
public class UpOneConfigFileVO implements BaseAppReqVO{
    private int envId;
    private Integer strategyId;
    private UpConfigFileVO file;
    private String fileSaveDir;
    private String desc;

    @Override
    public int getAppId() {
        return 0;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public String getFileSaveDir() {
        return fileSaveDir;
    }

    public void setFileSaveDir(String fileSaveDir) {
        this.fileSaveDir = fileSaveDir;
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
}
