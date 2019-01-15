package com.asiainfo.configcenter.center.vo.configItem;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 配置项查询请求参数对象
 * Created by bawy on 2018/7/31 17:33.
 */
public class ConfigItemQueryReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -319163287063524998L;

    private int envId;
    private String itemKey;
    private String creatorName;
    private long beginTime;
    private long endTime;


    @Override
    public int getAppId() {
        return 0;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
