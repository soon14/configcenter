package com.asiainfo.configcenter.center.vo.org;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 组织用户关系VO对象
 * Created by bawy on 2018/8/17 16:04.
 */
public class OrgUserRelVO implements Serializable{

    private static final long serialVersionUID = 9115551477893385071L;
    private int id;
    private int orgId;
    private int userId;
    private String orgName;
    private String nickname;
    private String email;
    private String roleName;
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
