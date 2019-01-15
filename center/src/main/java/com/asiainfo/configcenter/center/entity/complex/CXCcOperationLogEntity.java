package com.asiainfo.configcenter.center.entity.complex;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by oulc on 2018/7/20.
 */
@Entity
@Table(name = "cc_operation_log", schema = "config_center", catalog = "")
@SecondaryTable(name = "cc_user", schema = "config_center",foreignKey = @ForeignKey(name = ""))
public class CXCcOperationLogEntity {

    private int id;
    private int operator;
    private String operationType;
    private String operationDesc;
    private Timestamp createTime;
    private byte status;
    private String nickname;



    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "operator", nullable = false)
    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    @Basic
    @Column(name = "operation_type", nullable = false, length = 50)
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Basic
    @Column(name = "operation_desc", nullable = true, length = 1000)
    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
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
    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "nickname", nullable = false)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
