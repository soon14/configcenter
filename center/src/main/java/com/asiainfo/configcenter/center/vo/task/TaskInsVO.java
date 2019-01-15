package com.asiainfo.configcenter.center.vo.task;

import java.sql.Timestamp;

/**
 * Author: Erick
 * Date: 2018/9/4
 *
 * 推送任务实例信息 返回参数
 *
 */
public class TaskInsVO {

    private String insName;
    private String insIp;
    private Timestamp updateTime;

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName;
    }

    public String getInsIp() {
        return insIp;
    }

    public void setInsIp(String insIp) {
        this.insIp = insIp;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
