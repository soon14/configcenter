package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.vo.app.AppUserRelVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用用户关系数据库访问接口（mybaties）
 * Created by bawy on 2018/7/30 11:09.
 */
@Repository
@Mapper
public interface AppUserRelMapper {

    /**
     * 根据条件查询对应应用用户关系数据
     * @author bawy
     * @date 2018/7/30 14:37
     * @param appId 应用标识
     * @param nickname 昵称
     * @param roleId 角色标识
     * @return 列表
     */
    @Select("<script>" +
            " select a.id,a.app_id,a.user_id,a.role_id,a.create_time,a.update_time,b.nickname,c.role_name" +
            " from cc_app_user_rel a,cc_user b,cc_role c" +
            " where a.user_id = b.id" +
            " and a.role_id = c.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and a.app_id=#{appId}" +
            " <if test='nickname != null'>and b.nickname like CONCAT('%','#{nickname}','%')</if>" +
            " <if test='roleId != 0'>and c.id = #{roleId}</if>" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "app_id", property = "appId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "role_id", property = "roleId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "role_name", property = "roleName")
    })
    List<AppUserRelVO> getAppUsers(@Param("appId") int appId, @Param("nickname") String nickname, @Param("roleId") int roleId, @Param("start") int start, @Param("size") int size);

    /**
     * 根据条件查询对应应用用户关系数据的数量
     * @author bawy
     * @date 2018/7/30 14:37
     * @param appId 应用标识
     * @param nickname 昵称
     * @param roleId 角色标识
     * @return 数量
     */
    @Select("<script>select count(1)" +
            " from cc_app_user_rel a,cc_user b,cc_role c" +
            " where a.user_id = b.id" +
            " and a.role_id = c.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and a.app_id=#{appId}" +
            " <if test='nickname != null'>and b.nickname like CONCAT('%','#{nickname}','%')</if>" +
            " <if test='roleId != 0'>and c.id = #{roleId}</if>" +
            "</script>")
    int getAppUsersCount(@Param("appId") int appId, @Param("nickname") String nickname, @Param("roleId") int roleId);

}
