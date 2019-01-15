package com.asiainfo.configcenter.center.service.interfaces;


import com.asiainfo.configcenter.center.entity.CcStrategyStepConstructorEntity;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyConstructorReqVO;

/**
 * Created by oulc on 2018/8/13.
 */
public interface IStrategyStepConstructorSV {
    /**
     * 更新构造器策略步骤
     * @author oulc
     * @date 2018/8/13 10:53
     * @param createStrategyConstructorReqVO 策略步骤信息
     * @param userId 用户主键
     * @return 策略步骤主键
     */
    int updateStrategyConstructorDataBaseInfo(CreateStrategyConstructorReqVO createStrategyConstructorReqVO, int userId);

    /**
     * 创建策略步骤(构造器)数据库信息
     * @author oulc
     * @date 2018/8/9 16:51
     * @param createStrategyConstructorReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyConstructorDataBaseInfo(CreateStrategyConstructorReqVO createStrategyConstructorReqVO,int userId);

    /**
     * 创建策略步骤(构造器)数据库实体并保存
     * @author oulc
     * @date 2018/8/9 17:27
     * @param strategyStepId 策略步骤(strategy_step表)主键
     * @param clazz 类路径
     * @param paramsType 参数类型
     * @param paramsValue 参数值
     * @param desc 描述
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyConstructorEntityAndSave(int strategyStepId,String clazz,String paramsType,String paramsValue,String desc, int userId);

    /**
     * 查询构造器策略步骤实体
     * @author oulc
     * @date 2018/8/13 6:45
     * @param strategyStepId 策略步骤主键
     * @return 策略步骤实体
     */
    CcStrategyStepConstructorEntity getStrategyStepConstructorByStrategyStepId(int strategyStepId);

    /**
     * 查询构造器策略步骤实体（校验）
     * @author oulc
     * @date 2018/8/13 6:45
     * @param strategyStepId 策略步骤主键
     * @return 策略步骤实体
     */
    CcStrategyStepConstructorEntity getStrategyStepConstructorByStrategyStepIdCheck(int strategyStepId);

    /**
     * 创建策略步骤(构造器)
     * @author oulc
     * @date 2018/8/9 16:35
     * @param createStrategyConstructorReqVO 构造器信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyConstructor(CreateStrategyConstructorReqVO createStrategyConstructorReqVO,int userId);

    /**
     * 保存实体
     * @author oulc
     * @date 2018/8/13 6:51
     * @param ccStrategyStepConstructorEntity 实体
     */
    CcStrategyStepConstructorEntity save(CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity);

    /**
     * 删除构造器策略步骤
     * @param StrategyStepId 策略步骤主键
     * @param userId 用户主键
     */
    void deleteStrategyStepConstructor(int StrategyStepId,int userId);
}
