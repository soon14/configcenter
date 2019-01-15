package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/7/31.
 * 上传配置文件VO
 */
public class UpManyConfigFileVO implements BaseAppReqVO {
    private int envId;
    private UpConfigFileVO zipFile;

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

    public UpConfigFileVO getZipFile() {
        return zipFile;
    }

    public void setZipFile(UpConfigFileVO zipFile) {
        this.zipFile = zipFile;
    }
}
