package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task", schema = "config_center", catalog = "")
public class CcTaskEntity {
    private int id;
    private int appEnvId;
    private String taskName;
    private Byte taskType;
    private String description;
    private byte taskState;
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
    @Column(name = "task_name", nullable = false, length = 255)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Basic
    @Column(name = "task_type", nullable = true)
    public Byte getTaskType() {
        return taskType;
    }

    public void setTaskType(Byte taskType) {
        this.taskType = taskType;
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
    @Column(name = "task_state", nullable = false)
    public byte getTaskState() {
        return taskState;
    }

    public void setTaskState(byte taskState) {
        this.taskState = taskState;
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

        CcTaskEntity that = (CcTaskEntity) o;

        if (id != that.id) return false;
        if (appEnvId != that.appEnvId) return false;
        if (taskState != that.taskState) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (taskName != null ? !taskName.equals(that.taskName) : that.taskName != null) return false;
        if (taskType != null ? !taskType.equals(that.taskType) : that.taskType != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + appEnvId;
        result = 31 * result + (taskName != null ? taskName.hashCode() : 0);
        result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (int) taskState;
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
