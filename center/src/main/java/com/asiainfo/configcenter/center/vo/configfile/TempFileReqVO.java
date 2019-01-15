package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 获取临时文件
 * Created by bawy on 2018/8/9 16:15.
 */
public class TempFileReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -8029634813438277645L;
    private int envId;
    private int taskDetailId;


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

    public int getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(int taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

}
