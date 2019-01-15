package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.StrategyStepFieldRepository;
import com.asiainfo.configcenter.center.entity.CcStrategyStepFieldEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigUpdateStrategySV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepFieldSV;
import com.asiainfo.configcenter.center.service.interfaces.IStrategyStepSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.strategy.CreateStrategyFieldReqVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/8/13.
 */
@Service
public class StrategyStepFieldSVImpl implements IStrategyStepFieldSV{

    @Resource
    private IStrategyStepSV iStrategyStepSV;

    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Resource
    private StrategyStepFieldRepository strategyStepFieldRepository;

    @Transactional
    @Override
    public int updateStrategyFieldDataBaseInfo(CreateStrategyFieldReqVO createFieldStrategyReqVO, int userId) {
        int strategyId = createFieldStrategyReqVO.getStrategyId();
        int strategyStepId = createFieldStrategyReqVO.getStrategyStepId();
        byte stepNum = createFieldStrategyReqVO.getStepNum();
        String desc = createFieldStrategyReqVO.getDescription();

        iStrategyStepSV.updateStrategyStepEntityAndSave(strategyStepId,strategyId,null,stepNum,desc,userId);

        CcStrategyStepFieldEntity ccStrategyStepFieldEntity = getStrategyStepFieldByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepFieldEntity.setClazz(createFieldStrategyReqVO.getClazz());
        ccStrategyStepFieldEntity.setFieldName(createFieldStrategyReqVO.getFieldName());
        ccStrategyStepFieldEntity.setClassInstance(createFieldStrategyReqVO.getClassInstance());
        ccStrategyStepFieldEntity.setFieldValue(createFieldStrategyReqVO.getFieldValue());
        ccStrategyStepFieldEntity.setDataType(createFieldStrategyReqVO.getDataType());
        ccStrategyStepFieldEntity.setDescription(createFieldStrategyReqVO.getDescription());
        ccStrategyStepFieldEntity.setModifier(userId);
        ccStrategyStepFieldEntity.setUpdateTime(TimeUtil.currentTime());
        strategyStepFieldRepository.save(ccStrategyStepFieldEntity);

        return strategyStepId;
    }

    @Override
    public CcStrategyStepFieldEntity getStrategyStepFieldByStrategyStepIdCheck(int strategyStepId) {
        CcStrategyStepFieldEntity ccStrategyStepFieldEntity = getStrategyStepFieldByStrategyStepId(strategyStepId);
        Assert4CC.notNull(ccStrategyStepFieldEntity, ResultCodeEnum.STRATEGY_COMMON_ERROR,"策略步骤不存在(类变量),strategyStepId:"+strategyStepId);
        return ccStrategyStepFieldEntity;
    }

    @Override
    public CcStrategyStepFieldEntity getStrategyStepFieldByStrategyStepId(int strategyStepId) {
        return strategyStepFieldRepository.findByStrategyStepIdAndStatus(strategyStepId, ProjectConstants.STATUS_VALID);
    }


    @Transactional
    @Override
    public int createStrategyStepFieldEntityAndSave(int strategyStepId,String clazz,String fieldName,String classInstance,String fieldValue,String dataType,String desc,int creator) {
        CcStrategyStepFieldEntity ccStrategyStepFieldEntity = new CcStrategyStepFieldEntity();
        ccStrategyStepFieldEntity.setStrategyStepId(strategyStepId);
        ccStrategyStepFieldEntity.setClazz(clazz);
        ccStrategyStepFieldEntity.setFieldName(fieldName);
        ccStrategyStepFieldEntity.setClassInstance(classInstance);
        ccStrategyStepFieldEntity.setFieldValue(fieldValue);
        ccStrategyStepFieldEntity.setDataType(dataType);
        ccStrategyStepFieldEntity.setDescription(desc);
        ccStrategyStepFieldEntity.setCreator(creator);
        ccStrategyStepFieldEntity.setCreateTime(TimeUtil.currentTime());
        ccStrategyStepFieldEntity.setModifier(creator);
        ccStrategyStepFieldEntity.setUpdateTime(TimeUtil.currentTime());
        ccStrategyStepFieldEntity.setStatus(ProjectConstants.STATUS_VALID);
        strategyStepFieldRepository.save(ccStrategyStepFieldEntity);
        return ccStrategyStepFieldEntity.getId();
    }

