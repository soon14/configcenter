package com.asiainfo.configcenter.client.pojo;

import java.util.List;
import java.util.Map;

/**
 * 所有更新到的配置项
 * Created by bawy on 2018/9/11 20:18.
 */
public class AllConfigItemPojo {

    private Map<String, String> itemMap;
    private List<ConfigItemPojo> configItemPojos;

    public Map<String, String> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, String> itemMap) {
        this.itemMap = itemMap;
    }

    public List<ConfigItemPojo> getConfigItemPojos() {
        return configItemPojos;
    }

    public void setConfigItemPojos(List<ConfigItemPojo> configItemPojos) {
        this.configItemPojos = configItemPojos;
    }
}
