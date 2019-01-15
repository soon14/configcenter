package com.asiainfo.configcenter.center.vo.client;

import java.io.Serializable;

/**
 * 配置文件下载返回参数
 *
 * Author: Erick
 * Date: 2018/9/6
 */
public class ClientConfigFilesResp implements Serializable  {

    private static final long serialVersionUID = 3237410847732578643L;
    private byte [] configFileZipData;

    public byte[] getConfigFileZipData() {
        return configFileZipData;
    }

    public void setConfigFileZipData(byte[] configFileZipData) {
        this.configFileZipData = configFileZipData;
    }
}