package com.asiainfo.configcenter.center.vo.configItem;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 配置项新增、修改、删除请求对象
 * Created by bawy on 2018/8/1 10:22.
 */
public class ConfigItemReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -2088341687910949625L;

    private int envId;
    private Integer strategyId;
    private String itemKey;
    private String itemValue;
    private String modOption;
    private String description;


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

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModOption() {
        return modOption;
    }

    public void setModOption(String modOption) {
        this.modOption = modOption;
    }
}
