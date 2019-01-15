package com.asiainfo.configcenter.center.vo.home;

public class HomePageRepVO {
    private long appNum;
    private long envNum;
    private long insNum;
    private long passTaskNum;
    private long auditTaskNum;
    private long rollBackTaskNum;
    private AppInfo []appInfos;

    public AppInfo[] getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(AppInfo[] appInfos) {
        this.appInfos = appInfos;
    }

    public long getAppNum() {
        return appNum;
    }

    public void setAppNum(long appNum) {
        this.appNum = appNum;
    }

    public long getEnvNum() {
        return envNum;
    }

    public void setEnvNum(long envNum) {
        this.envNum = envNum;
    }

    public long getInsNum() {
        return insNum;
    }

    public void setInsNum(long insNum) {
        this.insNum = insNum;
    }

    public long getPassTaskNum() {
        return passTaskNum;
    }

    public void setPassTaskNum(long passTaskNum) {
        this.passTaskNum = passTaskNum;
    }

    public long getAuditTaskNum() {
        return auditTaskNum;
    }

    public void setAuditTaskNum(long auditTaskNum) {
        this.auditTaskNum = auditTaskNum;
    }

    public long getRollBackTaskNum() {
        return rollBackTaskNum;
    }

    public void setRollBackTaskNum(long rollBackTaskNum) {
        this.rollBackTaskNum = rollBackTaskNum;
    }
}
