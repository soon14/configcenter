package com.asiainfo.configcenter.center.service.interfaces;

public interface IConfigSV {
    String getConfigNameByConfigTypeAndId(int envId,byte configType,int configId);
}
