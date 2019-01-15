package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.vo.task.TaskInsVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InstanceMapper {

    @Select("<script>" +
            " select count(1) " +
            " from cc_instance a,cc_app b,cc_app_env c" +
            " where a.env_id = c.id" +
            " and a.is_alive = " + ProjectConstants.STATUS_VALID +
            " and c.app_id = b.id" +
            " and a.status =" + ProjectConstants.STATUS_VALID +
            " and b.status =" + ProjectConstants.STATUS_VALID +
            " and c.status =" + ProjectConstants.STATUS_VALID +
            "</script>")
    long getInstanceCount();

    @Select("<script>" +
            " select count(1) " +
            " from cc_instance a,cc_app b,cc_app_env c,cc_app_user_rel d" +
            " where a.env_id = c.id" +
            " and a.is_alive = " + ProjectConstants.STATUS_VALID +
            " and c.app_id = b.id" +
            " and b.id = d.app_id" +
            " and d.user_id = #{userId}" +
            " and a.status =" + ProjectConstants.STATUS_VALID +
            " and b.status =" + ProjectConstants.STATUS_VALID +
            " and c.status =" + ProjectConstants.STATUS_VALID +
            " and d.status =" + ProjectConstants.STATUS_VALID +
            "</script>")
    long getInstanceCountByUserId(@Param("userId") int userId);

    @Select("<script>" +
            " select a.ins_name, a.ins_ip, a.is_alive, a.status, a.update_time" +
            " from cc_instance a" +
            " where a.id in " +
            " <foreach item='id' index='index' collection='idList' open='(' separator=',' close=')'>" +
            "   #{id} " +
            "</foreach> " +
            "</script>")
    List<TaskInsVO> getInsInfo(@Param("idList")List idList);
}
