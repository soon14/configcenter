package com.asiainfo.configcenter.center.vo.org;

/**
 * 组织详细信息
 * Created by bawy on 2018/8/21 9:04.
 */
public class OrgDetailInfoVO extends BaseOrgVO {

    private static final long serialVersionUID = 1527687130864882354L;

    private String description;
    private String email;
    private int leader;
    private String leaderName;
    private String parentOrgName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLeader() {
        return leader;
    }

    public void setLeader(int leader) {
        this.leader = leader;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }
}
