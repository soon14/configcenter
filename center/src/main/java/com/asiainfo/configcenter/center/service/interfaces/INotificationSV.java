package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcNotificationEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcNotificationEntity;
import com.asiainfo.configcenter.center.vo.system.NotificationVO;

public interface INotificationSV {

    /**
     * 查询用户未读消息数量
     * @param userId 用户主键
     * @return 未读消息数量
     * @author oulc
     * @date 2018/7/20 18:00
     */
    long getNotReadNotificationCount(int userId);

    /**
     * 查询用户通知信息
     * @param pageRequestContainer 查询条件
     * @param userId 当前用户主键
     * @return 通知信息列表
     */
    PageResultContainer<CXCcNotificationEntity> getNotification(PageRequestContainer<NotificationVO> pageRequestContainer, int userId);

    /**
     * 查询用户通知信息
     * @param consumer 消息消费者用户主键
     * @param creator 消息创建人
     * @param hasRead 是否已读
     * @param type 消息类型
     * @param currentPage 当前页
     * @param size 页大小
     * @return 消息
     */

    PageResultContainer<CXCcNotificationEntity> getNotification(int consumer ,String creator , byte hasRead , byte type,int currentPage,int size);

    /**
     * 消费通知消息
     * @param id 消息主键
     * @param userId 操作人
     * @author oulc
     * @date 2018/7/23 14:02
     */
    void consumeNotification(int id,int userId);

    /**
     * 通知用户需要重新获取消息数量
     * @param userId 用户主键
     * @author oulc
     * @date 2018/7/25 11:05
     */
    void notifyUser(int userId);

    /**
     * 生成通知消息
     * @author bawy
     * @date 2018/7/31 17:08
     * @param notificationEntity 通知消息对象
     */
    void createNotification(CcNotificationEntity notificationEntity);

}
