package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.ConfigUpdateStrategyMapper;
import com.asiainfo.configcenter.center.dao.repository.*;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.strategy.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oulc on 2018/8/9.
 * 配置刷新策略业务层代码
 */
@Service
public class ConfigUpdateStrategySVImpl implements IConfigUpdateStrategySV {
    @Resource
    private ConfigUpdateStrategyRepository configUpdateStrategyRepository;

    @Resource
    private IStrategyStepSV iStrategyStepSV;

    @Resource
    private IStrategyStepFieldSV iStrategyStepFieldSV;

    @Resource
    private IStrategyStepMethodSV iStrategyStepMethodSV;

    @Resource
    private IStrategyStepConstructorSV iStrategyStepConstructorSV;

    @Resource
    private ConfigUpdateStrategyMapper configUpdateStrategyMapper;

    @Resource
    private AppUserRelRepository appUserRelRepository;

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private IConfigItemSV iConfigItemSV;


    @Transactional
    @Override
    public int createConfigUpdateStrategy(CreateConfigUpdateStrategyReqVO createConfigUpdateStrategyReqVO,int userId) {
        //基本校验
        createConfigUpdateStrategyCheckParam(createConfigUpdateStrategyReqVO);
        //保存数据库
        int appId = createConfigUpdateStrategyReqVO.getAppId();
        String strategyName = createConfigUpdateStrategyReqVO.getStrategyName();
        String desc = createConfigUpdateStrategyReqVO.getDesc();

        int strategyId = createConfigUpdateStrategyReqVO.getStrategyId();
        if(strategyId != 0){
            return updateConfigUpdateStrategyDataBaseInfo(appId,strategyId,strategyName,desc,userId);
        }else{
            return createConfigUpdateStrategyDataBaseInfo(appId,strategyName,desc,userId);
        }
    }

    @Transactional
    @Override
    public int createConfigUpdateStrategyDataBaseInfo(int appId, String strategyName, String desc, int userId) {
        CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity = new CcConfigUpdateStrategyEntity();
        ccConfigUpdateStrategyEntity.setAppId(appId);
        ccConfigUpdateStrategyEntity.setStrategyName(strategyName);
        ccConfigUpdateStrategyEntity.setDescription(desc);
        ccConfigUpdateStrategyEntity.setCreator(userId);
        ccConfigUpdateStrategyEntity.setCreateTime(TimeUtil.currentTime());
        ccConfigUpdateStrategyEntity.setModifier(userId);
        ccConfigUpdateStrategyEntity.setUpdateTime(TimeUtil.currentTime());
        ccConfigUpdateStrategyEntity.setStatus(ProjectConstants.STATUS_VALID);
        configUpdateStrategyRepository.save(ccConfigUpdateStrategyEntity);
        return ccConfigUpdateStrategyEntity.getId();
    }

    @Override
    public CcConfigUpdateStrategyEntity getConfigUpdateStrategyByIdAndAppIdCheck(int id, int appId) {
        CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity = getConfigUpdateStrategyByIdAndAppId(id, appId);
        Assert4CC.notNull(ccConfigUpdateStrategyEntity, ResultCodeEnum.STRATEGY_COMMON_ERROR,"策略不存在");
        return ccConfigUpdateStrategyEntity;
    }

    @Transactional
    @Override
    public int updateConfigUpdateStrategyDataBaseInfo(int appId,int strategyId, String strategyName, String desc, int userId) {
        CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity = getConfigUpdateStrategyByIdAndAppIdCheck(strategyId,appId);
        ccConfigUpdateStrategyEntity.setStrategyName(strategyName);
        ccConfigUpdateStrategyEntity.setDescription(desc);
        ccConfigUpdateStrategyEntity.setModifier(userId);
        ccConfigUpdateStrategyEntity.setUpdateTime(TimeUtil.currentTime());
        configUpdateStrategyRepository.save(ccConfigUpdateStrategyEntity);
        return ccConfigUpdateStrategyEntity.getId();
    }

