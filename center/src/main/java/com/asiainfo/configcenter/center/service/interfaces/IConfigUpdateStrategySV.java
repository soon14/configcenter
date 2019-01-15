package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.vo.strategy.*;

/**
 * Created by oulc on 2018/8/9.
 */
public interface IConfigUpdateStrategySV {


    /**
     * 创建更新策略
     * @author oulc
     * @date 2018/8/9 15:46
     * @param createConfigUpdateStrategyReqVO 策略信息
     * @param userId 用户主键
     * @return 策略主键
     */
    int createConfigUpdateStrategy(CreateConfigUpdateStrategyReqVO createConfigUpdateStrategyReqVO,int userId);

    /**
     * 创建配置更新策略数据库信息
     * @author oulc
     * @date 2018/8/9 15:47
     * @param appId 应用主键
     * @param strategyName 策略主键
     * @param desc 描述
     * @param userId 用户主键
     * @return 更新策略主键
     */
    int createConfigUpdateStrategyDataBaseInfo(int appId,String strategyName,String desc,int userId);

    /**
     * 查询配置更新策略
     * @author oulc
     * @date 2018/8/13 7:09
     * @param id 策略步骤
     * @param appId 应用主键
     * @return 更新策略实体
     */
    CcConfigUpdateStrategyEntity getConfigUpdateStrategyByIdAndAppId(int id,int appId);

    /**
     * 查询配置策略列表
     * @author oulc
     * @date 2018/8/13 7:10
     * @param pageRequestContainer 查询条件
     * @return 策略列表实体
     */
    PageResultContainer<CXCcConfigUpdateStrategyEntity> findConfigUpdateStrategy(PageRequestContainer<QueryUpdateStrategyReqVO> pageRequestContainer);

    /**
     * 查询策略的所有信息
     * @author oulc
     * @date 2018/8/13 7:11
     * @param id 策略主键
     * @param appId 应用主键
     * @return 策略信息
     */
    ConfigUpdateStrategyVO getConfigUpdateStrategyAllById(int id,int appId);

    /**
     * 更新配置更新策略
     * @author oulc
     * @date 2018/8/13 10:28
     * @param appId 应用主键
     * @param strategyId 策略主键
     * @param strategyName 策略名称
     * @param desc 策略描述
     * @param userId 用户主键
     */
    int updateConfigUpdateStrategyDataBaseInfo(int appId,int strategyId,String strategyName,String desc,int userId);

    /**
     * 查询配置更新策略
     * @author oulc
     * @date 2018/8/13 6:40
     * @param id 策略主键
     * @param appId 应用主键
     * @return 策略实体
     */
    CcConfigUpdateStrategyEntity getConfigUpdateStrategyByIdAndAppIdCheck(int id, int appId);

    /**
     * 删除策略
     * @author oulc
     * @date 2018/8/13 7:12
     * @param strategyId 策略主键
     * @param appId 应用主键
     * @param userId 用户主键
     */
    void deleteConfigUpdateStrategy(int strategyId,int appId,int userId);

    /**
     * 保存实体
     * @author oulc
     * @date 2018/8/13 7:14
     * @param ccConfigUpdateStrategyEntity 实体
     * @return 实体
     */
    CcConfigUpdateStrategyEntity save(CcConfigUpdateStrategyEntity ccConfigUpdateStrategyEntity);

    /**
     * 删除配置更新策略
     * @author oulc
     * @date 2018/8/13 16:13
     * @param deleteConfigUpdateStrategyReqVO 配置更新策略信息
     * @param userId 用户主键
     */
    void deleteConfigUpdateStrategy(DeleteConfigUpdateStrategyReqVO deleteConfigUpdateStrategyReqVO,int userId);

    /**
     * 获取刷新策略名称
     * @author bawy
     * @date 2018/8/22 17:29
     * @param queryStrategyAllReq 请求参数
     */
    String getStrategyName(QueryStrategyAllReqVo queryStrategyAllReq);

    /**
     * 校验用户是否有权限新增更新策略
     * @author oulc
     * @date 18-8-24 下午2:55
     * @param appId 应用主键
     * @param userId 用户主键
     * @return true:有权限 false:无权限
     */
    boolean checkUserHasCreatePermission(int appId,int userId);
}
