package com.asiainfo.configcenter.center.service.SV;

import com.asiainfo.configcenter.center.vo.app.AppInfoVO;

/**
 * Created by oulc on 2018/7/30.
 */
public interface IJunitSV {
    int createApp()throws Exception;

    int createEnv(int appId,String appName)throws Exception;

    void createApp(AppInfoVO appInfoVO)throws Exception;

    void createEnv(AppInfoVO appInfoVO)throws Exception;

    AppInfoVO createAppAndEnv()throws Exception;

    void rollBack()throws Exception;

    void createInstanceNode(AppInfoVO appInfoVO,int insNum)throws Exception;

    int createInsDataBaseInfo(AppInfoVO appInfoVO,int insNum)throws Exception;

    int createInsAll(AppInfoVO appInfoVO,int insNum)throws Exception;

    void createConfigFile(AppInfoVO appInfoVO);

}
