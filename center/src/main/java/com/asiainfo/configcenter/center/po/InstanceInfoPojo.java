package com.asiainfo.configcenter.center.po;


import java.util.Arrays;

/**
 * Created by oulc on 2018/7/27.
 */
public class InstanceInfoPojo {
    String ip;
    int instanceId;
    InstanceConfigPojo[] instanceConfigPojos;

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public InstanceConfigPojo[] getInstanceConfigPojos() {
        return instanceConfigPojos;
    }

    public void setInstanceConfigPojos(InstanceConfigPojo[] instanceConfigPojos) {
        this.instanceConfigPojos = instanceConfigPojos;
    }

    @Override
    public String toString() {
        return "InstanceInfoPojo{" +
                "ip='" + ip + '\'' +
                ", instanceId=" + instanceId +
                ", instanceConfigPojos=" + Arrays.toString(instanceConfigPojos) +
                '}';
    }
}
