package com.asiainfo.configcenter.center.vo.audit;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 增加审核策略步骤请求参数对象
 * Created by bawy on 2018/8/23 16:16.
 */
public class AddAuditStrategyStepReqVO implements BaseAppReqVO {

    private static final long serialVersionUID = -3843270542340225991L;

    private int envId;
    private int orgId;
    private int roleId;

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

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
