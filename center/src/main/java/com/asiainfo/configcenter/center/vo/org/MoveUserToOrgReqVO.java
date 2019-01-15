package com.asiainfo.configcenter.center.vo.org;

/**
 * 调整人员组织请求参数对象
 * Created by bawy on 2018/8/17 15:55.
 */
public class MoveUserToOrgReqVO implements BaseOrgReqVO{

    private static final long serialVersionUID = -7679598713901881240L;

    private int orgId;
    private int userId;
    private int targetOrgId;

    @Override
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTargetOrgId() {
        return targetOrgId;
    }

    public void setTargetOrgId(int targetOrgId) {
        this.targetOrgId = targetOrgId;
    }
}
