package com.asiainfo.configcenter.center.vo.audit;

import java.io.Serializable;

/**
 * 审核策略步骤VO对象
 * Created by bawy on 2018/8/23 14:27.
 */
public class AuditStrategyStepVO implements Serializable{

    private static final long serialVersionUID = -4164410764516697446L;

    private int stepId;
    private int orgId;
    private int roleId;
    private String orgName;
    private String roleName;
    private byte step;

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
    }
}
