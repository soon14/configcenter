package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.CcNotificationEntity;

import java.sql.Timestamp;

/**
 * 消息通知工具类
 * Created by bawy on 2018/7/17 15:29.
 */
public class NotificationUtil {

    /**
     * 通知类型
     * 1：注册审核通知
     * 2：注册审核通过通知
     * 3：任务审核通知
     * 4：任务审核结果通知
     */
    public static final byte NOTIFICATION_TYPE_REGISTER = 1;
    public static final byte NOTIFICATION_TYPE_REGISTER_OK = 2;
    public static final byte NOTIFICATION_TYPE_TASK_REVIEW = 3;
    public static final byte NOTIFICATION_TYPE_REVIEW_RESULT = 4;

    /**
     * 是否已读
     * 0：未读
     * 1：已读
     */
    public static final byte NOT_READ = 0;
    public static final byte HAS_READ = 1;


    /**
     * 创建消息对象
     * @param type 消息类型，从常量中选取
     * @param title 消息标题
     * @param content 消息内容
     * @param consumer 消息消费者
     * @param creator 消息创建者
     * @param link 详情链接
     * @return 消息对象
     */
    public static CcNotificationEntity createNotification(byte type, String title, String content, int consumer, int creator, String link){
        CcNotificationEntity notificationEntity = new CcNotificationEntity();
        notificationEntity.setNotificationType(type);
        notificationEntity.setNotificationTitle(title);
        notificationEntity.setNotificationContent(content);
        notificationEntity.setConsumer(consumer);
        notificationEntity.setDetailInfoLink(link);
        notificationEntity.setHasRead(NOT_READ);
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        notificationEntity.setCreator(creator);
        notificationEntity.setCreateTime(nowTime);
        notificationEntity.setModifier(creator);
        notificationEntity.setUpdateTime(nowTime);
        notificationEntity.setStatus(ProjectConstants.STATUS_VALID);
        return notificationEntity;
    }

}
