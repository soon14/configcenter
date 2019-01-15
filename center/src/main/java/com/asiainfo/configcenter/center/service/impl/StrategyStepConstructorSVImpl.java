package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.StrategyStepConstructorRepository;
import com.asiainfo.configcenter.center.entity.CcStrategyStepConstructorEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigUpdateStrategySV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepConstructorSV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyConstructorReqVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/8/13.
 * 构造器策略步骤业务层
 */
@Service
public class StrategyStepConstructorSVImpl implements IStrategyStepConstructorSV{

    @Resource
    private StrategyStepConstructorRepository strategyStepConstructorRepository;

    @Resource
    private IStrategyStepSV iStrategyStepSV;

    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Transactional
    @Override
    public int updateStrategyConstructorDataBaseInfo(CreateStrategyConstructorReqVO createStrategyConstructorReqVO, int userId) {
        int strategyStepId = createStrategyConstructorReqVO.getStrategyStepId();
        int strategyId = createStrategyConstructorReqVO.getStrategyId();

        //变更策略步骤数据
        iStrategyStepSV.updateStrategyStepEntityAndSave(strategyStepId,strategyId,null,createStrategyConstructorReqVO.getStepNum(),createStrategyConstructorReqVO.getDescription(),userId);

        CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = getStrategyStepConstructorByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepConstructorEntity.setClazz(createStrategyConstructorReqVO.getClazz());
        ccStrategyStepConstructorEntity.setParamsType(ccStrategyStepConstructorEntity.getParamsType());
        ccStrategyStepConstructorEntity.setParamsValue(ccStrategyStepConstructorEntity.getParamsValue());
        ccStrategyStepConstructorEntity.setDescription(ccStrategyStepConstructorEntity.getDescription());
        ccStrategyStepConstructorEntity.setModifier(userId);
        ccStrategyStepConstructorEntity.setUpdateTime(TimeUtil.currentTime());
        ccStrategyStepConstructorEntity.setDescription(createStrategyConstructorReqVO.getDescription());
        strategyStepConstructorRepository.save(ccStrategyStepConstructorEntity);

        return strategyStepId;
    }

    @Transactional
    @Override
    public int createStrategyConstructorDataBaseInfo(CreateStrategyConstructorReqVO createStrategyConstructorReqVO, int userId) {
        String desc = createStrategyConstructorReqVO.getDescription();

        //创建策略步骤表数据
        int strategyStepId = iStrategyStepSV.createStrategyStepEntityAndSave(
                createStrategyConstructorReqVO.getStrategyId(),
                ProjectConstants.STRATEGY_TYPE_CONSTRUCTOR,
                createStrategyConstructorReqVO.getStepNum(),
                desc,
                userId);
        //根据步骤具体类型存放到表中
        createStrategyConstructorEntityAndSave(
                strategyStepId,
                createStrategyConstructorReqVO.getClazz(),
                createStrategyConstructorReqVO.getParamsType(),
                createStrategyConstructorReqVO.getParamsValue(),
                desc,
                userId
        );
        return strategyStepId;
    }

    @Transactional
    @Override
    public int createStrategyConstructorEntityAndSave(int strategyStepId,String clazz,String paramsType,String paramsValue,String desc, int userId) {
        CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = new CcStrategyStepConstructorEntity();
        ccStrategyStepConstructorEntity.setStrategyStepId(strategyStepId);
        ccStrategyStepConstructorEntity.setClazz(clazz);
        ccStrategyStepConstructorEntity.setParamsType(paramsType);
        ccStrategyStepConstructorEntity.setParamsValue(paramsValue);
        ccStrategyStepConstructorEntity.setDescription(desc);
        ccStrategyStepConstructorEntity.setCreator(userId);
        ccStrategyStepConstructorEntity.setCreateTime(TimeUtil.currentTime());
        ccStrategyStepConstructorEntity.setModifier(userId);
        ccStrategyStepConstructorEntity.setUpdateTime(TimeUtil.currentTime());
        ccStrategyStepConstructorEntity.setStatus(ProjectConstants.STATUS_VALID);
        strategyStepConstructorRepository.save(ccStrategyStepConstructorEntity);
        return ccStrategyStepConstructorEntity.getId();
    }

    @Override
    public CcStrategyStepConstructorEntity getStrategyStepConstructorByStrategyStepId(int strategyStepId) {
        return strategyStepConstructorRepository.findByStrategyStepIdAndStatus(strategyStepId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcStrategyStepConstructorEntity getStrategyStepConstructorByStrategyStepIdCheck(int strategyStepId) {
        CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = getStrategyStepConstructorByStrategyStepId(strategyStepId);
        Assert4CC.notNull(ccStrategyStepConstructorEntity, ResultCodeEnum.STRATEGY_COMMON_ERROR,"策略步骤不存在(构造器)");
        return ccStrategyStepConstructorEntity;
    }

    @Transactional
    @Override
    public int createStrategyConstructor(CreateStrategyConstructorReqVO createStrategyConstructorReqVO, int userId) {
        createStrategyConstructorCheckParam(createStrategyConstructorReqVO);

        //查询策略是否存在
        iConfigUpdateStrategySV.getConfigUpdateStrategyByIdAndAppIdCheck(createStrategyConstructorReqVO.getStrategyId(),createStrategyConstructorReqVO.getAppId());

        //保存数据
        int strategyStepId = createStrategyConstructorReqVO.getStrategyStepId();
        if(strategyStepId != 0){
            //更新构造器策略步骤
            return updateStrategyConstructorDataBaseInfo(createStrategyConstructorReqVO,userId);
        }else{
            //创建构造器策略步骤
            return createStrategyConstructorDataBaseInfo(createStrategyConstructorReqVO,userId);
        }
    }

    @Transactional
    @Override
    public void deleteStrategyStepConstructor( int strategyStepId, int userId) {
        CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = getStrategyStepConstructorByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepConstructorEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccStrategyStepConstructorEntity.setModifier(userId);
        ccStrategyStepConstructorEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccStrategyStepConstructorEntity);
    }

    @Override
    public CcStrategyStepConstructorEntity save(CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity) {
        strategyStepConstructorRepository.save(ccStrategyStepConstructorEntity);
        return ccStrategyStepConstructorEntity;
    }

    //**********************************校验
    private void createStrategyConstructorCheckParam(CreateStrategyConstructorReqVO createStrategyConstructorReqVO){
        Assert4CC.isTrue(createStrategyConstructorReqVO.getAppId()!=0,"应用主键不能为空");
        Assert4CC.hasLength(createStrategyConstructorReqVO.getClazz(),"类路径不能为空");
        Assert4CC.isTrue(ValidateUtil.checkClzPath(createStrategyConstructorReqVO.getClazz()),"类路径不合法");
    }

}
