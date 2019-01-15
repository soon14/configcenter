package com.asiainfo.configcenter.center.vo.org;

/**
 * 删除组织请求参数对象
 * Created by bawy on 2018/8/17 15:42.
 */
public class DelOrgReqVO implements BaseOrgReqVO {

    private static final long serialVersionUID = -7070478602277730638L;

    private int orgId;

    @Override
    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