    @Override
    public PageResultContainer<CXCcConfigUpdateStrategyEntity> findConfigUpdateStrategy(PageRequestContainer<QueryUpdateStrategyReqVO> pageRequestContainer) {
        ValidateUtil.checkPageParam(pageRequestContainer);
        QueryUpdateStrategyReqVO queryUpdateStrategyReqVO = pageRequestContainer.getData();
        findConfigUpdateStrategyCheckParam(queryUpdateStrategyReqVO);
        PageResultContainer<CXCcConfigUpdateStrategyEntity> result = new PageResultContainer<>();

        int appId = queryUpdateStrategyReqVO.getAppId();
        String strategyName = queryUpdateStrategyReqVO.getStrategyName();
        String creatorName = queryUpdateStrategyReqVO.getCreatorName();
        String startDate = queryUpdateStrategyReqVO.getStartDate()==0 ?  null:TimeUtil.timeFormat(queryUpdateStrategyReqVO.getStartDate());
        String endData = queryUpdateStrategyReqVO.getEndDate() == 0 ? null :TimeUtil.timeFormat(queryUpdateStrategyReqVO.getEndDate());
        int size = pageRequestContainer.getPageSize();
        int start = pageRequestContainer.getCurrentPage()*size;

        List<CXCcConfigUpdateStrategyEntity> list = configUpdateStrategyMapper.findConfigUpdateStrategy(appId,creatorName,strategyName,startDate,endData,start,size);
        long count = configUpdateStrategyMapper.findConfigUpdateStrategyCount(appId,creatorName,strategyName,startDate,endData);
        result.setEntities(list);
        result.setCount(count);
        return result;
    }

