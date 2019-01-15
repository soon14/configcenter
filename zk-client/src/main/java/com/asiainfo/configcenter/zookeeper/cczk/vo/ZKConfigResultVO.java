package com.asiainfo.configcenter.zookeeper.cczk.vo;

import java.io.Serializable;

/**
 * 配置结果
 * Created by bawy on 2018/9/10 17:11.
 */
public class ZKConfigResultVO implements Serializable {

    private static final long serialVersionUID = -2924097721242212155L;

    private String name;
    private boolean result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
