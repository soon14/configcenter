package com.asiainfo.configcenter.center.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @Author: bawy
 * @Description:
 * @Dete: 2018/7/3 15:49
 */
@Entity
@Table(name = "cc_notification", schema = "config_center", catalog = "")
public class CcNotificationEntity {

    private int id;
    private int consumer;
    private byte notificationType;
    private String notificationTitle;
    private String notificationContent;
    private byte hasRead;
    private Timestamp createTime;
    private byte status;
    private String detailInfoLink;
    private int creator;
    private int modifier;
    private Timestamp updateTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "consumer")
    public int getConsumer() {
        return consumer;
    }

    public void setConsumer(int consumer) {
        this.consumer = consumer;
    }

    @Basic
    @Column(name = "notification_type")
    public byte getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(byte notificationType) {
        this.notificationType = notificationType;
    }

    @Basic
    @Column(name = "notification_title")
    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    @Basic
    @Column(name = "notification_content")
    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    @Basic
    @Column(name = "has_read")
    public byte getHasRead() {
        return hasRead;
    }

    public void setHasRead(byte hasRead) {
        this.hasRead = hasRead;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "status")
    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "detail_info_link")
    public String getDetailInfoLink() {
        return detailInfoLink;
    }

    public void setDetailInfoLink(String detailInfoLink) {
        this.detailInfoLink = detailInfoLink;
    }

    @Basic
    @Column(name = "creator")
    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    @Basic
    @Column(name = "modifier")
    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CcNotificationEntity that = (CcNotificationEntity) o;

        if (id != that.id) return false;
        if (consumer != that.consumer) return false;
        if (notificationType != that.notificationType) return false;
        if (hasRead != that.hasRead) return false;
        if (status != that.status) return false;
        if (creator != that.creator) return false;
        if (modifier != that.modifier) return false;
        if (notificationTitle != null ? !notificationTitle.equals(that.notificationTitle) : that.notificationTitle != null)
            return false;
        if (notificationContent != null ? !notificationContent.equals(that.notificationContent) : that.notificationContent != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (detailInfoLink != null ? !detailInfoLink.equals(that.detailInfoLink) : that.detailInfoLink != null)
            return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + consumer;
        result = 31 * result + (int) notificationType;
        result = 31 * result + (notificationTitle != null ? notificationTitle.hashCode() : 0);
        result = 31 * result + (notificationContent != null ? notificationContent.hashCode() : 0);
        result = 31 * result + (int) hasRead;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (int) status;
        result = 31 * result + (detailInfoLink != null ? detailInfoLink.hashCode() : 0);
        result = 31 * result + creator;
        result = 31 * result + modifier;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }
}

