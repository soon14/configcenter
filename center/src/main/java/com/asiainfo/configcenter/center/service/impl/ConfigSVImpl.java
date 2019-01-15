package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.CcConfigFileEntity;
import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigFileSV;
import com.asiainfo.configcenter.center.service.interfaces.IConfigItemSV;
import com.asiainfo.configcenter.center.service.interfaces.IConfigSV;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConfigSVImpl implements IConfigSV {

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private IConfigItemSV iConfigItemSV;

    @Override
    public String getConfigNameByConfigTypeAndId(int envId, byte configType, int configId) {
        String configName = null;
        if(configType == ProjectConstants.CONFIG_TYPE_FILE){
            CcConfigFileEntity ccConfigFileEntity = iConfigFileSV.getConfigFileByIdAndEnvIdCheck(configId,envId);
            configName = ccConfigFileEntity.getFileName();
        }else if(configType == ProjectConstants.CONFIG_TYPE_ITEM){
            CcConfigItemEntity ccConfigItemEntity = iConfigItemSV.getConfigItemByIdCheck(configId,envId);
            configName = ccConfigItemEntity.getItemKey();
        }
        return configName;
    }
}
