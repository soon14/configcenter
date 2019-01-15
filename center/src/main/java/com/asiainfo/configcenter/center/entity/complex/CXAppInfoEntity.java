package com.asiainfo.configcenter.center.entity.complex;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 应用信息视图实体类
 * Created by bawy on 2018/7/25 15:29.
 */
@Entity
public class CXAppInfoEntity {

    private int id;
    private String appName;
    private String description;
    private int creator;
    private String createTime;
    private String creatorName;
    private String updateTime;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
