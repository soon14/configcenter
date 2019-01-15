package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.configpush.PushConfigReqVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigPushVO;

public interface IConfigPushSV {
    /**
     * 推送配置文件
     * @author oulc
     * @date 18-8-14 上午11:26
     * @param pushConfigReqVO
     * @param userId
     */
    void pushOneConfig(PushConfigReqVO pushConfigReqVO,int userId);

    /**
     * 推送任务详情通过
     * @author oulc
     * @date 18-8-14 下午2:52
     * @param taskId 任务主键
     * @param envId 任务主键
     */
    void pushTaskPass(int taskId, int envId);

    /**
     * 处理推送定时任务
     * @author oulc
     * @date 18-8-14 下午5:22
     * @param taskId 任务主键
     */
    void dealPushTimeTask(int taskId);

    void pushConfigToZookeeper(AppInfoVO appInfoVO, String insName, ZKConfigPushVO zkConfigPushVO);

}
