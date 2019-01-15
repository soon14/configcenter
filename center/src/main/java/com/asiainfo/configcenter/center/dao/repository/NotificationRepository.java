package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 消息表数据库层访问接口
 * Created by bawy on 2018/7/17 15:48.
 */
@Repository
public interface NotificationRepository extends JpaRepository<CcNotificationEntity, Integer> , JpaSpecificationExecutor<CcNotificationEntity> {

    @Query( value = "select count(*) from cc_notification a where a.consumer = :consumer and a.has_read=:hasRead",nativeQuery = true)
    long findCountByHasReadAndConsumer(@Param(value = "consumer") int consumer , @Param(value = "hasRead") byte hasRead);

    CcNotificationEntity findOneByIdAndConsumerAndStatus(int id , int consumer,byte status);


}
