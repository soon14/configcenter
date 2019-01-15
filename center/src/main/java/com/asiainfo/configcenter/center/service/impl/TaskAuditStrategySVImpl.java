package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ErrorCodeException;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.dao.repository.TaskAuditStrategyRepository;
import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import com.asiainfo.configcenter.center.entity.CcRoleEntity;
import com.asiainfo.configcenter.center.entity.CcTaskAuditStrategyEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOrgSV;
import com.asiainfo.configcenter.center.service.interfaces.IRoleSV;
import com.asiainfo.configcenter.center.service.interfaces.ITaskAuditStrategySV;
import com.asiainfo.configcenter.center.service.interfaces.ITaskSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.vo.audit.AddAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.AuditStrategyStepVO;
import com.asiainfo.configcenter.center.vo.audit.DelAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.ModAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.org.OrgTreeVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务审核策略服务实现类
 * Created by bawy on 2018/8/2 9:23.
 */
@Service
public class TaskAuditStrategySVImpl implements ITaskAuditStrategySV {

    @Resource
    private TaskAuditStrategyRepository taskAuditStrategyRepository;

    @Resource
    private IRoleSV roleSV;

    @Resource
    private IOrgSV orgSV;

    @Resource
    private ITaskSV taskSV;

    private Logger logger = Logger.getLogger(TaskAuditStrategySVImpl.class);

    @Override
    public int getSuperOperator(int operator, int envId) {
        /*CcTaskAuditStrategyEntity taskAuditStrategyEntity = taskAuditStrategyRepository.findByAppEnvIdAndOperatorAndStatus(envId, operator, ProjectConstants.STATUS_VALID);
        if (taskAuditStrategyEntity == null){
            //未设置审核策略
            throw new ErrorCodeException(ResultCodeEnum.TASK_NO_AUDIT_STRATEGY_ERROR, operator, envId);
        }else{
            //设置了审核策略，需要判断上级审核人员是否还在当前应用中
            int superOperator = taskAuditStrategyEntity.getSuperOperator();
            if (superOperator!=0) {
                CcAppUserRelEntity appUserRelEntity = appUserRelRepository.getByEnvIdAndUserId(envId, superOperator, ProjectConstants.STATUS_VALID);
                Assert4CC.notNull(appUserRelEntity, ResultCodeEnum.TASK_AUDIT_OPERATOR_NOT_EXIST, operator, superOperator, appUserRelEntity.getAppId());
            }
            return superOperator;
        }*/
        return 0;
    }

    @Override
    public List<AuditStrategyStepVO> getAuditStrategy(int envId) {
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        List<CcTaskAuditStrategyEntity> auditStrategyEntities = taskAuditStrategyRepository.findByAppEnvIdAndStatusOrderByStepAsc(envId, ProjectConstants.STATUS_VALID);
        if (auditStrategyEntities == null){
            return new ArrayList<>();
        }else {
            List<AuditStrategyStepVO> auditStrategySteps = new ArrayList<>();
            for (CcTaskAuditStrategyEntity auditStrategyEntity : auditStrategyEntities){
                AuditStrategyStepVO auditStrategyStep = convertAuditStrategyStep(auditStrategyEntity);
                auditStrategySteps.add(auditStrategyStep);
            }
            return auditStrategySteps;
        }
    }

