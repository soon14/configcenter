package com.asiainfo.configcenter.zookeeper.cczk.vo;

import java.io.Serializable;

/**
 * 配置文件对象
 * Created by bawy on 2018/9/10 15:49.
 */
public class ZKConfigVO implements Serializable{

    private static final long serialVersionUID = 4616947394609714824L;

    private String name;
    private String version;

    public ZKConfigVO(){

    }

    public ZKConfigVO(String name, String version){
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}