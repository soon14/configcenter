package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.dao.repository.NotificationRepository;
import com.asiainfo.configcenter.center.service.interfaces.INotificationSV;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationSVImplTest {

    @Resource
    private INotificationSV notificationSV;

    @Resource
    private NotificationRepository notificationRepository;
    @Test
    public void getNotReadNotificationCount() {
        System.out.println(notificationSV.getNotReadNotificationCount(100411));
    }
}