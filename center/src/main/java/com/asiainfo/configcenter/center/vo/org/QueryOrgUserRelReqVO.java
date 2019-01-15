package com.asiainfo.configcenter.center.vo.org;

/**
 * 查询组织用户关系请求参数
 * Created by bawy on 2018/8/17 16:02.
 */
public class QueryOrgUserRelReqVO implements BaseOrgReqVO{

    private static final long serialVersionUID = -5891187946638457795L;

    private int orgId;
    private String nickname;
    private String roleName;

    @Override
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