    @Transactional
    @Override
    public int createStrategyField(CreateStrategyFieldReqVO createFieldStrategyReqVO, int userId) {
        //基本校验
        createFieldStrategyCheckParam(createFieldStrategyReqVO);

        iConfigUpdateStrategySV.getConfigUpdateStrategyByIdAndAppIdCheck(createFieldStrategyReqVO.getStrategyId(),createFieldStrategyReqVO.getAppId());

        int strategyStepId = createFieldStrategyReqVO.getStrategyStepId();

        if(strategyStepId != 0){
            return updateStrategyFieldDataBaseInfo(createFieldStrategyReqVO,userId);
        }else{
            //创建表数据
            return createStrategyFieldDataBaseInfo(createFieldStrategyReqVO,userId);
        }
    }

    @Transactional
    @Override
    public int createStrategyFieldDataBaseInfo(CreateStrategyFieldReqVO createFieldStrategyReqVO, int userId) {
        String desc = createFieldStrategyReqVO.getDescription();
        int strategyStepId = iStrategyStepSV.createStrategyStepEntityAndSave(
                createFieldStrategyReqVO.getStrategyId(),
                ProjectConstants.STRATEGY_TYPE_FIELD,
                createFieldStrategyReqVO.getStepNum(),
                desc,
                userId);

        createStrategyStepFieldEntityAndSave(
                strategyStepId,
                createFieldStrategyReqVO.getClazz(),
                createFieldStrategyReqVO.getFieldName(),
                createFieldStrategyReqVO.getClassInstance(),
                createFieldStrategyReqVO.getFieldValue(),
                createFieldStrategyReqVO.getDataType(),
                createFieldStrategyReqVO.getDescription(),
                userId);
        return  strategyStepId;
    }

    @Transactional
    @Override
    public void deleteStrategyStepField(int strategyStepId, int userId) {
        CcStrategyStepFieldEntity ccStrategyStepFieldEntity = getStrategyStepFieldByStrategyStepIdCheck(strategyStepId);
        ccStrategyStepFieldEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccStrategyStepFieldEntity.setModifier(userId);
        ccStrategyStepFieldEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccStrategyStepFieldEntity);
    }

    @Transactional
    @Override
    public CcStrategyStepFieldEntity save(CcStrategyStepFieldEntity ccStrategyStepFieldEntity) {
        strategyStepFieldRepository.save(ccStrategyStepFieldEntity);
        return ccStrategyStepFieldEntity;
    }

    //***********************************校验
    private void createFieldStrategyCheckParam(CreateStrategyFieldReqVO createFieldStrategyReqVO){
        Assert4CC.isTrue( createFieldStrategyReqVO.getAppId() != 0,"应用主键不能为空");
        Assert4CC.isTrue(createFieldStrategyReqVO.getStrategyId() != 0,"策略主键不能为空");
        Assert4CC.hasLength( createFieldStrategyReqVO.getClazz(),"类路径不能为空");
        Assert4CC.isTrue(ValidateUtil.checkClzPath(createFieldStrategyReqVO.getClazz()),"类路径不合法");
        Assert4CC.hasLength( createFieldStrategyReqVO.getFieldName(),"变量名不能为空");
        Assert4CC.hasLength( createFieldStrategyReqVO.getDataType(),"变量类型不能为空");
        Assert4CC.isTrue(createFieldStrategyReqVO.getStepNum() != 0,"策略步数不能为空");

    }



}
