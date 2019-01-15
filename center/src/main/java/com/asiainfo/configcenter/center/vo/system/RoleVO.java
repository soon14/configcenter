package com.asiainfo.configcenter.center.vo.system;

import java.io.Serializable;

/**
 * 组织角色VO对象
 * Created by bawy on 2018/8/21 10:54.
 */
public class RoleVO implements Serializable{

    private static final long serialVersionUID = -5937173482722383262L;

    private int roleId;
    private String roleName;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
