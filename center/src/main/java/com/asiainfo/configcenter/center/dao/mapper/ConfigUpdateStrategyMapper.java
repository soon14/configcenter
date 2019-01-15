package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigUpdateStrategyEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by oulc on 2018/8/10.
 * 策略mybatis
 */
@Repository
@Mapper
public interface ConfigUpdateStrategyMapper {
    @Select("<script>" +
            " select a.*,b.nickname from cc_config_update_strategy a,cc_user b" +
            " where" +
            " a.creator = b.id" +
            " and a.app_id = ${appId}" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " <if test = 'creatorName!=null'> and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test = 'strategyName!=null'> and a.strategy_name like CONCAT('%','#{strategyName}','%')</if>" +
            " <if test = 'startDate!=null'> and a.update_time <![CDATA[>=]]> #{startDate}</if>" +
            " <if test = 'endDate!=null'> and a.update_time <![CDATA[<=]]> #{endDate}</if>" +
            " limit ${start},${size}" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "app_id", property = "appId"),
            @Result(column = "strategy_name", property = "strategyName"),
            @Result(column = "description", property = "description"),
            @Result(column = "creator", property = "creator"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "modifier", property = "modifier"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "status", property = "status"),
            @Result(column = "nickname", property = "creatorName"),
    })
    List<CXCcConfigUpdateStrategyEntity> findConfigUpdateStrategy(@Param("appId") int appId,
                                                                  @Param("creatorName") String creatorName,
                                                                  @Param("strategyName") String strategyName,
                                                                  @Param("startDate") String startDate,
                                                                  @Param("endDate") String endDate,
                                                                  @Param("start") int start,
                                                                  @Param("size") int size);

    @Select("<script>" +
            " select count(1) from cc_config_update_strategy a,cc_user b" +
            " where" +
            " a.creator = b.id" +
            " and a.app_id = ${appId}" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " <if test = 'creatorName!=null'> and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test = 'strategyName!=null'> and a.strategy_name like CONCAT('%','#{strategyName}','%')</if>" +
            " <if test = 'startDate!=null'> and a.update_time <![CDATA[>=]]> #{startDate}</if>" +
            " <if test = 'endDate!=null'> and a.update_time <![CDATA[<=]]> #{endDate}</if>" +
            "</script>")
    long findConfigUpdateStrategyCount(@Param("appId") int appId,
                                       @Param("creatorName") String creatorName,
                                       @Param("strategyName") String strategyName,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate);

}
