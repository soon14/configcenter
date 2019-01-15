package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_task_oper_record", schema = "config_center", catalog = "")
public class CcTaskOperRecordEntity {
    private int id;
    private int taskId;
    private byte step;
    private int operator;
    private byte operationState;
    private String reason;
    private Timestamp createTime;
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
    @Column(name = "step", nullable = false)
    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
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
    @Column(name = "operation_state", nullable = false)
    public byte getOperationState() {
        return operationState;
    }

    public void setOperationState(byte operationState) {
        this.operationState = operationState;
    }

    @Basic
    @Column(name = "reason", nullable = true, length = 2000)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

        CcTaskOperRecordEntity that = (CcTaskOperRecordEntity) o;

        if (id != that.id) return false;
        if (taskId != that.taskId) return false;
        if (step != that.step) return false;
        if (operator != that.operator) return false;
        if (operationState != that.operationState) return false;
        if (status != that.status) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + taskId;
        result = 31 * result + (int) step;
        result = 31 * result + operator;
        result = 31 * result + (int) operationState;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
