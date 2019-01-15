package com.asiainfo.configcenter.zookeeper.cczk.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 配置更新结果对象
 * Created by bawy on 2018/9/10 17:06.
 */
public class ZKUpdateResultVO implements Serializable {

    private static final long serialVersionUID = 4775065497151426919L;

    private List<ZKConfigResultVO> files;
    private List<ZKConfigResultVO> items;

    public List<ZKConfigResultVO> getFiles() {
        return files;
    }

    public void setFiles(List<ZKConfigResultVO> files) {
        this.files = files;
    }

    public List<ZKConfigResultVO> getItems() {
        return items;
    }

    public void setItems(List<ZKConfigResultVO> items) {
        this.items = items;
    }
}
