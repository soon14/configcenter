package com.asiainfo.configcenter.center.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */

@Entity
@Table(name = "cc_user_ext_info", schema = "config_center", catalog = "")
public class CcUserExtInfoEntity {
    private int id;
    private int userId;
    private String extInfoKey;
    private String extInfoValue;
    private Timestamp createTime;
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
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
    @Column(name = "ext_info_value", nullable = false, length = 255)
    public String getExtInfoValue() {
        return extInfoValue;
    }

    public void setExtInfoValue(String extInfoValue) {
        this.extInfoValue = extInfoValue;
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

        CcUserExtInfoEntity that = (CcUserExtInfoEntity) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
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
        result = 31 * result + userId;
        result = 31 * result + (extInfoKey != null ? extInfoKey.hashCode() : 0);
        result = 31 * result + (extInfoValue != null ? extInfoValue.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
