package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.complex.CXAppInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AppMapper {

    @Select("<script>" +
            " select a.*,c.nickname as creator_name" +
            " from cc_app a,cc_app_user_rel b,cc_user c" +
            " where a.id=b.app_id" +
            " and a.creator = c.id" +
            " and b.user_id = #{userId}" +
            " <if test='appName!=null'> and a.app_name like CONCAT('%','#{appName}','%')</if>" +
            " and a.status=#{status}" +
            " and b.status=#{status}" +
            " and c.status=#{status}" +
            "</script>")
    List<CXAppInfoEntity> getMyAppsByUserId(@Param("appName") String appName,
                                    @Param("userId") int userId,
                                    @Param("status") byte status);


    @Select("<script>" +
            " select a.*,b.nickname as creator_name" +
            " from cc_app a,cc_user b" +
            " where a.creator = b.id" +
            " <if test='appName!=null'> and a.app_name like CONCAT('%','#{appName}','%')</if>" +
            " and a.status=#{status}" +
            "</script>")
    List<CXAppInfoEntity> getMyApps(@Param("appName") String appName,
                                    @Param("status") byte status);

    @Select("<script>" +
            " select count(1)" +
            " from cc_app a,cc_app_user_rel b" +
            " where a.id=b.app_id" +
            " and b.user_id = #{userId}" +
            " and a.status=#{status}" +
            " and b.status=#{status}" +
            "</script>")
    long getMyAppCountByUserId(@Param("userId") int userId,@Param("status") byte status);

    @Select("<script>" +
            " select a.* " +
            " from cc_app a,cc_user_ext_info b,cc_app_user_rel c" +
            " where a.id = c.app_id " +
            " and b.user_id = c.user_id " +
            " and a.id = CAST(ext_info_value  as signed integer)" +
            " and b.ext_info_key = '" + ProjectConstants.USER_ACCESS_RECENT_PROJECT + "'" +
            " and a.status = " + ProjectConstants.STATUS_VALID +
            " and b.status = " + ProjectConstants.STATUS_VALID +
            " and c.status = " + ProjectConstants.STATUS_VALID +
            " and c.user_id = #{userId}" +
            " order by b.create_time" +
            "</script>")
    List<CcAppEntity> getUserRecentProject(@Param("userId")int userId);


}
