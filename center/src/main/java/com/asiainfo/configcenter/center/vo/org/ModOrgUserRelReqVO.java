package com.asiainfo.configcenter.center.vo.org;

/**
 * 修改用户在组织中的角色请求参数
 * Created by bawy on 2018/8/17 16:11.
 */
public class ModOrgUserRelReqVO implements BaseOrgReqVO{

    private static final long serialVersionUID = -3386458217433224919L;

    private int orgId;
    private int userId;
    private int newRoleId;

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

    public int getNewRoleId() {
        return newRoleId;
    }

    public void setNewRoleId(int newRoleId) {
        this.newRoleId = newRoleId;
    }
}
