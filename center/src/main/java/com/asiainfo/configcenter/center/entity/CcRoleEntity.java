package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_role", schema = "config_center", catalog = "")
public class CcRoleEntity {
    private int id;
    private byte roleType;
    private String roleName;
    private byte roleLevel;
    private String description;
    private Timestamp createTime;
    private int modifier;
    private Timestamp updateTime;
    private byte status;
    private int creator;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_type", nullable = false)
    public byte getRoleType() {
        return roleType;
    }

    public void setRoleType(byte roleType) {
        this.roleType = roleType;
    }

    @Basic
    @Column(name = "role_name", nullable = false, length = 255)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "role_level", nullable = false)
    public byte getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(byte roleLevel) {
        this.roleLevel = roleLevel;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        CcRoleEntity that = (CcRoleEntity) o;

        if (id != that.id) return false;
        if (roleType != that.roleType) return false;
        if (roleLevel != that.roleLevel) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (roleName != null ? !roleName.equals(that.roleName) : that.roleName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) roleType;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (int) roleLevel;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }

    @Basic
    @Column(name = "creator")
    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