    @Override
    public AuditStrategyStepVO convertAuditStrategyStep(CcTaskAuditStrategyEntity auditStrategyEntity){
        AuditStrategyStepVO auditStrategyStep = new AuditStrategyStepVO();
        int orgId = auditStrategyEntity.getOrgId();
        int roleId = auditStrategyEntity.getRoleId();
        auditStrategyStep.setStepId(auditStrategyEntity.getId());
        auditStrategyStep.setStep(auditStrategyEntity.getStep());
        auditStrategyStep.setOrgId(orgId);
        auditStrategyStep.setRoleId(roleId);
        CcRoleEntity roleEntity = roleSV.getRoleByIdAndRoleType(roleId, ProjectConstants.ROLE_TYPE_ORG);
        //获取角色名称
        if(roleEntity == null){
            logger.error("组织角色"+roleId+"已经不存在，但仍然在环境"+auditStrategyEntity.getAppEnvId()+"的审核策略中");
            auditStrategyStep.setRoleName("未知角色");
        }else{
            auditStrategyStep.setRoleName(roleEntity.getRoleName());
        }
        //获取组织名称
        if (orgId == 0){
            auditStrategyStep.setOrgName("任务提交人所属组织");
        }else{
            CcOrgEntity orgEntity = orgSV.getOrgById(orgId);
            if (orgEntity == null){
                logger.error("组织"+orgId+"已经不存在，但仍然在环境"+auditStrategyEntity.getAppEnvId()+"的审核策略中");
                auditStrategyStep.setOrgName("未知组织");
            }else{
                auditStrategyStep.setOrgName(orgEntity.getName());
            }
        }
        return auditStrategyStep;
    }

    @Override
    @Transactional
    public Integer addAuditStrategyStep(AddAuditStrategyStepReqVO addAuditStrategyStepReq, int creator) {
        int envId = addAuditStrategyStepReq.getEnvId();
        int orgId = addAuditStrategyStepReq.getOrgId();
        int roleId = addAuditStrategyStepReq.getRoleId();
        //校验
        checkEnv(envId);
        checkOrgAndRole(envId, orgId, roleId, -1);
        CcTaskAuditStrategyEntity newStep = new CcTaskAuditStrategyEntity();
        newStep.setAppEnvId(envId);
        newStep.setOrgId(orgId);
        newStep.setRoleId(roleId);
        Timestamp currentTime = TimeUtil.currentTime();
        newStep.setCreator(creator);
        newStep.setCreateTime(currentTime);
        newStep.setModifier(creator);
        newStep.setUpdateTime(currentTime);
        newStep.setStatus(ProjectConstants.STATUS_VALID);
        List<CcTaskAuditStrategyEntity> taskAuditStrategyEntities = taskAuditStrategyRepository.findByAppEnvIdAndStatusOrderByStepDesc(envId, ProjectConstants.STATUS_VALID);
        if (taskAuditStrategyEntities == null || taskAuditStrategyEntities.size()==0){
            newStep.setStep((byte)1);
        }else{
            byte step = taskAuditStrategyEntities.get(0).getStep();
            newStep.setStep((byte)(step+1));
        }
        taskAuditStrategyRepository.save(newStep);
        return newStep.getId();
    }

    @Override
    @Transactional
    public String modAuditStrategyStep(ModAuditStrategyStepReqVO modAuditStrategyStepReq, int modifier) {
        int stepId = modAuditStrategyStepReq.getStepId();
        int envId = modAuditStrategyStepReq.getEnvId();
        int orgId = modAuditStrategyStepReq.getOrgId();
        int roleId = modAuditStrategyStepReq.getRoleId();
        checkEnv(envId);
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = checkAuditStrategyExist(envId, stepId);
        checkOrgAndRole(envId, orgId, roleId, stepId);
        taskAuditStrategyEntity.setOrgId(orgId);
        taskAuditStrategyEntity.setRoleId(roleId);
        taskAuditStrategyEntity.setModifier(modifier);
        taskAuditStrategyEntity.setUpdateTime(TimeUtil.currentTime());
        return "修改审核策略步骤成功";
    }

    @Override
    @Transactional
    public String delAuditStrategyStep(DelAuditStrategyStepReqVO delAuditStrategyStepReq, int modifier) {
        int stepId = delAuditStrategyStepReq.getStepId();
        int envId = delAuditStrategyStepReq.getEnvId();
        checkEnv(envId);
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = checkAuditStrategyExist(envId, stepId);
        taskAuditStrategyEntity.setModifier(modifier);
        taskAuditStrategyEntity.setUpdateTime(TimeUtil.currentTime());
        taskAuditStrategyEntity.setStatus(ProjectConstants.STATUS_INVALID);
        //大于此步骤的所有步骤setp减一
        taskAuditStrategyRepository.updateStep(envId, taskAuditStrategyEntity.getStep(), ProjectConstants.STATUS_VALID);
        return "删除审核策略步骤成功";
    }

