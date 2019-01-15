package com.asiainfo.configcenter.center.vo.configItem;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 配置项历史查询列表
 * Created by bawy on 2018/8/7 15:38.
 */
public class ConfigItemHisVO implements Serializable {

    private static final long serialVersionUID = 6565430259066033191L;

    private int hisId;
    private String creatorName;
    private String itemKey;
    private String itemValue;
    private Integer strategyId;
    private String description;
    private Timestamp createTime;

    public int getHisId() {
        return hisId;
    }

    public void setHisId(int hisId) {
        this.hisId = hisId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }
}
