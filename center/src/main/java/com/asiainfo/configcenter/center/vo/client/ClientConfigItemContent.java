package com.asiainfo.configcenter.center.vo.client;

import java.io.Serializable;

/**
 * Author: Erick
 * Date: 2018/8/30
 *
 * 配置项内容
 */
public class ClientConfigItemContent implements Serializable {

    private static final long serialVersionUID = 3511918870268393454L;

    private int id;
    private Integer strategyId;
    private String itemKey;
    private String itemValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