    @Override
    public List<OrgTreeVO> getOrgTree(int envId) {
        return orgSV.getAllOrgTree();
    }


    /**
     * 校验环境是否允许修改审核策略
     * @author bawy
     * @date 2018/8/24 13:09
     * @param envId 环境标识
     */
    private void checkEnv(int envId){
        Assert4CC.isTrue(envId>0, "环境标识不可为空");
        //校验当前环境是否存在审核中的任务
        if (taskSV.hasInReviewTask(envId)){
            throw new ErrorCodeException(ResultCodeEnum.TASK_CAN_NOT_CHANGE_AUDIT_STRATEGY_ERROR);
        }
    }

    /**
     * 校验组织和角色
     * *************************************
     * 1.校验参数是否填写
     * 2.校验组织是否存在
     * 3.校验角色是否存在
     * 4.校验组织是否已经存在于该审核策略的流程中（如果与当前要修改的步骤则允许）
     * *************************************
     * @author bawy
     * @date 2018/8/23 16:39
     * @param envId 环境标识
     * @param orgId 组织标识
     * @param roleId 角色标识
     * @param stepId 审核策略步骤标识
     */
    private void checkOrgAndRole(int envId, int orgId, int roleId, int stepId){
        Assert4CC.isTrue(orgId>=0, "组织标识不可为空");
        Assert4CC.isTrue(roleId>0, "角色标识不为空");
        if (orgId == 0){
            List<CcTaskAuditStrategyEntity> taskAuditStrategyEntities = taskAuditStrategyRepository.findByAppEnvIdAndStatusOrderByStepAsc(envId, ProjectConstants.STATUS_VALID);
            Assert4CC.isTrue(taskAuditStrategyEntities==null||taskAuditStrategyEntities.size()==0||taskAuditStrategyEntities.get(0).getId()==stepId, ResultCodeEnum.TASK_SELF_ORG_NOT_IS_FIRST_ERROR);
        }else {
            CcOrgEntity orgEntity = orgSV.getOrgById(orgId);
            Assert4CC.notNull(orgEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织"+orgId+"不存在或已失效");
        }
        if (roleId>0){
            CcRoleEntity roleEntity = roleSV.getRoleByIdAndRoleType(roleId, ProjectConstants.ROLE_TYPE_ORG);
            Assert4CC.notNull(roleEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织角色"+roleId+"不存在或已失效");
        }
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = taskAuditStrategyRepository.findByAppEnvIdAndOrgIdAndStatus(envId, orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.isTrue(taskAuditStrategyEntity==null||taskAuditStrategyEntity.getId()==stepId, ResultCodeEnum.TASK_AUDIT_STRATEGY_ORG_EXIST_ERROR);
    }

    /**
     * 校验审核策略步骤存在
     * @author bawy
     * @date 2018/8/23 17:02
     * @param envId 环境标识
     * @param stepId 步骤标识
     * @return 存在返回数据，不存在抛出异常
     */
    private CcTaskAuditStrategyEntity checkAuditStrategyExist(int envId, int stepId){
        Assert4CC.isTrue(stepId>0, "要修改的审核策略步骤标识不可为空");
        CcTaskAuditStrategyEntity taskAuditStrategyEntity = taskAuditStrategyRepository.findByIdAndAppEnvIdAndStatus(stepId, envId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(taskAuditStrategyEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "审核策略步骤"+stepId+"不存在或已经失效");
        return taskAuditStrategyEntity;
    }

    @Override
    public CcTaskAuditStrategyEntity getAuditStrategyStep(int envId, byte step) {
        return taskAuditStrategyRepository.findByAppEnvIdAndStepAndStatus(envId, step, ProjectConstants.STATUS_VALID);
    }

}
