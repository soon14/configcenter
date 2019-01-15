package com.asiainfo.configcenter.center.vo.system;

import java.io.Serializable;

/**
 * 下拉列表数据对象
 * Created by bawy on 2018/7/23 14:11.
 */
public class SelectDataVO implements Serializable {

    private static final long serialVersionUID = -2435873835305783852L;

    private String value;
    private String text;
    private String ext2;
    private String ex3;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getEx3() {
        return ex3;
    }

    public void setEx3(String ex3) {
        this.ex3 = ex3;
    }
}
