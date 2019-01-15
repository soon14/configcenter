package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.StrategyStepMethodRepository;
import com.asiainfo.configcenter.center.entity.CcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepMethodEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigUpdateStrategySV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepMethodSV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyMethodReqVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/8/13.
 */
@Service
public class StrategyStepMethodSVImpl implements IStrategyStepMethodSV {

    @Resource
    private StrategyStepMethodRepository strategyStepMethodRepository;

    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Resource
    private IStrategyStepSV iStrategyStepSV;

    @Transactional
    @Override
    public int createStrategyMethod(CreateStrategyMethodReqVO createStrategyMethodReqVO, int userId) {
        //基本校验
        createStrategyMethodCheckParam(createStrategyMethodReqVO);

        iConfigUpdateStrategySV.getConfigUpdateStrategyByIdAndAppIdCheck(createStrategyMethodReqVO.getStrategyId(),createStrategyMethodReqVO.getAppId());

        int strategyStepId = createStrategyMethodReqVO.getStrategyStepId();
        if(strategyStepId !=0){
            return updateStrategyMethodDataBaseInfo(createStrategyMethodReqVO,userId);
        }else{
            return createStrategyMethodDataBaseInfo(createStrategyMethodReqVO,userId);
        }
    }

    @Transactional
    @Override
    public int updateStrategyMethodDataBaseInfo(CreateStrategyMethodReqVO createStrategyMethodReqVO, int userId) {
        int strategyId = createStrategyMethodReqVO.getStrategyId();
        int strategyStepId = createStrategyMethodReqVO.getStrategyStepId();

        iStrategyStepSV.updateStrategyStepEntityAndSave(strategyStepId,strategyId,null,createStrategyMethodReqVO.getStepNum(),createStrategyMethodReqVO.getDescription(),userId);

        CcStrategyStepMethodEntity ccStrategyStepMethodEntity = getStrategyStepMethodByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepMethodEntity.setClazz(createStrategyMethodReqVO.getClazz());
        ccStrategyStepMethodEntity.setMethodName(createStrategyMethodReqVO.getMethodName());
        ccStrategyStepMethodEntity.setClassInstance(createStrategyMethodReqVO.getClassInstance());
        ccStrategyStepMethodEntity.setParamsType(createStrategyMethodReqVO.getParamsType());
        ccStrategyStepMethodEntity.setParamsValue(createStrategyMethodReqVO.getParamsValue());
        ccStrategyStepMethodEntity.setDescription(ccStrategyStepMethodEntity.getDescription());
        save(ccStrategyStepMethodEntity);

        return strategyStepId;
    }

    @Transactional
    @Override
    public int createStrategyMethodDataBaseInfo(CreateStrategyMethodReqVO createStrategyMethodReqVO, int userId) {
        int strategyStepId = iStrategyStepSV.createStrategyStepEntityAndSave(
                createStrategyMethodReqVO.getStrategyId(),
                ProjectConstants.STRATEGY_TYPE_METHOD,
                createStrategyMethodReqVO.getStepNum(),
                createStrategyMethodReqVO.getDescription(),
                userId);

        createStrategyStepMethodEntityAndSave(
                strategyStepId,
                createStrategyMethodReqVO.getClazz(),
                createStrategyMethodReqVO.getMethodName(),
                createStrategyMethodReqVO.getClassInstance(),
                createStrategyMethodReqVO.getParamsType(),
                createStrategyMethodReqVO.getParamsValue(),
                createStrategyMethodReqVO.getDescription(),
                userId
        );

        return strategyStepId;
    }

    @Transactional
    @Override
    public int createStrategyStepMethodEntityAndSave(int strategyStepId,String clazz,String methodName,String classInstance,String paramType,String paramValue,String desc,int creator) {
        CcStrategyStepMethodEntity ccStrategyStepMethodEntity = new CcStrategyStepMethodEntity();
        ccStrategyStepMethodEntity.setStrategyStepId(strategyStepId);
        ccStrategyStepMethodEntity.setClazz(clazz);
        ccStrategyStepMethodEntity.setMethodName(methodName);
        ccStrategyStepMethodEntity.setClassInstance(classInstance);
        ccStrategyStepMethodEntity.setParamsType(paramType);
        ccStrategyStepMethodEntity.setParamsValue(paramValue);
        ccStrategyStepMethodEntity.setDescription(desc);
        ccStrategyStepMethodEntity.setCreator(creator);
        ccStrategyStepMethodEntity.setCreateTime(TimeUtil.currentTime());
        ccStrategyStepMethodEntity.setModifier(creator);
        ccStrategyStepMethodEntity.setUpdateTime(TimeUtil.currentTime());
        ccStrategyStepMethodEntity.setStatus(ProjectConstants.STATUS_VALID);
        strategyStepMethodRepository.save(ccStrategyStepMethodEntity);
        return ccStrategyStepMethodEntity.getId();
    }

    @Override
    public CcStrategyStepMethodEntity getStrategyStepMethodByStrategyStepId(int strategyStepId) {
        return strategyStepMethodRepository.findByStrategyStepIdAndStatus(strategyStepId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcStrategyStepMethodEntity getStrategyStepMethodByStrategyStepIdCheck(int strategyStepId) {
        CcStrategyStepMethodEntity ccStrategyStepMethodEntity = getStrategyStepMethodByStrategyStepId(strategyStepId);
        Assert4CC.notNull(ccStrategyStepMethodEntity,ResultCodeEnum.STRATEGY_COMMON_ERROR,"策略步骤不存在(方法)");
        return ccStrategyStepMethodEntity;
    }

    @Override
    public CcStrategyStepMethodEntity save(CcStrategyStepMethodEntity ccStrategyStepMethodEntity) {
        strategyStepMethodRepository.save(ccStrategyStepMethodEntity);
        return ccStrategyStepMethodEntity;
    }

    //*******************************************校验
    private void createStrategyMethodCheckParam(CreateStrategyMethodReqVO createStrategyMethodReqVO){
        Assert4CC.isTrue(createStrategyMethodReqVO.getAppId() != 0,"应用主键不能为空");
        Assert4CC.isTrue(createStrategyMethodReqVO.getStrategyId() != 0,"策略主键不能为空");
        Assert4CC.hasLength( createStrategyMethodReqVO.getClazz(),"类路径不能为空");
        Assert4CC.isTrue(ValidateUtil.checkClzPath(createStrategyMethodReqVO.getClazz()),"类路径不合法");
        Assert4CC.hasLength(createStrategyMethodReqVO.getMethodName(),"方法名称不能为空");
    }

    @Override
    public void deleteStrategyStepMethod(int strategyStepId, int userId) {
        CcStrategyStepMethodEntity ccStrategyStepMethodEntity = getStrategyStepMethodByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepMethodEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccStrategyStepMethodEntity.setModifier(userId);
        ccStrategyStepMethodEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccStrategyStepMethodEntity);
    }
}
