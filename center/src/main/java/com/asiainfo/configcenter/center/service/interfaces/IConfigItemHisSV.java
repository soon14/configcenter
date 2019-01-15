package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import com.asiainfo.configcenter.center.entity.CcConfigItemHisEntity;

/**
 * 配置项历史数据服务接口
 * Created by bawy on 2018/8/1 11:01.
 */
public interface IConfigItemHisSV {

    /**
     * 将配置项转移到历史数据库
     * @author bawy
     * @date 2018/8/1 11:03
     * @param configItemEntity 配置项对象
     * @return 配置项历史数据
     */
    CcConfigItemHisEntity transferConfigItem(CcConfigItemEntity configItemEntity);

}
