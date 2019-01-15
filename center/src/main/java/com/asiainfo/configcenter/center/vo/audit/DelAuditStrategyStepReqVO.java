package com.asiainfo.configcenter.center.vo.audit;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 增加审核策略步骤请求参数对象
 * Created by bawy on 2018/8/23 16:16.
 */
public class DelAuditStrategyStepReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -2689084419298091564L;

    private int envId;
    private int stepId;

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

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
}
