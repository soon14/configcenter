package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.ConfigItemHisRepository;
import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import com.asiainfo.configcenter.center.entity.CcConfigItemHisEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigItemHisSV;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 配置项历史数据服务实现类
 * Created by bawy on 2018/8/1 11:03.
 */
@Service
public class ConfigItemHisSVImpl implements IConfigItemHisSV {

    @Resource
    private ConfigItemHisRepository configItemHisRepository;

    @Override
    @Transactional
    public CcConfigItemHisEntity transferConfigItem(CcConfigItemEntity configItemEntity) {
        CcConfigItemHisEntity configItemHisEntity = new CcConfigItemHisEntity();
        configItemHisEntity.setItemId(configItemEntity.getId());
        configItemHisEntity.setItemKey(configItemEntity.getItemKey());
        configItemHisEntity.setItemValue(configItemEntity.getItemValue());
        configItemHisEntity.setDescription(configItemEntity.getDescription());
        configItemHisEntity.setCreator(configItemEntity.getModifier());
        configItemHisEntity.setCreateTime(configItemEntity.getUpdateTime());
        configItemHisEntity.setStatus(ProjectConstants.STATUS_VALID);
        configItemHisEntity = configItemHisRepository.saveAndFlush(configItemHisEntity);
        return configItemHisEntity;
    }

}
