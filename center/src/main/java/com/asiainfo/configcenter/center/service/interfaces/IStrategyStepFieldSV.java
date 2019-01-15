package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyFieldReqVO;
import com.asiainfo.configcenter.center.vo.strategy.UpdateStrategyStepInfoReqVO;

/**
 * Created by oulc on 2018/8/13.
 */
public interface IStrategyStepFieldSV {
    /**
     * 更新类变量策略步骤
     * @author oulc
     * @date 2018/8/13 10:54
     * @param createFieldStrategyReqVO 类变量策略步骤信息
     * @param userId 用户主键
     * @return 策略步骤主键
     */
    int updateStrategyFieldDataBaseInfo(CreateStrategyFieldReqVO createFieldStrategyReqVO, int userId);

    /**
     * 查询类变量策略步骤
     * @author oulc
     * @date 2018/8/13 6:28
     * @param strategyStepId 策略步骤主键
     * @return 类变量策略步骤尸体
     */
    CcStrategyStepFieldEntity getStrategyStepFieldByStrategyStepId(int strategyStepId);

    /**
     * 查询类变量策略步骤(校验)
     * @author oulc
     * @date 2018/8/13 6:28
     * @param strategyStepId 策略步骤主键
     * @return 类变量策略步骤尸体
     */
    CcStrategyStepFieldEntity getStrategyStepFieldByStrategyStepIdCheck(int strategyStepId);

    /**
     * 创建策略步骤实体(类变量)
     * @author oulc
     * @date 2018/8/9 16:30
     * @param strategyStepId 策略步骤(strategy_step表)主键
     * @param clazz 类路径
     * @param fieldName 变量名称
     * @param classInstance 类实例
     * @param fieldValue 变量值
     * @param dataType 变量类型
     * @param desc 描述信息
     * @param creator 创建人
     * @return 策略步骤主键
     */
    int createStrategyStepFieldEntityAndSave(int strategyStepId,String clazz,String fieldName,String classInstance,String fieldValue,String dataType,String desc,int creator);

    /**
     * 创建更新策略（类变量）
     * @author oulc
     * @date 2018/8/9 14:17
     * @param createFieldStrategyReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyField(CreateStrategyFieldReqVO createFieldStrategyReqVO , int userId);

    /**
     * 创建更新策略(类变量)数据库信息
     * @author oulc
     * @date 2018/8/9 15:47
     * @param createFieldStrategyReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createStrategyFieldDataBaseInfo(CreateStrategyFieldReqVO createFieldStrategyReqVO , int userId);

    /**
     * 删除类变量策略步骤
     * @author oulc
     * @date 2018/8/13 7:30
     * @param strategyStepId 策略步骤实体
     * @param userId 用户主键
     */
    void deleteStrategyStepField(int strategyStepId,int userId);

    /**
     * 保存实体
     * @author oulc
     * @date 2018/8/13 7:31
     * @param ccStrategyStepFieldEntity 实体
     */
    CcStrategyStepFieldEntity save(CcStrategyStepFieldEntity ccStrategyStepFieldEntity);

}
