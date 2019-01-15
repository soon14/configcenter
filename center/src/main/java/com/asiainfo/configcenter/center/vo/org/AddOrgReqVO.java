package com.asiainfo.configcenter.center.vo.org;

/**
 * 增加组织请求参数对象
 * Created by bawy on 2018/8/17 15:35.
 */
public class AddOrgReqVO implements BaseOrgReqVO{

    private static final long serialVersionUID = -1557201581391890946L;

    private int orgId;
    private String name;
    private String description;
    private Integer leader;
    private String email;

    @Override
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
