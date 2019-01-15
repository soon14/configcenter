package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task_detail_config", schema = "config_center", catalog = "")
public class CcTaskDetailConfigEntity {
    private int id;
    private int taskId;
    private byte changeType;
    private byte configType;
    private String configName;
    private String configContent;
    private String configDesc;
    private int creator;
    private Timestamp createTime;
    private int modifier;
    private Timestamp updateTime;
    private byte status;
    private Integer configId;
    private Integer strategyId;

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
    @Column(name = "task_id", nullable = false)
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "change_type", nullable = false)
    public byte getChangeType() {
        return changeType;
    }

    public void setChangeType(byte changeType) {
        this.changeType = changeType;
    }

    @Basic
    @Column(name = "config_type", nullable = false)
    public byte getConfigType() {
        return configType;
    }

    public void setConfigType(byte configType) {
        this.configType = configType;
    }

    @Basic
    @Column(name = "config_name", nullable = false, length = 50)
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @Basic
    @Column(name = "config_content", nullable = true, length = 2000)
    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    @Basic
    @Column(name = "config_desc", nullable = true, length = 1000)
    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
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

        CcTaskDetailConfigEntity that = (CcTaskDetailConfigEntity) o;

        if (id != that.id) return false;
        if (taskId != that.taskId) return false;
        if (changeType != that.changeType) return false;
        if (configType != that.configType) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (configName != null ? !configName.equals(that.configName) : that.configName != null) return false;
        if (configContent != null ? !configContent.equals(that.configContent) : that.configContent != null)
            return false;
        if (configDesc != null ? !configDesc.equals(that.configDesc) : that.configDesc != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskId;
        result = 31 * result + (int) changeType;
        result = 31 * result + (int) configType;
        result = 31 * result + (configName != null ? configName.hashCode() : 0);
        result = 31 * result + (configContent != null ? configContent.hashCode() : 0);
        result = 31 * result + (configDesc != null ? configDesc.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }

    @Basic
    @Column(name = "config_id")
    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    @Basic
    @Column(name = "strategy_id")
    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }
}
