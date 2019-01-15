package com.asiainfo.configcenter.center.entity.complex;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 用户表 用户扩展信息表 用户角色关联表 角色表
 */
@Entity
@Table(name="cc_user", schema = "config_center")
@SecondaryTables(value = {
        @SecondaryTable(name = "cc_user_ext_info", schema = "config_center"),
        @SecondaryTable(name = "cc_user_role_rel", schema = "config_center"),
        @SecondaryTable(name = "cc_role", schema = "config_center")
})
public class CXCcUserInfoEntity {
    private int userId;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String roleName;
    private int userStatus;
    private Timestamp createTime;



    @Id
    @Column(name="user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name="role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name="user_status")
    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    @Column(name="create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
