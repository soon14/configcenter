package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcStrategyStepEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.center.vo.strategy.UpdateStrategyStepInfoReqVO;
import com.asiainfo.configcenter.center.vo.strategy.UpdateStrategyStepNumVO;

import java.util.List;

/**
 * Created by oulc on 2018/8/13.
 */
public interface IStrategyStepSV {
    /**
     * 查询策略步骤
     * @author oulc
     * @date 2018/8/13 11:09
     * @param id 策略步骤主键
     * @param StrategyId 策略主键
     * @return 策略步骤实体
     */
    CcStrategyStepEntity getStrategyStepByIdAndStrategyId(int id, int StrategyId);

    /**
     * 查询策略步骤（校验）
     * @author oulc
     * @date 2018/8/13 11:09
     * @param id 策略步骤主键
     * @param StrategyId 策略主键
     * @return 策略步骤实体
     */
    CcStrategyStepEntity getStrategyStepByIdAndStrategyIdCheck(int id,int StrategyId);

    /**
     * 创建策略步骤实体并保存
     * @author oulc
     * @date 2018/8/9 16:27
     * @param strategyId 策略主键
     * @param strategyType 策略类型
     * @param stepNum 步数
     * @param desc 描述
     * @param creator 创建人
     * @return 策略步骤主键
     */
    int createStrategyStepEntityAndSave(int strategyId,String strategyType,byte stepNum,String desc,int creator);

    /**
     * 保存实体
     * @author oulc
     * @date 2018/8/13 11:13
     * @param ccStrategyStepEntity 实体
     * @return 实体主键
     */
    int save(CcStrategyStepEntity ccStrategyStepEntity);

    /**
     * 更新策略步骤信息
     * @param strategyStepId 策略步骤主键
     * @param strategyId 策略主键
     * @param strategyType 策略类型
     * @param stepNum 策略步骤
     * @param desc 描述
     * @param creator 创建人
     * @return 主键
     */
    CcStrategyStepEntity updateStrategyStepEntityAndSave(int strategyStepId,int strategyId,String strategyType,byte stepNum,String desc,int creator);


    /**
     * 查询策略步骤 
     * @author oulc
     * @date 2018/8/13 11:25
     * @param strategyId 策略抓紧
     * @return 策略步骤主键
     */
    List<CcStrategyStepEntity> findStrategyStepByStrategyId(int strategyId);

    /**
     * 更新策略步骤的步数
     * @author oulc
     * @date 2018/8/13 6:49
     * @param updateStrategyStepNumVO 策略步骤信息
     * @param userId 用户主键
     */
    void updateStrategyStepNums(UpdateStrategyStepNumVO updateStrategyStepNumVO, int userId);

    /**
     * 更新策略步骤的步数
     * @author oulc
     * @date 2018/8/13 6:55
     * @param strategyStepInfoVO 策略步骤信息
     * @param strategyId 策略主键
     * @param userId 用户主键
     */
    void updateStrategyStepNum(UpdateStrategyStepInfoReqVO strategyStepInfoVO,int strategyId, int userId);

    /**
     * 删除策略步骤
     * @param strategyId
     * @param strategyStepId
     * @param appId
     * @param userId
     */
    void deleteStrategyStep(int strategyId , int strategyStepId , int appId,int userId);


}
