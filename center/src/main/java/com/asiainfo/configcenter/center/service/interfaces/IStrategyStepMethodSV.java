package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepMethodEntity;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyMethodReqVO;

/**
 * Created by oulc on 2018/8/13.
 */
public interface IStrategyStepMethodSV {
    /**
     * 创建更新策略（方法）
     * @author oulc
     * @date 2018/8/9 15:51
     * @param createStrategyMethodReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyMethod(CreateStrategyMethodReqVO createStrategyMethodReqVO, int userId);

    /**
     * 创建更新策略（方法）数据库信息
     * @author oulc
     * @date 2018/8/9 15:51
     * @param createStrategyMethodReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyMethodDataBaseInfo(CreateStrategyMethodReqVO createStrategyMethodReqVO,int userId);

    /**
     * 更新方法策略步骤
     * @author oulc
     * @date 2018/8/13 11:04
     * @param createStrategyMethodReqVO 方法策略步骤信息
     * @param userId 用户主键
     * @return 策略步骤主键
     */
    int updateStrategyMethodDataBaseInfo(CreateStrategyMethodReqVO createStrategyMethodReqVO, int userId);

    /**
     * 创建策略步骤实体(方法)
     * @author oulc
     * @date 2018/8/9 16:31
     * @param strategyStepId 策略步骤(strategy_step表)主键
     * @param clazz 类路径
     * @param methodName 方法名称
     * @param classInstance 类实例
     * @param paramType 参数类型
     * @param paramValue 参数值
     * @param desc 描述
     * @param creator 创建人
     * @return 策略步骤主键
     */
    int createStrategyStepMethodEntityAndSave(int strategyStepId,String clazz,String methodName,String classInstance,String paramType,String paramValue,String desc,int creator);

    /**
     * 查询方法策略步骤实体
     * @author oulc
     * @date 2018/8/13 6:54
     * @param strategyStepId 策略步骤主键
     * @return 策略步骤实体
     */
    CcStrategyStepMethodEntity getStrategyStepMethodByStrategyStepId(int strategyStepId);

    /**
     * 查询方法策略步骤实体(校验)
     * @author oulc
     * @date 2018/8/13 6:54
     * @param strategyStepId 策略步骤主键
     * @return 策略步骤实体
     */
    CcStrategyStepMethodEntity getStrategyStepMethodByStrategyStepIdCheck(int strategyStepId);

    /**
     * 保存实体
     * @author oulc
     * @date 2018/8/13 6:55
     * @param ccStrategyStepMethodEntity 实体
     * @return 实体
     */
    CcStrategyStepMethodEntity save(CcStrategyStepMethodEntity ccStrategyStepMethodEntity);

    /**
     * 删除方法策略步骤
     * @author oulc
     * @date 2018/8/13 7:53
     * @param strategyStepId  策略步骤主键
     * @param userId 用户主键
     */
    void deleteStrategyStepMethod(int strategyStepId,int userId);
}
