package com.asiainfo.configcenter.center.vo.configfile;

/**
 * Created by oulc on 2018/8/1.
 */
public class UpConfigFileVO {
    private boolean isOld;
    private long uid;
    private String type;
    private String name;
    private String url;

    public boolean isOld() {
        return isOld;
    }

    public void setOld(boolean old) {
        isOld = old;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
