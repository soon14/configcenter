package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcAppEnvEntity;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.env.AppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.DelAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.QueryAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.UpdateAppEnvReqVO;

import java.util.List;

public interface IAppEnvSV {
    /**
     * 创建环境
     * @param createAppEnvReqVO 环境数据
     * @param userId 用过户主键
     * @return 环境主键
     * @author oulc
     * @date 2018/7/25 9:37
     */
    int createAppEnv(AppEnvReqVO createAppEnvReqVO, int userId);

    /**
     * 查询环境
     * @param envId 环境主键
     * @return 环境实体
     * @author oulc
     * @date 2018/7/24 14:54
     */
    CcAppEnvEntity getEnvByEnvId( int envId);

    /**
     * 根据项目主键和环境名称查询环境
     * @param appId 项目主键
     * @param envName 环境名称
     * @return 环境实体
     */
    List<CcAppEnvEntity> getEnvByAppIdAndEnvName(int appId ,String envName);



    /**
     * 保存环境表
     * @param appId app主键
     * @param envName 项目名称
     * @param desc 项目简介
     * @param userId 用户主键
     * @author oulc
     * @date 2018/7/24 15:50
     */
    CcAppEnvEntity saveAppEnv(int appId,String envName,String desc,int userId);



    /**
     * 修改环境信息
     * @param appEnvReqVO 环境信息
     * @author oulc
     * @date 2018/7/24 17:12
     */
    void updateAppEnv(UpdateAppEnvReqVO appEnvReqVO);

    /**
     *
     * @param envId 环境主机按
     * @param envName 环境名称
     * @param desc 描述
     * @return 旧环境名称
     * @author oulc
     * @date 2018/7/25 14:38
     */
    String updateAppEnvDataBaseInfo(int envId,String envName,String desc);

    /**
     * 查询环境信息
     * @param appId app主键
     * @return 环境信息
     * @author oulc
     * @date 2018/7/25 14:42
     */
    List<CXCcAppEnvEntity> getEnvByAppId(int appId);

    /**
     * 查询环境信息
     * @param queryAppEnvReqVO 查询条件
     * @return 环境信息
     * @author oulc
     * @date 2018/7/25 14:42
     */
    List<CXCcAppEnvEntity>  getEnvByAppId(QueryAppEnvReqVO queryAppEnvReqVO);

    /**
     * 删除环境
     * @param envId 环境主键
     * @author oulc
     * @date 2018/7/25 15:07
     */
    void deleteEnv(int envId);

    /**
     * 删除环境
     * @author oulc
     * @date 2018/8/8 15:04
     * @param delAppEnvReqVO 环境条件
     */
    void deleteEnv(DelAppEnvReqVO delAppEnvReqVO);

    /**
     * 校验环境下面没有配置文件或者配置项
     * @author oulc
     * @date 2018/8/8 15:03
     * @param envId 环境主键
     */
    void checkAppEnvHasNoneConfig(int envId);

    /**
     * 根据环境主键查询app名称和env名称
     * @author oulc
     * @date 2018/8/8 15:03
     * @param envId 环境主键
     * @return 应用信息
     */
    AppInfoVO getAppInfoByEnvId(int envId);

    /**
     * 根据环境主键查询环境 校验:如果环境不存在，抛异常
     * @author oulc
     * @date 2018/8/8 15:03
     * @param envId  环境主键
     * @return 环境实体
     */
    CcAppEnvEntity getEnvByEnvIdCheck(int envId);

    /**
     * 根据用户主键查询环境数量
     * @author oulc
     * @date 18-8-28 下午2:26
     * @param userId 用户主键
     * @return 环境数量
     */
    long getEnvCountByUserId(int userId);

}
