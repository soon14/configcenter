package com.asiainfo.configcenter.center.vo.system;

public class NotificationVO {
    private String creator;
    private byte type;
    private byte hasRead;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getHasRead() {
        return hasRead;
    }

    public void setHasRead(byte hasRead) {
        this.hasRead = hasRead;
    }
}
