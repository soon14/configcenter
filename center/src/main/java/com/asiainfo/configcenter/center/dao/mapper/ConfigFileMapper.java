package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigFileEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by oulc on 2018/8/1.
 */
@Repository
@Mapper
public interface ConfigFileMapper {
    @Select("<script>"+
            " select a.*,b.nickname " +
            " from cc_config_file a,cc_user b " +
            " where a.creator = b.id and " +
            " a.status = " + ProjectConstants.STATUS_VALID +
            " and a.app_env_id = #{appEnvId}"+
            " <if test='configFileName != null'> and a.file_name like CONCAT('%','#{configFileName}','%')</if>" +
            " <if test='creatorName != null'> and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='startDate!=null'> and a.create_time &gt;= CONCAT('#{startDate}') </if>" +
            " <if test='endDate!=null'> and a.create_time &lt;= CONCAT('#{endDate}') </if>" +
            " limit #{start},#{size}" +
            "</script>")
    @Results({
            @Result(column = "app_env_id", property = "appEnvId"),
            @Result(column = "strategy_id", property = "strategyId"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "file_version", property = "fileVersion"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "nickname", property = "creatorName")
    })
    List<CXCcConfigFileEntity> findConfigFile(@Param("appEnvId")int appEnvId,
                                              @Param("configFileName")String configFileName,
                                              @Param("creatorName")String creatorName,
                                              @Param("startDate")String startDate,
                                              @Param("endDate")String endDate,
                                              @Param("start")int start,
                                              @Param("size")int size);

    @Select("<script>"+
            " select count(1) " +
            " from cc_config_file a,cc_user b " +
            " where a.creator = b.id and " +
            " a.status = " + ProjectConstants.STATUS_VALID +
            " and a.app_env_id = #{appEnvId}"+
            " <if test='configFileName != null'> and a.file_name like CONCAT('%','#{configFileName}','%')</if>" +
            " <if test='creatorName != null'> and b.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='startDate!=null'> and a.create_time &gt;= CONCAT('#{startDate}') </if>" +
            " <if test='endDate!=null'> and a.create_time &lt;= CONCAT('#{endDate}') </if>" +
            "</script>")
    long findConfigFileCount(@Param("appEnvId")int appEnvId,
                             @Param("configFileName")String configFileName,
                             @Param("creatorName")String creatorName,
                             @Param("startDate")String startDate,
                             @Param("endDate")String endDate);
}
