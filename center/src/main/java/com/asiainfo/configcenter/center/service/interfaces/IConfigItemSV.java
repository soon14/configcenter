package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import com.asiainfo.configcenter.center.entity.CcConfigItemHisEntity;
import com.asiainfo.configcenter.center.entity.CcTaskDetailConfigEntity;
import com.asiainfo.configcenter.center.vo.configItem.*;

import java.util.List;

/**
 * 配置项服务接口
 * Created by oulc on 2018/7/25.
 */
public interface IConfigItemSV {

    /**
     * 根据环境标识查询配置项列表
     * @param envId 环境标识
     * @return 配置项列表
     */
    List<CcConfigItemEntity> getConfigItemsByEnvId(int envId);

    /**
     * 根据条件获取配置项
     * @author bawy
     * @date 2018/7/31 22:14
     * @param requestContainer 请求参数容器
     * @return 符合条件的配置项列表
     */
    PageResultContainer<ConfigItemVO> getConfigItemsByCondition(PageRequestContainer<ConfigItemQueryReqVO> requestContainer);

    /**
     * 新增配置项
     * @author bawy
     * @date 2018/8/1 10:29
     * @param configItem 请求参数容器
     * @param creator 创建人（当前登录用户）
     * @return 新增成功提示语
     */
    String addConfigItem(ConfigItemReqVO configItem, int creator);

    /**
     * 任务审核通过后，将任务详情中的配置项内容保存到数据库
     * @author bawy
     * @date 2018/8/1 17:11
     * @param taskId 任务标识
     * @param envId 环境标识
     * @param taskDetailConfigEntity 配置项变更详情
     */
    void saveOrUpdateItem(int taskId, int envId, CcTaskDetailConfigEntity taskDetailConfigEntity);

    /**
     * 修改配置项
     * @author bawy
     * @date 2018/8/2 12:57
     * @param configItem 请求参数
     * @param modifier 修改人（当前登录用户）
     * @return 修改成功提示语
     */
    String modConfigItem(ConfigItemReqVO configItem, int modifier);

    /**
     * 配置项复制
     * @param configItemCopyReq 请求参数对象
     * @param creator 创建人
     * @return 成功复制提示语
     */
    String copyConfigItem(ConfigItemCopyReqVO configItemCopyReq, int creator);

    /**
     * 删除配置项
     * @param configItemReq 配置项请求参数
     * @param modifier 修改人（当前登录用户）
     * @return 删除成功提示语
     */
    String delConfigItem(ConfigItemReqVO configItemReq, int modifier);

    /**
     * 获取配置项历史
     * @author bawy
     * @date 2018/8/7 15:03
     * @param configItemHisReq 请求参数
     * @return 配置项历史数据列表
     */
    List<ConfigItemHisVO> getConfigItemHis(ConfigItemHisReqVO configItemHisReq);

    /**
     * 获取配置项最新版本内容
     * @author bawy
     * @date 2018/8/9 14:52
     * @param configItemHisReq 请求参数
     * @return 配置项内容
     */
    ConfigItemHisVO getVersionContent(ConfigItemHisReqVO configItemHisReq);

    /**
     * 根据主键查询配置项
     * @author oulc
     * @date 18-8-14 下午5:34
     * @param id 配置项主键
     * @param envId 环境主键
     * @return 配置项实体
     */
    CcConfigItemEntity getConfigItemById(int id,int envId);

    /**
     * 根据主键查询配置项(校验)
     * @author oulc
     * @date 18-8-14 下午5:34
     * @param id 配置项主键
     * @param envId 环境主键
     * @return 配置项实体
     */
    CcConfigItemEntity getConfigItemByIdCheck(int id,int envId);

    /**
     * 根据策略主键查询配置项
     * @author oulc
     * @date 18-9-3 上午11:07
     * @param strategyId 配置策略主键
     * @return 配置项列表
     */
    List<CcConfigItemEntity> getConfigItemByStrategyId(int strategyId);

    /**
     * 根据策略逐渐查询配置项名称
     * @author oulc
     * @date 18-9-3 上午11:07
     * @param strategyId 策略主键
     * @return 配置名称列表
     */
    String [] getConfigItemNamesByStrategyId(int strategyId);
    /**
     * 根据主键查询配置文件
     * */
    CcConfigItemHisEntity getConfigItemHisVersionByIdCheck(int envId,int id);
}
