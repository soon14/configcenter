package com.asiainfo.configcenter.center.vo.org;


import java.util.ArrayList;
import java.util.List;

/**
 * 组织树对象
 * Created by bawy on 2018/8/17 13:50.
 */
public class OrgTreeVO extends BaseOrgVO {

    private static final long serialVersionUID = -3910447879098233343L;

    private Integer leader;
    private List<OrgTreeVO> children;

    public OrgTreeVO(){
        children = new ArrayList<>();
    }

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public List<OrgTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<OrgTreeVO> children) {
        this.children = children;
    }

    public void addChild(OrgTreeVO orgTreeVO){
        children.add(orgTreeVO);
    }
}
