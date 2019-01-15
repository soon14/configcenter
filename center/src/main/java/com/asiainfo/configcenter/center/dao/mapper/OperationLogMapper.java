package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.entity.complex.CXCcOperationLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OperationLogMapper {
    @Select("<script>" +
            " select a.*,b.nickname" +
            " from cc_operation_log a,cc_user b" +
            " where a.operator = b.id" +
            " and a.operator=${operator}" +
            " <if test='operateType!=0'> and a.operation_type = #{operateType} </if>" +
            " <if test='startTime!=null and endTime!=null'> and a.create_time BETWEEN #{startTime} and #{endTime} </if>" +
            " ORDER BY a.create_time DESC" +
            " limit ${start},${size}" +
            "</script>")
    List<CXCcOperationLogEntity> findByOperatorAndTypeAndTime(@Param(value = "operator") int operator,
                                                              @Param(value = "operateType") byte operateType,
                                                              @Param(value = "startTime") String startTime,
                                                              @Param(value = "endTime")String endTime,
                                                              @Param(value = "start") int start,
                                                              @Param(value = "size") int size
    );

    @Select("<script>" +
            " select count(1)" +
            " from cc_operation_log a,cc_user b" +
            " where a.operator = b.id" +
            " and a.operator=${operator}" +
            " <if test='operateType!=0'> and a.operation_type = #{operateType} </if>" +
            " <if test='startTime!=null and endTime!=null'> and a.create_time BETWEEN #{startTime} and #{endTime} </if>" +
            "</script>")
    long findCountByOperatorAndTypeAndTime(@Param(value = "operator") int operator,
                                           @Param(value = "operateType") byte operateType,
                                           @Param(value = "startTime") String startTime,
                                           @Param(value = "endTime")String endTime
    );
}
