package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.vo.org.OrgUserRelVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织数据库访问接口（Mybaties）
 * Created by bawy on 2018/8/17 18:28.
 */
@Repository
@Mapper
public interface OrgMapper {


    @Select("<script>" +
            " select a.id,a.org_id,a.user_id,a.create_time,b.name,c.nickname,c.email,d.role_name" +
            " from cc_org_user_rel a,cc_org b,cc_user c,cc_role d" +
            " where a.org_id=b.id and a.user_id=c.id and a.role_id=d.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and d.status=" + ProjectConstants.STATUS_VALID +
            " and a.org_id=#{orgId}" +
            " <if test='nickname != null'>and c.nickname like CONCAT('%','#{nickname}','%')</if>" +
            " <if test='roleName != null'>and d.role_name like CONCAT('%','#{roleName}','%')</if>" +
            " order by a.create_time desc" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "org_id", property = "orgId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "name", property = "orgName"),
            @Result(column = "role_name", property = "roleName"),
            @Result(column = "create_time", property = "createTime"),
    })
    List<OrgUserRelVO> queryUsers(@Param("orgId") int orgId, @Param("nickname") String nickname, @Param("roleName") String roleName,
                                      @Param("start") int start, @Param("size") int size);


    @Select("<script>" +
            " select count(1)" +
            " from cc_org_user_rel a,cc_org b,cc_user c,cc_role d" +
            " where a.org_id=b.id and a.user_id=c.id and a.role_id=d.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and d.status=" + ProjectConstants.STATUS_VALID +
            " and a.org_id=#{orgId}" +
            " <if test='nickname != null'>and c.nickname like CONCAT('%','#{nickname}','%')</if>" +
            " <if test='roleName != null'>and d.role_name like CONCAT('%','#{roleName}','%')</if>" +
            "</script>")
    int queryUsersCount(@Param("orgId") int orgId, @Param("nickname") String nickname, @Param("roleName") String roleName);


}
