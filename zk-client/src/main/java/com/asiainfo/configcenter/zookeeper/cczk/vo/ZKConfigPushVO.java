package com.asiainfo.configcenter.zookeeper.cczk.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 配置推送时zk节点数据对象
 * Created by bawy on 2018/9/10 15:47.
 */
public class ZKConfigPushVO implements Serializable {

    private static final long serialVersionUID = 372976776000481715L;

    private List<ZKConfigVO> files;
    private List<ZKConfigVO> items;

    public List<ZKConfigVO> getFiles() {
        return files;
    }

    public void setFiles(List<ZKConfigVO> files) {
        this.files = files;
    }

    public List<ZKConfigVO> getItems() {
        return items;
    }

    public void setItems(List<ZKConfigVO> items) {
        this.items = items;
    }
}
