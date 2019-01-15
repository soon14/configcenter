package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_config_item", schema = "config_center", catalog = "")
public class CcConfigItemEntity {
    private int id;
    private int appEnvId;
    private Integer strategyId;
    private String itemKey;
    private String itemValue;
    private String description;
    private int creator;
    private Timestamp createTime;
    private int modifier;
    private Timestamp updateTime;
    private byte status;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "app_env_id", nullable = false)
    public int getAppEnvId() {
        return appEnvId;
    }

    public void setAppEnvId(int appEnvId) {
        this.appEnvId = appEnvId;
    }

    @Basic
    @Column(name = "strategy_id", nullable = true)
    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    @Basic
    @Column(name = "item_key", nullable = false, length = 255)
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    @Basic
    @Column(name = "item_value", nullable = false, length = 2000)
    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "creator", nullable = false)
    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "modifier", nullable = false)
    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @Basic
    @Column(name = "update_time", nullable = false)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CcConfigItemEntity that = (CcConfigItemEntity) o;

        if (id != that.id) return false;
        if (appEnvId != that.appEnvId) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (strategyId != null ? !strategyId.equals(that.strategyId) : that.strategyId != null) return false;
        if (itemKey != null ? !itemKey.equals(that.itemKey) : that.itemKey != null) return false;
        if (itemValue != null ? !itemValue.equals(that.itemValue) : that.itemValue != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + appEnvId;
        result = 31 * result + (strategyId != null ? strategyId.hashCode() : 0);
        result = 31 * result + (itemKey != null ? itemKey.hashCode() : 0);
        result = 31 * result + (itemValue != null ? itemValue.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
