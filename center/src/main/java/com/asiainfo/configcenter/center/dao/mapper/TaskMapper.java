package com.asiainfo.configcenter.center.dao.mapper;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.vo.task.MyInReviewTaskVO;
import com.asiainfo.configcenter.center.vo.task.MyReviewedTaskVO;
import com.asiainfo.configcenter.center.vo.task.MySubmitTaskVO;
import com.asiainfo.configcenter.center.vo.task.TaskStepVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * 任务数据库访问接口(myBaties)
 * Created by bawy on 2018/8/4.
 */
@Repository
@Mapper
public interface TaskMapper {


    /**
     * 根据条件查询符合条件的任务
     * @author bawy
     * @date @date 2018/8/4 21:55
     * @param creator 任务创建人
     * @param taskType 任务类型
     * @param taskState 任务状态
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param start 开始下标
     * @param size 数量
     * @return 配置项集合
     */
    @Select("<script>" +
            " select a.id,a.task_name,b.app_name,c.env_name,a.description,a.task_type,a.task_state,a.create_time,a.update_time" +
            " from cc_task a,cc_app b,cc_app_env c" +
            " where a.app_env_id=c.id and c.app_id=b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and a.creator=#{creator}" +
            " and a.task_state!=" + ProjectConstants.TASK_STATE_TO_SUBMIT +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='taskState > 0'>and a.task_state=#{taskState}</if>" +
            " <if test='beginTime != null'>and a.create_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and a.create_time <![CDATA[<=]]> #{endTime}</if>" +
            " order by a.create_time desc" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "id", property = "taskId"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "app_name", property = "appName"),
            @Result(column = "env_name", property = "envName"),
            @Result(column = "task_state", property = "taskState"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<MySubmitTaskVO> getSubmitTasks(@Param("creator") int creator, @Param("taskType") byte taskType, @Param("taskState") byte taskState,
                                        @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime,
                                        @Param("start") int start, @Param("size") int size);

    /**
     * 根据条件获取符合条件的任务数量
     * @author bawy
     * @date 2018/8/4 21:55
     * @param creator 任务创建人
     * @param taskType 任务类型
     * @param taskState 任务状态
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 数量
     */
    @Select("<script>select count(1)" +
            " from cc_task a,cc_app b,cc_app_env c" +
            " where a.app_env_id=c.id and c.app_id=b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and c.status=" + ProjectConstants.STATUS_VALID +
            " and a.creator=#{creator}" +
            " and a.task_state!=" + ProjectConstants.TASK_STATE_TO_SUBMIT +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='taskState > 0'>and a.task_state=#{taskState}</if>" +
            " <if test='beginTime != null'>and a.update_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and a.update_time <![CDATA[<=]]> #{endTime}</if>" +
            "</script>")
    int getSubmitTasksCount(@Param("creator") int creator, @Param("taskType") byte taskType, @Param("taskState") byte taskState,
                            @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);


    /**
     * 根据条件查询符合条件的待审核任务
     * @author bawy
     * @date 2018/8/5 22:29
     * @param operator 操作员
     * @param taskType 任务类型
     * @param creatorName 任务创建人
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param start 开始下标
     * @param size 数量
     * @return 配置项集合
     */
    @Select("<script>" +
            " select a.id,a.app_env_id,b.step,a.task_name,c.app_name,d.env_name,e.nickname,a.description,a.task_type,a.task_state,b.create_time,b.update_time" +
            " from cc_task a,cc_task_oper_record b,cc_app c,cc_app_env d,cc_user e" +
            " where a.id=b.task_id and a.app_env_id=d.id and d.app_id=c.id and a.creator=e.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and b.operator=#{operator}" +
            " and a.task_state=" + ProjectConstants.TASK_STATE_IN_REVIEW +
            " and b.operation_state=" + ProjectConstants.OPERATE_STATE_IN_REVIEW +
            " <if test='taskId > 0'>and a.id like CONCAT('%','#{taskId}','%')</if>" +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='creatorName != null'>and e.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and b.create_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and b.create_time <![CDATA[<=]]> #{endTime}</if>" +
            " order by a.create_time desc" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "id", property = "taskId"),
            @Result(column = "app_env_id", property = "envId"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "app_name", property = "appName"),
            @Result(column = "env_name", property = "envName"),
            @Result(column = "nickname", property = "creatorName"),
            @Result(column = "task_state", property = "taskState"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<MyInReviewTaskVO> getReviewWaitingTasks(@Param("operator") int operator, @Param("taskId") int taskId, @Param("taskType") byte taskType, @Param("creatorName") String creatorName,
                                                 @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime,
                                                 @Param("start") int start, @Param("size") int size);

    /**
     * 根据条件获取符合条件的配置项数量
     * @author bawy
     * @date 2018/8/5 22:33
     * @param operator 操作员
     * @param taskType 任务类型
     * @param creatorName 创建人名称
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 数量
     */
    @Select("<script>select count(1)" +
            " from cc_task a,cc_task_oper_record b,cc_app c,cc_app_env d,cc_user e" +
            " where a.id=b.task_id and a.app_env_id=d.id and d.app_id=c.id and a.creator=e.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and b.operator=#{operator}" +
            " and a.task_state=" + ProjectConstants.TASK_STATE_IN_REVIEW +
            " and b.operation_state=" + ProjectConstants.OPERATE_STATE_IN_REVIEW +
            " <if test='taskId > 0'>and a.id like CONCAT('%','#{taskId}','%')</if>" +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='creatorName != null'>and e.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and b.create_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and b.create_time <![CDATA[<=]]> #{endTime}</if>" +
            "</script>")
    int getReviewWaitingTasksCount(@Param("operator") int operator, @Param("taskId") int taskId, @Param("taskType") byte taskType, @Param("creatorName") String creatorName,
                            @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);

    /**
     * 根据条件查询符合条件的已经审核过的任务
     * @author bawy
     * @date 2018/8/5 22:29
     * @param operator 操作员
     * @param taskType 任务类型
     * @param creatorName 任务创建人
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param start 开始下标
     * @param size 数量
     * @return 配置项集合
     */
    @Select("<script>" +
            " select a.id,b.step,a.task_name,c.app_name,d.env_name,e.nickname,a.description,a.task_type,a.task_state,b.create_time,b.update_time" +
            " from cc_task a,cc_task_oper_record b,cc_app c,cc_app_env d,cc_user e" +
            " where a.id=b.task_id and a.app_env_id=d.id and d.app_id=c.id and a.creator=e.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and b.operator=#{operator}" +
            " and b.operation_state in (" + ProjectConstants.OPERATE_STATE_REVIEW_PASS + "," + ProjectConstants.OPERATE_STATE_REVIEW_NOT_PASS + ")" +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='taskState > 0'>and a.task_state=#{taskState}</if>" +
            " <if test='creatorName != null'>and e.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and b.update_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and b.update_time <![CDATA[<=]]> #{endTime}</if>" +
            " order by a.update_time desc" +
            " limit #{start},#{size}"+
            "</script>")
    @Results({
            @Result(column = "id", property = "taskId"),
            @Result(column = "task_name", property = "taskName"),
            @Result(column = "app_name", property = "appName"),
            @Result(column = "env_name", property = "envName"),
            @Result(column = "nickname", property = "creatorName"),
            @Result(column = "task_type", property = "taskType"),
            @Result(column = "task_state", property = "taskState"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<MyReviewedTaskVO> getMyReviewedTasks(@Param("operator") int operator, @Param("taskType") byte taskType, @Param("taskState") byte taskState, @Param("creatorName") String creatorName,
                                              @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime,
                                              @Param("start") int start, @Param("size") int size);

    /**
     * 根据条件获取符合条件的已经审核过的任务数量
     * @author bawy
     * @date 2018/8/5 22:33
     * @param operator 操作员
     * @param taskType 任务类型
     * @param creatorName 创建人名称
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 数量
     */
    @Select("<script>select count(1)" +
            " from cc_task a,cc_task_oper_record b,cc_app c,cc_app_env d,cc_user e" +
            " where a.id=b.task_id and a.app_env_id=d.id and d.app_id=c.id and a.creator=e.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and b.status=" + ProjectConstants.STATUS_VALID +
            " and b.operator=#{operator}" +
            " and b.operation_state in (" + ProjectConstants.OPERATE_STATE_REVIEW_PASS + "," + ProjectConstants.OPERATE_STATE_REVIEW_NOT_PASS + ")" +
            " <if test='taskType > 0'>and a.task_type=#{taskType}</if>" +
            " <if test='taskState > 0'>and a.task_state=#{taskState}</if>" +
            " <if test='creatorName != null'>and e.nickname like CONCAT('%','#{creatorName}','%')</if>" +
            " <if test='beginTime != null'>and b.update_time <![CDATA[>=]]> #{beginTime}</if>" +
            " <if test='endTime != null'>and b.update_time <![CDATA[<=]]> #{endTime}</if>" +
            " order by a.update_time desc" +
            "</script>")
    int getMyReviewedTasksCount(@Param("operator") int operator, @Param("taskType") byte taskType, @Param("taskState") byte taskState, @Param("creatorName") String creatorName,
                                   @Param("beginTime") Timestamp beginTime, @Param("endTime") Timestamp endTime);

    @Select("<script>" +
            " select a.step,a.operation_state,a.reason,b.nickname,a.update_time" +
            " from cc_task_oper_record a,cc_user b" +
            " where a.operator=b.id" +
            " and a.status=" + ProjectConstants.STATUS_VALID +
            " and a.task_id=#{taskId}" +
            " order by a.step asc" +
            "</script>")
    @Results({
            @Result(column = "operation_state", property = "operationState"),
            @Result(column = "nickname", property = "operatorName"),
            @Result(column = "update_time", property = "updateTime"),
    })
    List<TaskStepVO> getTaskFlow(@Param("taskId") int taskId);


    /**
     * 根据条件查询符合条件的已经审核过的任务
     * @author bawy
     * @date 2018/8/5 22:29
     * @param userId 用户主键
     * @return 配置项集合`
     */
    @Select("<script>" +
            " select count(1)" +
            " from cc_task a,cc_task_oper_record b" +
            " where a.id = b. task_id" +
            " and b.step =(select  MAX(c.step) from cc_task_oper_record c where a.id = c.task_id) " +
            " and b.operation_state = #{operationState}" +
            " <if test='userId!=0'> and a.creator=#{userId} </if>" +
            " and a.status=1" +
            " and b.status=1" +
            "</script>")
    long getTaskByOperationState(@Param("userId") int userId,@Param("operationState") byte operationState);

    /**
     * 根据条件查询符合条件的已经审核过的任务
     * @author bawy
     * @date 2018/8/5 22:29
     * @param userId 用户主键
     * @return 配置项集合
     */
    @Select("<script>" +
            " select count(1)" +
            " from cc_task a,cc_task_oper_record b" +
            " where a.id = b. task_id" +
            " and b.operation_state = " + ProjectConstants.OPERATE_STATE_REVIEW_NOT_PASS +
            " <if test='userId!=0'> and a.creator=#{userId} </if>" +
            " and a.status=1" +
            " and b.status=1" +
            "</script>")
    long getRollbackTaskByUserId(@Param("userId") int userId);

}
