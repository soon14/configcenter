package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task_oper_record_detail", schema = "config_center", catalog = "")
public class CcTaskOperRecordDetailEntity {
    private int id;
    private int recordId;
    private byte recordDetailType;
    private String recordDetailKey;
    private String recordDetailValue;
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
    @Column(name = "record_id", nullable = false)
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @Basic
    @Column(name = "record_detail_type", nullable = false)
    public byte getRecordDetailType() {
        return recordDetailType;
    }

    public void setRecordDetailType(byte recordDetailType) {
        this.recordDetailType = recordDetailType;
    }

    @Basic
    @Column(name = "record_detail_key", nullable = false, length = 255)
    public String getRecordDetailKey() {
        return recordDetailKey;
    }

    public void setRecordDetailKey(String recordDetailKey) {
        this.recordDetailKey = recordDetailKey;
    }

    @Basic
    @Column(name = "record_detail_value", nullable = false, length = 500)
    public String getRecordDetailValue() {
        return recordDetailValue;
    }

    public void setRecordDetailValue(String recordDetailValue) {
        this.recordDetailValue = recordDetailValue;
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

        CcTaskOperRecordDetailEntity that = (CcTaskOperRecordDetailEntity) o;

        if (id != that.id) return false;
        if (recordId != that.recordId) return false;
        if (recordDetailType != that.recordDetailType) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (recordDetailKey != null ? !recordDetailKey.equals(that.recordDetailKey) : that.recordDetailKey != null)
            return false;
        if (recordDetailValue != null ? !recordDetailValue.equals(that.recordDetailValue) : that.recordDetailValue != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + recordId;
        result = 31 * result + (int) recordDetailType;
        result = 31 * result + (recordDetailKey != null ? recordDetailKey.hashCode() : 0);
        result = 31 * result + (recordDetailValue != null ? recordDetailValue.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
