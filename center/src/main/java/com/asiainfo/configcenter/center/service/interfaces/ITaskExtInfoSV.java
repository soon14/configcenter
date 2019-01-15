package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.entity.CcTaskExtInfoEntity;

public interface ITaskExtInfoSV {

    /**
     * 查询任务扩展信息
     * @author oulc
     * @date 18-8-14 下午3:36
     * @param taskId 任务主键
     * @param extInfoKey 扩展信息key
     * @return 任务扩展信息实体
     */
    CcTaskExtInfoEntity findTaskExtInfoByTaskIdAndKey(int taskId,String extInfoKey);

    /**
     * 查询任务扩展信息(校验)
     * @author oulc
     * @date 18-8-14 下午3:36
     * @param taskId 任务主键
     * @param extInfoKey 扩展信息key
     * @return 任务扩展信息实体
     */
    CcTaskExtInfoEntity findTaskExtInfoByTaskIdAndKeyCheck(int taskId,String extInfoKey);

}
