package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.complex.CXCcAppEnvEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AppEnvMapper {

    @Select("<script>" +
            " select a.*,b.nickname as creator_name" +
            " from cc_app_env a,cc_user b where " +
            " a.creator = b.id " +
            " and a.app_id = #{appId} " +
            " and a.status =" + ProjectConstants.STATUS_VALID +
            "</script>")
    List<CXCcAppEnvEntity> findCXAppEnvByAppId(@Param(value = "appId") int appId);

    @Select("<script>" +
            " select count(1) " +
            " from cc_app a,cc_app_env b,cc_app_user_rel c" +
            " where a.id = b.app_id " +
            " and a.id = c.app_id " +
            " and c.user_id = #{userId}" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " and b.status = " + ProjectConstants.STATUS_VALID +
            " and c.status = " + ProjectConstants.STATUS_VALID +
            "</script>")
    long getEnvCountByUserId( @Param("userId")int userId);

    @Select("<script>" +
            " select count(1) " +
            " from cc_app a,cc_app_env b" +
            " where a.id = b.app_id " +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " and b.status = " + ProjectConstants.STATUS_VALID +
            "</script>")
    long getEnvCount();
}
