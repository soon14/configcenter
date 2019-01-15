package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.StrategyStepFieldRepository;
import com.asiainfo.configcenter.center.dao.repository.StrategyStepRepository;
import com.asiainfo.configcenter.center.entity.CcStrategyStepConstructorEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.center.entity.CcStrategyStepMethodEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.vo.strategy.UpdateStrategyStepInfoReqVO;
import com.asiainfo.configcenter.center.vo.strategy.UpdateStrategyStepNumVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oulc on 2018/8/13.
 * 策略步骤业务层
 */
@Service
public class StrategyStepSVImpl implements IStrategyStepSV{
    @Resource
    private StrategyStepRepository strategyStepRepository;

    @Resource
    private StrategyStepFieldRepository strategyStepFieldRepository;

    @Resource
    private IStrategyStepFieldSV iStrategyStepFieldSV;

    @Resource
    private IStrategyStepConstructorSV iStrategyStepConstructorSV;

    @Resource
    private IStrategyStepMethodSV iStrategyStepMethodSV;

    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Override
    public CcStrategyStepEntity getStrategyStepByIdAndStrategyId(int id, int StrategyId) {
        return strategyStepRepository.findByIdAndStrategyIdAndStatus(id,StrategyId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcStrategyStepEntity getStrategyStepByIdAndStrategyIdCheck(int id,int StrategyId) {
        CcStrategyStepEntity ccStrategyStepEntity = getStrategyStepByIdAndStrategyId(id,StrategyId);
        Assert4CC.notNull(ccStrategyStepEntity, ResultCodeEnum.STRATEGY_COMMON_ERROR,"策略步骤不存在:"+id);
        return ccStrategyStepEntity;
    }

    @Transactional
    @Override
    public int createStrategyStepEntityAndSave(int strategyId, String strategyType, byte stepNum, String desc, int creator) {
        CcStrategyStepEntity ccStrategyStepEntity = new CcStrategyStepEntity();
        ccStrategyStepEntity.setStrategyId(strategyId);
        ccStrategyStepEntity.setStepType(strategyType);
        ccStrategyStepEntity.setStepNumber(stepNum);
        ccStrategyStepEntity.setDescription(desc);
        ccStrategyStepEntity.setCreator(creator);
        ccStrategyStepEntity.setCreateTime(TimeUtil.currentTime());
        ccStrategyStepEntity.setModifier(creator);
        ccStrategyStepEntity.setUpdateTime(TimeUtil.currentTime());
        ccStrategyStepEntity.setStatus(ProjectConstants.STATUS_VALID);
        save(ccStrategyStepEntity);
        return ccStrategyStepEntity.getId();
    }

    @Transactional
    @Override
    public CcStrategyStepEntity updateStrategyStepEntityAndSave(int strategyStepId, int strategyId, String strategyType, byte stepNum, String desc, int creator) {
        CcStrategyStepEntity ccStrategyStepEntity = getStrategyStepByIdAndStrategyIdCheck(strategyStepId,strategyId);
        if(StringUtils.isNotBlank(strategyType)){
            ccStrategyStepEntity.setStepType(strategyType);
        }
        if(stepNum != 0){
            ccStrategyStepEntity.setStepNumber(stepNum);
        }
        if(StringUtils.isNotBlank(desc)){
            ccStrategyStepEntity.setDescription(desc);
        }
        ccStrategyStepEntity.setCreator(creator);
        ccStrategyStepEntity.setModifier(creator);
        ccStrategyStepEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccStrategyStepEntity);
        return ccStrategyStepEntity;
    }


    @Transactional
    @Override
    public int save(CcStrategyStepEntity ccStrategyStepEntity) {
        strategyStepRepository.save(ccStrategyStepEntity);
        return ccStrategyStepEntity.getId();
    }

    @Override
    public List<CcStrategyStepEntity> findStrategyStepByStrategyId(int strategyId) {
        return strategyStepRepository.findByStrategyIdAndStatusOrderByStepNumber(strategyId,ProjectConstants.STATUS_VALID);
    }

    @Transactional
    @Override
    public void updateStrategyStepNum(UpdateStrategyStepInfoReqVO strategyStepInfoVO, int strategyId, int userId) {
        CcStrategyStepEntity ccStrategyStepEntity = updateStrategyStepEntityAndSave(strategyStepInfoVO.getStrategyStepId(),strategyId,null,strategyStepInfoVO.getStepNum(),null,userId);

        String stepType = ccStrategyStepEntity.getStepType();
        if(ProjectConstants.STRATEGY_TYPE_FIELD.equals(stepType)){
            CcStrategyStepFieldEntity ccStrategyStepFieldEntity = iStrategyStepFieldSV.getStrategyStepFieldByStrategyStepIdCheck(ccStrategyStepEntity.getId());
            ccStrategyStepFieldEntity.setFieldValue(strategyStepInfoVO.getParamValue());
            ccStrategyStepFieldEntity.setUpdateTime(TimeUtil.currentTime());
            ccStrategyStepEntity.setModifier(userId);
            strategyStepFieldRepository.save(ccStrategyStepFieldEntity);
        }else if(ProjectConstants.STRATEGY_TYPE_CONSTRUCTOR.equals(stepType)){
            CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = iStrategyStepConstructorSV.getStrategyStepConstructorByStrategyStepIdCheck(ccStrategyStepEntity.getId());
            ccStrategyStepConstructorEntity.setParamsValue(strategyStepInfoVO.getParamValue());
            ccStrategyStepConstructorEntity.setUpdateTime(TimeUtil.currentTime());
            ccStrategyStepConstructorEntity.setModifier(userId);
            iStrategyStepConstructorSV.save(ccStrategyStepConstructorEntity);
        }else if(ProjectConstants.STRATEGY_TYPE_METHOD.equals(stepType)){
            CcStrategyStepMethodEntity ccStrategyStepMethodEntity = iStrategyStepMethodSV.getStrategyStepMethodByStrategyStepIdCheck(ccStrategyStepEntity.getId());
            ccStrategyStepMethodEntity.setParamsValue(strategyStepInfoVO.getParamValue());
            ccStrategyStepMethodEntity.setUpdateTime(TimeUtil.currentTime());
            ccStrategyStepMethodEntity.setModifier(userId);
            iStrategyStepMethodSV.save(ccStrategyStepMethodEntity);
        }
    }

    @Transactional
    @Override
    public void updateStrategyStepNums(UpdateStrategyStepNumVO updateStrategyStepNumVO, int userId) {
        //基本校验
        updateStrategyStepNumCheckParam(updateStrategyStepNumVO);

        iConfigUpdateStrategySV.getConfigUpdateStrategyByIdAndAppIdCheck(updateStrategyStepNumVO.getStrategyId(),updateStrategyStepNumVO.getAppId());
        UpdateStrategyStepInfoReqVO[] strategyStepInfoVOS =  updateStrategyStepNumVO.getStrategyStepInfoVOS();
        for(UpdateStrategyStepInfoReqVO strategyStepInfoVO : strategyStepInfoVOS){
            updateStrategyStepNum(strategyStepInfoVO ,updateStrategyStepNumVO.getStrategyId() ,userId);
        }
    }

    @Transactional
    @Override
    public void deleteStrategyStep(int strategyId, int strategyStepId, int appId, int userId) {
        deleteStrategyStepCheckParam(strategyId, strategyStepId, appId);
        //查询策略是否合法
        iConfigUpdateStrategySV.getConfigUpdateStrategyByIdAndAppIdCheck(strategyId,appId);
        //删除策略步骤
        CcStrategyStepEntity ccStrategyStepEntity = getStrategyStepByIdAndStrategyIdCheck(strategyStepId,strategyId);
        ccStrategyStepEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccStrategyStepEntity.setModifier(userId);
        ccStrategyStepEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccStrategyStepEntity);

        //根据策略类型删除策略步骤详细信息
        String stepType = ccStrategyStepEntity.getStepType();
        if(ProjectConstants.STRATEGY_TYPE_FIELD.equals(stepType)){
            iStrategyStepFieldSV.deleteStrategyStepField(strategyStepId,userId);
        }else if(ProjectConstants.STRATEGY_TYPE_CONSTRUCTOR.equals(stepType)){
            iStrategyStepConstructorSV.deleteStrategyStepConstructor(strategyStepId,userId);
        }else if(ProjectConstants.STRATEGY_TYPE_METHOD.equals(stepType)){
            iStrategyStepMethodSV.deleteStrategyStepMethod(strategyStepId,userId);
        }
    }

