package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.complex.CXCcUserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Select("<script>" +
            " select a.id as user_id, a.username,a.nickname,a.email,c.role_name,a.status as user_status,a.create_time," +
            " (select b.ext_info_value from cc_user_ext_info b " +
            " where a.id = b.user_id and b.ext_info_key='"+ProjectConstants.USER_EXT_INFO_PHONE+"'" +
            " and b.status = "+ ProjectConstants.STATUS_VALID +") as phone" +
            " from cc_user a,cc_role c,cc_user_role_rel d" +
            " where a.id = d.user_id" +
            " and c.id = d.role_id" +
            " <if test='nickname!=null'> and a.nickname like CONCAT('%','#{nickname}','%') </if>" +
            " <if test='userStatus!=-1'> and a.status = #{userStatus} </if>" +
            /*" ORDER BY a.nickname ASC" +*/
            " limit #{start},#{size}"+
            "</script>")
    List<CXCcUserInfoEntity> findCcUserInfoComplexInfo(@Param("nickname") String nickname, @Param("userStatus") int userStatus, @Param("start") int start, @Param("size")int size);

    @Select("<script>" +
            " select count(1)" +
            " from cc_user a,cc_role c,cc_user_role_rel d" +
            " where a.id = d.user_id" +
            " and c.id = d.role_id" +
            " <if test='nickname!=null'> and a.nickname like CONCAT('%','#{nickname}','%') </if>" +
            " <if test='userStatus!=-1'> and a.status = #{userStatus} </if>" +
            "</script>")
    long findCcUserInfoComplexInfoCount(@Param("nickname") String nickname, @Param("userStatus") int userStatus);
}
