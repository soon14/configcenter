package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by oulc on 2018/7/26.
 */
@Entity
@Table(name = "cc_instance", schema = "config-center", catalog = "")
public class CcInstanceEntity {
    private Integer id;
    private Integer envId;
    private String insName;
    private String insIp;
    private Byte isAlive;
    private Timestamp lastConnectTime;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Byte status;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "env_id", nullable = false)
    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    @Basic
    @Column(name = "ins_name", nullable = false, length = 255)
    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    @Basic
    @Column(name = "ins_ip", nullable = true, length = 255)
    public String getInsIp() {
        return insIp;
    }

    public void setInsIp(String insIp) {
        this.insIp = insIp;
    }

    @Basic
    @Column(name = "is_alive", nullable = false)
    public Byte getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Byte isAlive) {
        this.isAlive = isAlive;
    }

    @Basic
    @Column(name = "last_connect_time", nullable = false)
    public Timestamp getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(Timestamp lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
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
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CcInstanceEntity that = (CcInstanceEntity) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(insName, that.insName) &&
                java.util.Objects.equals(insIp, that.insIp) &&
                java.util.Objects.equals(isAlive, that.isAlive) &&
                java.util.Objects.equals(lastConnectTime, that.lastConnectTime) &&
                java.util.Objects.equals(createTime, that.createTime) &&
                java.util.Objects.equals(updateTime, that.updateTime) &&
                java.util.Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, insName, insIp, isAlive, lastConnectTime, createTime, updateTime, status);
    }
}