    //*******************************校验
    private void updateStrategyStepNumCheckParam(UpdateStrategyStepNumVO updateStrategyStepNumVO){
        Assert4CC.isTrue(updateStrategyStepNumVO.getAppId()!=0,"应用主键不能为空");
        Assert4CC.isTrue(updateStrategyStepNumVO.getStrategyId()!=0,"策略主键不能为空");
        UpdateStrategyStepInfoReqVO[] strategyStepInfoVOS = updateStrategyStepNumVO.getStrategyStepInfoVOS();
        Assert4CC.isTrue(strategyStepInfoVOS != null && strategyStepInfoVOS.length > 0,"策略信息不能为空");
        for(UpdateStrategyStepInfoReqVO strategyStepInfoVO : strategyStepInfoVOS){
            Assert4CC.isTrue(strategyStepInfoVO.getStrategyStepId() != 0,"策略主键不能为空");
            Assert4CC.isTrue(strategyStepInfoVO.getStepNum() != 0,"策略步数不能为空");
        }
    }

    private void deleteStrategyStepCheckParam(int strategyId, int strategyStepId, int appId){
        Assert4CC.isTrue(strategyId !=0,"策略主键不能为空");
        Assert4CC.isTrue(strategyStepId != 0,"策略步骤主键不能为空");
        Assert.isTrue(appId != 0,"应用主键不能为空");
    }

}
