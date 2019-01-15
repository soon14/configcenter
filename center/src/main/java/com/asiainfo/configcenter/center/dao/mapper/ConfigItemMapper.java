package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.vo.configItem.ConfigItemHisVO;
import com.asiainfo.configcenter.center.vo.configItem.ConfigItemVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 配置项数据库访问接口（mybaties）
 * Created by bawy on 2018/7/30 11:09.
 */
@Repository
@Mapper
public interface ConfigItemMapper {


    /**
     * 根据条件查询符合条件的配置项
     * @author bawy
     * @date 2018/8/1 9:56
     * @param envId 环境标识
     * @param itemKey 配置项
     * @param creatorName 创建时间
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param start 开始下标
     * @param size 数量
     * @return 配置项集合
     */
    @Select("<script>" +
            " select a.id,a.app_env_id,a.strategy_id,a.item_key,a.item_value,a.description,a.creator,a.update_time,b.nickname" +
            " from cc_config_item a,cc_user b" +
            " where a.creator = b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and a.app_env_id=#{envId}" +
            " <if test='itemKey != null'>and a.item_key like CONCAT('%','#{itemKey}','%')</if>" +
            " <if test='creatorName != null'>and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and a.update_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and a.update_time <![CDATA[<=]]> #{endTime}</if>" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "app_env_id", property = "envId"),
            @Result(column = "strategy_id", property = "strategyId"),
            @Result(column = "item_key", property = "itemKey"),
            @Result(column = "item_value", property = "itemValue"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "nickname", property = "creatorName")
    })
    List<ConfigItemVO> getConfigItems(@Param("envId") int envId, @Param("itemKey") String itemKey, @Param("creatorName") String creatorName,
                                      @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime,
                                      @Param("start") int start, @Param("size") int size);

    /**
     * 根据条件获取符合条件的配置项数量
     * @author bawy
     * @date 2018/7/31 22:55
     * @param envId 环境标识
     * @param creatorName 创建人
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 数量
     */
    @Select("<script>select count(1)" +
            " from cc_config_item a,cc_user b" +
            " where a.creator = b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and a.app_env_id=#{envId}" +
            " <if test='itemKey != null'>and a.item_key like CONCAT('%','#{itemKey}','%')</if>" +
            " <if test='creatorName != null'>and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and a.update_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and a.update_time <![CDATA[<=]]> #{endTime}</if>" +
            "</script>")
    int getConfigItemsCount(@Param("envId") int envId, @Param("itemKey") String itemKey, @Param("creatorName") String creatorName,
                            @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);

    /**
     * 获取配置项历史版本内容
     * @author bawy
     * @date 2018/8/7 18:10
     * @param configItemId 配置项标识
     * @param creatorName 创建人名称
     * @param size 数量
     * @return 配置项历史版本内容列表
     */
    @Select("<script>select a.id,a.item_key,a.item_value,a.description,a.create_time,b.nickname" +
            " from cc_config_item_his a,cc_user b" +
            " where a.creator=b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and a.item_id=#{configItemId}" +
            " <if test='creatorName != null'>and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " order by a.create_time desc" +
            " limit 0,#{size}"+
            "</script>")
    @Results({
            @Result(column = "id", property = "hisId"),
            @Result(column = "nickname", property = "creatorName"),
            @Result(column = "item_key", property = "itemKey"),
            @Result(column = "item_value", property = "itemValue"),
            @Result(column = "create_time", property = "createTime"),
    })
    List<ConfigItemHisVO> getConfigItemHis(@Param("configItemId") int configItemId, @Param("creatorName") String creatorName, @Param("size") int size);

}
