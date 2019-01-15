package com.asiainfo.configcenter.center.entity.complex;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by oulc on 2018/7/20.
 */
@Entity
@Table(name = "cc_notification", schema = "config_center", catalog = "")
@SecondaryTable(name = "cc_user", schema = "config_center")
public class CXCcNotificationEntity {
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
    private String CreatorNickName;
    private int modifier;
    private Timestamp updateTime;

    @Id
    @Column(name = "id")
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

    @Basic
    @Column(name = "creator_nick_name")
    public String getCreatorNickName() {
        return CreatorNickName;
    }

    public void setCreatorNickName(String creatorNickName) {
        CreatorNickName = creatorNickName;
    }
}
