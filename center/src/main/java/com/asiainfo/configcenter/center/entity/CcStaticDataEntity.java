package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_static_data", schema = "config_center", catalog = "")
public class CcStaticDataEntity {
    private int id;
    private String codeType;
    private String codeValue;
    private String codeDesc;
    private String ext3;
    private String ext2;
    private int creator;
    private Timestamp createTime;
    private int modifier;
    private Timestamp updateTime;
    private byte status;
    private String codeText;
    private Integer ext1;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code_type", nullable = false, length = 255)
    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    @Basic
    @Column(name = "code_value", nullable = false, length = 255)
    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    @Basic
    @Column(name = "code_desc", nullable = true, length = 1000)
    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    @Basic
    @Column(name = "ext3", nullable = true, length = 1000)
    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    @Basic
    @Column(name = "ext2", nullable = true, length = 500)
    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
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

        CcStaticDataEntity that = (CcStaticDataEntity) o;

        if (id != that.id) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (status != that.status) return false;
        if (codeType != null ? !codeType.equals(that.codeType) : that.codeType != null) return false;
        if (codeValue != null ? !codeValue.equals(that.codeValue) : that.codeValue != null) return false;
        if (codeDesc != null ? !codeDesc.equals(that.codeDesc) : that.codeDesc != null) return false;
        if (ext3 != null ? !ext3.equals(that.ext3) : that.ext3 != null) return false;
        if (ext2 != null ? !ext2.equals(that.ext2) : that.ext2 != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (codeType != null ? codeType.hashCode() : 0);
        result = 31 * result + (codeValue != null ? codeValue.hashCode() : 0);
        result = 31 * result + (codeDesc != null ? codeDesc.hashCode() : 0);
        result = 31 * result + (ext3 != null ? ext3.hashCode() : 0);
        result = 31 * result + (ext2 != null ? ext2.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (int) status;
        return result;
    }

    @Basic
    @Column(name = "code_text")
    public String getCodeText() {
        return codeText;
    }

    public void setCodeText(String codeText) {
        this.codeText = codeText;
    }

    @Basic
    @Column(name = "ext1")
    public Integer getExt1() {
        return ext1;
    }

    public void setExt1(Integer ext1) {
        this.ext1 = ext1;
    }
}
