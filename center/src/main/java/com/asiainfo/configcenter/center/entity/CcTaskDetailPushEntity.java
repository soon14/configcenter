package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task_detail_push", schema = "config_center", catalog = "")
public class CcTaskDetailPushEntity {
    private int id;
    private int taskId;
    private int configId;
    private byte configType;
    private String configName;
    private String configVersion;
    private String instances;
    private String cronTime;
    private String description;
    private int creator;
    private Timestamp createTime;
    private int modifier;
    private Timestamp updateTime;
    private byte status;

    @Id
    @Column(name = "id", nullable = false)
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
    @Column(name = "config_id", nullable = false)
    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
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
    @Column(name = "config_version", nullable = false, length = 100)
    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    @Basic
    @Column(name = "instances", nullable = false, length = 1000)
    public String getInstances() {
        return instances;
    }

    public void setInstances(String instances) {
        this.instances = instances;
    }

    @Basic
    @Column(name = "cron_time", nullable = true, length = 100)
    public String getCronTime() {
        return cronTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
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

        CcTaskDetailPushEntity that = (CcTaskDetailPushEntity) o;

        if (id != that.id) return false;
        if (taskId != that.taskId) return false;
        if (configType != that.configType) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (configName != null ? !configName.equals(that.configName) : that.configName != null) return false;
        if (configVersion != null ? !configVersion.equals(that.configVersion) : that.configVersion != null)
            return false;
        if (instances != null ? !instances.equals(that.instances) : that.instances != null) return false;
        if (cronTime != null ? !cronTime.equals(that.cronTime) : that.cronTime != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskId;
        result = 31 * result + (int) configType;
        result = 31 * result + (configName != null ? configName.hashCode() : 0);
        result = 31 * result + (configVersion != null ? configVersion.hashCode() : 0);
        result = 31 * result + (instances != null ? instances.hashCode() : 0);
        result = 31 * result + (cronTime != null ? cronTime.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
