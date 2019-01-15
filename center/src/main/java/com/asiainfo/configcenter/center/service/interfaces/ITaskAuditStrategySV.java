package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcTaskAuditStrategyEntity;
import com.asiainfo.configcenter.center.vo.audit.AddAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.AuditStrategyStepVO;
import com.asiainfo.configcenter.center.vo.audit.DelAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.ModAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.org.OrgTreeVO;

import java.util.List;

/**
 * 任务审核策略服务接口
 * Created by bawy on 2018/8/2 9:18.
 */
public interface ITaskAuditStrategySV {

    /**
     * 获取操作员在指定环境中的上级操作员，如果未配置审核策略则抛出运行时异常
     * @author bawy
     * @date 2018/8/2 9:23
     * @param operator 操作员标识
     * @param envId 环境标识
     * @return 上级操作员，如果无须审核则返回0。
     */
    int getSuperOperator(int operator, int envId);

    /**
     * 获取环境审核策略列表
     * @author bawy
     * @param envId 环境标识
     * @return 审核策略列表
     */
    List<AuditStrategyStepVO> getAuditStrategy(int envId);

    /**
     * 将审核策略步骤转成VO对象
     * @author bawy
     * @date 2018/8/24 16:54
     * @param auditStrategyEntity 审核策略步骤实体
     * @return 转换后的VO对象
     */
    AuditStrategyStepVO convertAuditStrategyStep(CcTaskAuditStrategyEntity auditStrategyEntity);

    /**
     * 新增审核策略步骤
     * @author bawy
     * @date 2018/8/23 16:21
     * @param addAuditStrategyStepReq 请求参数
     * @param creator 创建人（当前登录用户）
     * @return 新增策略步骤的主键
     */
    Integer addAuditStrategyStep(AddAuditStrategyStepReqVO addAuditStrategyStepReq, int creator);

    /**
     * 修改审核策略步骤
     * @author bawy
     * @date 2018/8/23 16:54
     * @param modAuditStrategyStepReq 请求参数
     * @param modifier 修改人（当前登录用户）
     * @return 成功修改提示语
     */
    String modAuditStrategyStep(ModAuditStrategyStepReqVO modAuditStrategyStepReq, int modifier);

    /**
     * 删除审核策略步骤
     * @author bawy
     * @date 2018/8/23 16:54
     * @param delAuditStrategyStepReq 请求参数
     * @param modifier 修改人（当前登录用户）
     * @return 成功删除提示语
     */
    String delAuditStrategyStep(DelAuditStrategyStepReqVO delAuditStrategyStepReq, int modifier);

    /**
     * 获取组织树
     * @author bawy
     * @date 2018/8/23 18:09
     * @param envId 环境标识
     * @return 组织树
     */
    List<OrgTreeVO> getOrgTree(int envId);

    /**
     * 获取审核步骤
     * @author bawy
     * @date 2018/8/24 15:41
     * @param envId 环境标识
     * @param step 步骤序号
     * @return 审核步骤数据
     */
    CcTaskAuditStrategyEntity getAuditStrategyStep(int envId, byte step);
}
