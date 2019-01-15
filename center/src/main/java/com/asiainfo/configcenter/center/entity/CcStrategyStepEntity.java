package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_strategy_step", schema = "config_center", catalog = "")
public class CcStrategyStepEntity {
    private int id;
    private int strategyId;
    private String stepType;
    private byte stepNumber;
    private String description;
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
    @Column(name = "strategy_id", nullable = false)
    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    @Basic
    @Column(name = "step_type", nullable = false, length = 1)
    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    @Basic
    @Column(name = "step_number", nullable = false)
    public byte getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(byte stepNumber) {
        this.stepNumber = stepNumber;
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

        CcStrategyStepEntity that = (CcStrategyStepEntity) o;

        if (id != that.id) return false;
        if (strategyId != that.strategyId) return false;
        if (stepNumber != that.stepNumber) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (stepType != null ? !stepType.equals(that.stepType) : that.stepType != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + strategyId;
        result = 31 * result + (stepType != null ? stepType.hashCode() : 0);
        result = 31 * result + (int) stepNumber;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }
}
