package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task_ext_info", schema = "config_center", catalog = "")
public class CcTaskExtInfoEntity {
    private int id;
    private int taskId;
    private String extInfoKey;
    private String extInfoValue;
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
    @Column(name = "task_id", nullable = false)
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "ext_info_key", nullable = false, length = 255)
    public String getExtInfoKey() {
        return extInfoKey;
    }

    public void setExtInfoKey(String extInfoKey) {
        this.extInfoKey = extInfoKey;
    }

    @Basic
    @Column(name = "ext_info_value", nullable = false, length = 500)
    public String getExtInfoValue() {
        return extInfoValue;
    }

    public void setExtInfoValue(String extInfoValue) {
        this.extInfoValue = extInfoValue;
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

        CcTaskExtInfoEntity that = (CcTaskExtInfoEntity) o;

        if (id != that.id) return false;
        if (taskId != that.taskId) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (extInfoKey != null ? !extInfoKey.equals(that.extInfoKey) : that.extInfoKey != null) return false;
        if (extInfoValue != null ? !extInfoValue.equals(that.extInfoValue) : that.extInfoValue != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskId;
        result = 31 * result + (extInfoKey != null ? extInfoKey.hashCode() : 0);
        result = 31 * result + (extInfoValue != null ? extInfoValue.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
