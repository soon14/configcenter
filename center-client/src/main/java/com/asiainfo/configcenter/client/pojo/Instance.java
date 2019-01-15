package com.asiainfo.configcenter.client.pojo;

public class Instance {
    private String instanceName;
    private String instanceIp;
    private Long createTime;

    public Instance() {}

    public Instance(String instanceName, String instanceIp, Long createTime) {
        this.instanceName = instanceName;
        this.instanceIp = instanceIp;
        this.createTime = createTime;
    }

    public String getInstanceIp() {
        return instanceIp;
    }

    public void setInstanceIp(String instanceIp) {
        this.instanceIp = instanceIp;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInstanceName() {

        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