    @Override
    public CcConfigUpdateStrategyEntity getConfigUpdateStrategyByIdAndAppId(int id, int appId) {
        return configUpdateStrategyRepository.findByIdAndAppIdAndStatus(id,appId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public ConfigUpdateStrategyVO getConfigUpdateStrategyAllById(int id,int appId) {
        ConfigUpdateStrategyVO configUpdateStrategyVO = new ConfigUpdateStrategyVO();
        CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity = getConfigUpdateStrategyByIdAndAppIdCheck(id, appId);
        Assert4CC.notNull(ccConfigUpdateStrategyEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "无法获取应用"+appId+"的策略"+id);
        configUpdateStrategyVO.setStrategyId(ccConfigUpdateStrategyEntity.getId());
        configUpdateStrategyVO.setStrategyName(ccConfigUpdateStrategyEntity.getStrategyName());
        configUpdateStrategyVO.setDescription(ccConfigUpdateStrategyEntity.getDescription());
        int strategyId = ccConfigUpdateStrategyEntity.getId();
        //步骤
        List<UpdateStrategyStepVO> updateStrategyStepVOList = new ArrayList<>();
        configUpdateStrategyVO.setStrategyStepVOS(updateStrategyStepVOList);
        //查询步骤表
        List<CcStrategyStepEntity> strategyStepList =  iStrategyStepSV.findStrategyStepByStrategyId(strategyId);
        if(strategyStepList != null && strategyStepList.size() > 0){
            for(CcStrategyStepEntity ccStrategyStepEntity : strategyStepList){
                int strategyStepId = ccStrategyStepEntity.getId();

                UpdateStrategyStepVO updateStrategyStepVO = new UpdateStrategyStepVO();
                updateStrategyStepVO.setStrategyStepId(strategyStepId);
                updateStrategyStepVO.setStepNum(ccStrategyStepEntity.getStepNumber());
                updateStrategyStepVO.setStrategyType(ccStrategyStepEntity.getStepType());
                updateStrategyStepVO.setDesc(ccStrategyStepEntity.getDescription());
                updateStrategyStepVOList.add(updateStrategyStepVO);

                //查询各个类型的数据
                if(ProjectConstants.STRATEGY_TYPE_FIELD.equals(ccStrategyStepEntity.getStepType())){
                    CcStrategyStepFieldEntity ccStrategyStepFieldEntity = iStrategyStepFieldSV.getStrategyStepFieldByStrategyStepIdCheck(strategyStepId);
                    updateStrategyStepVO.setCcStrategyStepFieldEntity(ccStrategyStepFieldEntity);
                }else  if(ProjectConstants.STRATEGY_TYPE_METHOD.equals(ccStrategyStepEntity.getStepType())){
                    CcStrategyStepMethodEntity ccStrategyStepMethodEntity = iStrategyStepMethodSV.getStrategyStepMethodByStrategyStepIdCheck(strategyStepId);
                    updateStrategyStepVO.setCcStrategyStepMethodEntity(ccStrategyStepMethodEntity);
                }else if(ProjectConstants.STRATEGY_TYPE_CONSTRUCTOR.equals(ccStrategyStepEntity.getStepType())){
                    CcStrategyStepConstructorEntity ccStrategyStepConstructorEntity = iStrategyStepConstructorSV.getStrategyStepConstructorByStrategyStepIdCheck(strategyStepId);
                    updateStrategyStepVO.setCcStrategyStepConstructorEntity(ccStrategyStepConstructorEntity);
                }
            }
        }
        return configUpdateStrategyVO;
    }

    @Transactional
    @Override
    public void deleteConfigUpdateStrategy(DeleteConfigUpdateStrategyReqVO deleteConfigUpdateStrategyReqVO, int userId) {
        deleteConfigUpdateStrategyCheckParam(deleteConfigUpdateStrategyReqVO);
        //教研策略没有被配置使用
        int strategyId = deleteConfigUpdateStrategyReqVO.getStrategyId();

        String [] configFileNames = iConfigFileSV.getConfigFileNamesByStrategyId(strategyId);
        Assert4CC.isTrue( configFileNames == null || configFileNames.length == 0 , "删除失败，存在配置文件使用此刷新策略。配置文件名称：" + Arrays.toString(configFileNames));

        String [] configItemNames = iConfigItemSV.getConfigItemNamesByStrategyId(strategyId);
        Assert4CC.isTrue( configItemNames == null ||configItemNames.length == 0 ,"删除失败，存在配置项使用此刷新策略。配置项 key：" + Arrays.toString(configItemNames));

        deleteConfigUpdateStrategy(deleteConfigUpdateStrategyReqVO.getStrategyId(),deleteConfigUpdateStrategyReqVO.getAppId(),userId);
    }

    @Override
    public String getStrategyName(QueryStrategyAllReqVo queryStrategyAllReq) {
        int appId = queryStrategyAllReq.getAppId();
        int strategyId = queryStrategyAllReq.getStrategyId();
        Assert4CC.isTrue(appId>0, "应用标识不可为空");
        Assert4CC.isTrue(strategyId>0, "策略标识不可为空");
        CcConfigUpdateStrategyEntity strategyEntity = configUpdateStrategyRepository.findByIdAndAppIdAndStatus(strategyId, appId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(strategyEntity, ResultCodeEnum.DATA_ERROR, "无法获取刷新策略"+strategyId+"的名称");
        return strategyEntity.getStrategyName();
    }

    @Transactional
    @Override
    public CcConfigUpdateStrategyEntity save(CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity) {
        configUpdateStrategyRepository.save(ccConfigUpdateStrategyEntity);
        return ccConfigUpdateStrategyEntity;
    }

    @Transactional
    @Override
    public void deleteConfigUpdateStrategy(int strategyId, int appId, int userId) {
        CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity = getConfigUpdateStrategyByIdAndAppIdCheck(strategyId,appId);
        ccConfigUpdateStrategyEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccConfigUpdateStrategyEntity.setModifier(userId);
        ccConfigUpdateStrategyEntity.setUpdateTime(TimeUtil.currentTime());
        save(ccConfigUpdateStrategyEntity);
    }

    @Override
    public boolean checkUserHasCreatePermission(int appId, int userId) {
        Assert4CC.isTrue(appId !=0 ,"应用主键不不能为空");
        Assert4CC.isTrue(userId!=0,"用户主键不能为空");
        //校验用户是否是应用的经理
        CcAppUserRelEntity ccAppUserRelEntity = appUserRelRepository.findByUserIdAndRoleIdAndAppIdAndStatus(userId,ProjectConstants.ROLE_MANAGER,appId,ProjectConstants.STATUS_VALID);
        return ccAppUserRelEntity != null;
    }

    //******************************************************校验参数
    private void findConfigUpdateStrategyCheckParam(QueryUpdateStrategyReqVO queryUpdateStrategyReqVO){
        Assert4CC.isTrue(queryUpdateStrategyReqVO.getAppId() !=0,"应用主键不能为空");
        long startDate = queryUpdateStrategyReqVO.getStartDate();
        long endDate = queryUpdateStrategyReqVO.getEndDate();
        if(startDate !=0 && endDate !=0){
            Assert4CC.isTrue(startDate < endDate,"开始时间必须要小于结束时间");
        }
    }

    private void createConfigUpdateStrategyCheckParam(CreateConfigUpdateStrategyReqVO createConfigUpdateStrategyReqVO){
        Assert4CC.isTrue(createConfigUpdateStrategyReqVO.getAppId()!=0 , "应用主键不能为空");
        Assert4CC.hasLength(createConfigUpdateStrategyReqVO.getStrategyName(),"策略名称不能为空");
    }

    private void deleteConfigUpdateStrategyCheckParam(DeleteConfigUpdateStrategyReqVO deleteConfigUpdateStrategyReqVO){
        Assert4CC.isTrue(deleteConfigUpdateStrategyReqVO.getAppId() !=0,"应用主键不能为空");
        Assert4CC.isTrue(deleteConfigUpdateStrategyReqVO.getStrategyId()!=0 ,"策略主键不能为空");

    }



}
