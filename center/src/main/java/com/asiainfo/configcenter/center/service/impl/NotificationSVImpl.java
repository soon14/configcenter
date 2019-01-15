package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.controller.NotificationWSController;
import com.asiainfo.configcenter.center.dao.mapper.NotificationMapper;
import com.asiainfo.configcenter.center.dao.repository.NotificationRepository;
import com.asiainfo.configcenter.center.entity.CcNotificationEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcNotificationEntity;
import com.asiainfo.configcenter.center.service.interfaces.INotificationSV;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.system.NotificationVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NotificationSVImpl implements INotificationSV {
    @Resource
    private NotificationRepository notificationRepository;

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    public long getNotReadNotificationCount(int userId) {
        return notificationMapper.findComplexCountByConsumerAndHasRead(userId,ProjectConstants.STATUS_INVALID);
    }

    private Logger logger = Logger.getLogger(NotificationSVImpl.class);

    @Override
    public PageResultContainer<CXCcNotificationEntity> getNotification(PageRequestContainer<NotificationVO> pageRequestContainer, int userId) {
        checkGetNotificationParam(pageRequestContainer,userId);

        String creator = pageRequestContainer.getData().getCreator();//申请人 默认为0

        byte hadRead = pageRequestContainer.getData().getHasRead();//是否已读 1：已读 0：未读

        byte type = pageRequestContainer.getData().getType();//消息类型 默认为0

        int currentPage = pageRequestContainer.getCurrentPage();

        int pageSize = pageRequestContainer.getPageSize();

        return getNotification(userId,creator,hadRead,type,currentPage,pageSize);
    }

    @Override

    public PageResultContainer<CXCcNotificationEntity> getNotification(int consumer ,String creator, byte hasRead, byte type, int currentPage, int size) {
        PageResultContainer<CXCcNotificationEntity> pageResultContainer = new PageResultContainer<>();
        List<CXCcNotificationEntity> list = notificationMapper.findComplexByConsumerAndCreatorAndType(consumer,creator,type,hasRead,currentPage*size,size);
        long count = notificationMapper.findCountComplexByConsumerAndCreatorAndType(consumer,creator,type,hasRead);
        pageResultContainer.setCount(count);
        pageResultContainer.setEntities(list);
        return pageResultContainer;
    }

    /**
     * 校验参数
     * @param pageRequestContainer 参数
     * @param userId 用户主键
     */
    private void checkGetNotificationParam(PageRequestContainer<NotificationVO> pageRequestContainer,int userId){
        //校验页数参数
        ValidateUtil.checkPageParam(pageRequestContainer);
    }

    @Override
    public void consumeNotification(int id, int userId) {
        Assert4CC.isTrue(id != 0,"消息主键不能为空");
        CcNotificationEntity ccNotificationEntity = notificationRepository.findOneByIdAndConsumerAndStatus(id,userId,ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(ccNotificationEntity,"用户没有此条消息,消息主键:"+id);
        ccNotificationEntity.setHasRead(ProjectConstants.STATUS_VALID);
        notificationRepository.save(ccNotificationEntity);

        //通知前端重新获取数量
        notifyUser(userId);
    }

    @Override
    public void notifyUser(int userId) {
        try {
            NotificationWSController.sendMessage(userId,getNotReadNotificationCount(userId));
        }catch (Exception e){
            logger.error("消息通知出错");
            logger.error(ErrorInfo.errorInfo(e));
        }
    }

    @Transactional
    @Override
    public void createNotification(CcNotificationEntity notificationEntity) {
        if (notificationEntity!=null) {
            notificationRepository.saveAndFlush(notificationEntity);
            notifyUser(notificationEntity.getConsumer());
        }
    }
}
