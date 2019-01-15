package com.asiainfo.configcenter.center.vo.org;

import java.io.Serializable;

/**
 * 组织基本信息VO对象
 * Created by bawy on 2018/8/16 20:01.
 */
public class BaseOrgVO implements Serializable{

    private static final long serialVersionUID = 4114088961553430614L;

    private int id;
    private String name;
    private byte level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }
}
