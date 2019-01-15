package com.asiainfo.configcenter.center.dao.repository;

import com.asiainfo.configcenter.center.entity.CcOperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by oulc on 2018/7/20.
 */
@Repository
public interface OperationLogRepository extends JpaRepository<CcOperationLogEntity, Integer>, JpaSpecificationExecutor<CcOperationLogEntity> {
    // operator  OperateType startTime endTime
    @Query(value = "select * from cc_operation_log a where a.operator=:operator and a.operation_type = :OperateType and a.create_time BETWEEN :startTime and :endTime ORDER BY a.create_time DESC limit :start,:size",nativeQuery = true)
    List<CcOperationLogEntity> findByOperatorAndTypeAndTime(@Param(value = "operator") int operator,
                                                            @Param(value = "OperateType") byte OperateType,
                                                            @Param(value = "startTime")Timestamp startTime,
                                                            @Param(value = "endTime")Timestamp endTime,
                                                            @Param(value = "start") int start,
                                                            @Param(value = "size") int size
    );
    @Query(value = "select count(*) from cc_operation_log a where a.operator=:operator and a.operation_type = :OperateType and a.create_time BETWEEN :startTime and :endTime",nativeQuery = true)
    long findCountByOperatorAndTypeAndTime(@Param(value = "operator") int operator,
                                           @Param(value = "OperateType") byte OperateType,
                                           @Param(value = "startTime")Timestamp startTime,
                                           @Param(value = "endTime")Timestamp endTime
    );


    // operator OperateType
    @Query(value = "select * from cc_operation_log a where a.operator=:operator and a.operation_type = :OperateType ORDER BY a.create_time DESC limit :start,:size",nativeQuery = true)
    List<CcOperationLogEntity> findByOperatorAndType(@Param(value = "operator") int operator,
                                                            @Param(value = "OperateType") byte OperateType,
                                                            @Param(value = "start") int start,
                                                            @Param(value = "size") int size
    );
    @Query(value = "select count(*) from cc_operation_log a where a.operator=:operator and a.operation_type = :OperateType",nativeQuery = true)
    long findCountByOperatorAndType(@Param(value = "operator") int operator,
                                    @Param(value = "OperateType") byte OperateType
    );


    // operator startTime endTime
    @Query(value = "select * from cc_operation_log a where a.operator=:operator and a.create_time BETWEEN :startTime and :endTime ORDER BY a.create_time DESC limit :start,:size",nativeQuery = true)
    List<CcOperationLogEntity> findByOperatorAndTime(@Param(value = "operator") int operator,
                                                            @Param(value = "startTime")Timestamp startTime,
                                                            @Param(value = "endTime")Timestamp endTime,
                                                            @Param(value = "start") int start,
                                                            @Param(value = "size") int size
    );
    @Query(value = "select count(*) from cc_operation_log a where a.operator=:operator and a.create_time BETWEEN :startTime and :endTime",nativeQuery = true)
    long findCountByOperatorAndTime(@Param(value = "operator") int operator,
                                           @Param(value = "startTime")Timestamp startTime,
                                           @Param(value = "endTime")Timestamp endTime
    );

    //operator
    @Query(value = "select * from cc_operation_log a where a.operator=:operator ORDER BY a.create_time DESC limit :start,:size",nativeQuery = true)
    List<CcOperationLogEntity> findByOperator(@Param(value = "operator") int operator,
                                                            @Param(value = "start") int start,
                                                            @Param(value = "size") int size
    );
    @Query(value = "select count(*) from cc_operation_log a where a.operator=:operator",nativeQuery = true)
    long findCountByOperator(@Param(value = "operator") int operator
    );
}
