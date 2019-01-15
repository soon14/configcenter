package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.CcInstanceEntity;
import com.asiainfo.configcenter.center.po.InstanceConfigPojo;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInsByConfigReqVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInstanceReqVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by oulc on 2018/7/26.
 * 实例业务层
 */
public interface IInstanceSV {
    /**
     * 查询实例列表
     * @param pageRequestContainer 查询条件
     * @return 实例列表
     * @author oulc
     * @date 2018/7/26 14:19
     */
    PageResultContainer<CcInstanceEntity> findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer);

    /**
     * 查询实例列表
     * @param envId 环境主键
     * @param insName 实例名称
     * @param isAlive 是否存活 1存活 0断开 -1不作为条件
     * @param insIP 实例部署IP
     * @return 环境实体列表
     * @author oulc
     * @date 2018/7/26 14:18
     */
    Page<CcInstanceEntity> findInstance(int envId ,String insName , byte isAlive , String insIP, int currentPage, int size);

    /**
     * 如果数据库信息和zk信息不一致 修复数据 修复数据过程中发生异常需要发送邮件
     * @param envId 环境主键
     * @author oulc
     * @date 2018/7/30 11:20
     */
    void fixData(int envId);

    /**
     * 修复数据 遍历表数据
     * @param zkChild 环境节点的子节点
     * @param envId 环境主键
     * @author oulc
     * @date 2018/7/30 11:21
     */
    void fixDataLoopDataBaseInfo(List<String> zkChild, int envId);

    /**
     * 修复数据 遍历zk数据
     * @param zkChild 环境节点的子节点
     * @param envId 环境主键
     * @param appName 应用名称
     * @param envName 环境名称
     * @throws Exception 异常
     * @author oulc
     * @date 2018/7/30 11:21
     */
    void fixDataLoopZkInfo(List<String> zkChild,int envId,String appName,String envName)throws Exception;

    /**
     * 创建实例 需要从zk中读取ip数据
     * @param appName 应用名称
     * @param envName 环境名称
     * @param zkInsName 实例名称
     * @param envId 环境主键
     * @return 实例主键
     * @author oulc
     * @date 2018/7/30 11:22
     */
    int createInstance(String appName ,String envName,String zkInsName,int envId) ;

    /**
     * 创建实例 创建实例的表数据
     * @param appName 应用名称
     * @param envName 环境名称
     * @param zkInsName 实例名称
     * @param envId 环境主键
     * @param zkIp 实例ip
     * @return 实例主键
     * @author oulc
     * @date 2018/7/30 11:22
     */
    int createInstance(String appName ,String envName,String zkInsName,int envId,String zkIp);

    /**
     * 删除实例 判断zk节点之后 把表数据置为失效
     * @param insId 实例主键
     * @author oulc
     * @date 2018/7/30 11:22
     */
    void deleteInstance(int insId);

    /**
     * 根据实例主键查询应用信息
     * @param insId 实例主键
     * @return 应用信息
     * @author oulc
     * @date 2018/7/30 11:24
     */
    AppInfoVO getAppInfoByInsId(int insId);

    /**
     * 删除实例数据库信息
     * @param insId 实例主键
     * @author oulc
     * @date 2018/7/30 11:24
     */
    void deleteInstanceDataBaseInfo(int insId);

    /**
     * 查询实例正在使用的配置的信息
     * @param insId 实例主键
     * @return 配置信息
     * @author oulc
     * @date 2018/7/27 15:13
     */
    InstanceConfigPojo[] getInstanceConfigInfo(int insId);

    /**
     * 设置实例的连接状态
     * @param insId 实例主键
     * @param isAlive 1：存活 0：失联
     * @author oulc
     * @date 2018/7/30 11:17
     */
    void setInstanceAliveStatus(int insId,byte isAlive);

    /**
     * 查询环境下存活的实例(zk)
     * @param appName 应用名称
     * @param envName 环境名称
     * @return 存活的实例信息
     * @author oulc
     * @date 2018/7/30 15:08
     */
    List<String> getZkAliveInstance(String appName,String envName);

    /**
     * 根据实例主键查询实例
     * @param insId 实例主键
     * @return 实例实体
     * @author oulc
     * @date 2018/7/31 10:50
     */
    CcInstanceEntity getInstanceById(int insId);

    /**
     * 根据实例主键查询实例 校验实例必须存在
     * @param insId 实例主键
     * @return 实例主题
     * @author oulc
     * @date 2018/7/31 10:57
     */
    CcInstanceEntity getInstanceByIdCheck(int insId);

    /**
     * 查询实例（not in）
     * @author oulc
     * @date 18-8-15 上午10:51
     * @param envId 环境主键
     * @param ids not in ids
     * @param insName 实例名称
     * @param insIp 实例ip
     * @return 实例列表
     */
    List<CcInstanceEntity> getInstancesByNotIn(int envId,int [] ids,String insName,String insIp);

    /**
     * 根据配置查询正在使用此配置的实例列表
     * @author oulc
     * @date 18-8-15 下午3:44
     * @param envId 环境主键
     * @param configType 配置类型
     * @param configId 配置主键
     * @param configVersion 配置版本
     * @return 实例列表
     */
    PageResultContainer<CcInstanceEntity> getInstancesByConfigIdAndVersion(int envId, byte configType, int configId, String configVersion,String insName,String insIp,int start,int size);

    /**
     * 根据配置查询实例
     * @author oulc
     * @date 18-8-15 下午3:50
     * @param pageRequestContainer 配置信息
     * @return 实例列表
     */
    PageResultContainer<CcInstanceEntity> getInstanceByConfig(PageRequestContainer<QueryInsByConfigReqVO> pageRequestContainer);

    /**
     * 根据主键 实例名称 实例Ip查询实例
     * @author oulc
     * @date 18-8-15 下午5:22
     * @param id 实例主键
     * @param insName 实例名称
     * @param insIp 实例主键
     * @return 实例实体
     */
    CcInstanceEntity getInstanceByIdAndNameAndIp(int id,String insName,String insIp);

    /**
     * 根据用户查询实例的数量
     * @author oulc
     * @date 18-8-28 下午2:42
     * @param userId 用户主键
     * @return 实例数量
     */
    long getInstanceCountByUserId(int userId);


}
