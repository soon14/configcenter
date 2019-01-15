package com.asiainfo.configcenter.center.vo;

import java.io.Serializable;

/**
 * App应用模块请求对象基类
 * Created by bawy on 2018/7/24 21:38.
 */
public interface BaseAppReqVO extends Serializable {


    int getAppId();

    int getEnvId();

}
