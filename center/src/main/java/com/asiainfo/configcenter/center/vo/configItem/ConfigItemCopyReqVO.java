package com.asiainfo.configcenter.center.vo.configItem;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

import java.util.List;

/**
 * 配置项复制请求参数
 * Created by bawy on 2018/8/14 9:19.
 */
public class ConfigItemCopyReqVO implements BaseAppReqVO{

    private static final long serialVersionUID = 8479256290381700850L;

    private int envId;
    private int sourceEnvId;
    private List<Integer> configItemIds;

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

    public int getSourceEnvId() {
        return sourceEnvId;
    }

    public void setSourceEnvId(int sourceEnvId) {
        this.sourceEnvId = sourceEnvId;
    }

    public List<Integer> getConfigItemIds() {
        return configItemIds;
    }

    public void setConfigItemIds(List<Integer> configItemIds) {
        this.configItemIds = configItemIds;
    }

}
