package com.asiainfo.configcenter.center.vo.instance;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 查询实例请求参数对象
 * Created by oulc on 2018/7/26.
 */
public class QueryInstanceReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -8487060618991088642L;

    //只需要envId
    private int envId;
    private String insName;
    private String insIp;
    private byte isAlive;

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

    public byte getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(byte isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public int getAppId() {
        return 0;
    }

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }
}
