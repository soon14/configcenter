package com.asiainfo.configcenter.client.pojo;

import java.util.List;

/**
 * 处理后的配置文件对象
 * Created by bawy on 2018/9/11 16:02.
 */
public class ConfigItemPojo {

    private String itemKey;
    private String itemValue;
    private List<Strategy> strategies;

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

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }
}
