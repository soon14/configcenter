package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.complex.CXCcNotificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotificationMapper {
    @Select(" <script>" +
            " select a.*,b.nickname as CreatorNickName" +
            " from cc_notification a,cc_user b " +
            " where a.creator = b.id" +
            " and a.has_read = ${hasRead}" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " <if test='consumer != 0'> and a.consumer = #{consumer}</if>" +
            " <if test='creator != null'> and b.nickname like CONCAT('%','#{creator}','%')</if>" +
            " <if test='notificationType!=0'> and a.notification_type = #{notificationType}</if>" +
            " ORDER BY a.create_time" +
            " limit #{start},#{size}" +
            " </script>")
    List<CXCcNotificationEntity> findComplexByConsumerAndCreatorAndType(@Param("consumer")int consumer,
                                                                        @Param("creator") String creator,
                                                                        @Param("notificationType") byte notificationType,
                                                                        @Param("hasRead") byte hasRead,
                                                                        @Param("start")int start,
                                                                        @Param("size")int size);

    @Select(" <script>" +
            " select count(1) as CreatorNickName" +
            " from cc_notification a,cc_user b " +
            " where a.creator = b.id" +
            " and a.has_read = #{hasRead}" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " <if test='consumer != 0'> and a.consumer = #{consumer}</if>" +
            " <if test='creator != null'> and b.nickname like CONCAT('%','#{creator}','%')</if>" +
            " <if test='notificationType!=0'> and a.notification_type = #{notificationType}</if>" +
            " </script>")
    long findCountComplexByConsumerAndCreatorAndType(@Param("consumer")int consumer,
                                                @Param("creator") String creator,
                                                @Param("notificationType") byte notificationType,
                                                @Param("hasRead") byte hasRead);

    @Select("<script>" +
            " select count(1) from cc_notification a,cc_user b" +
            " where a.creator = b.id" +
            " AND a.status = " + ProjectConstants.STATUS_VALID +
            "<if test='consumer!=0'> and consumer=#{consumer} </if>" +
            "<if test='hasRead!=-1'> and a.has_read = #{hasRead}</if>" +
            "</script>")
    long findComplexCountByConsumerAndHasRead(@Param(value = "consumer")int consumer,
                                              @Param(value = "hasRead") byte hasRead);
}
