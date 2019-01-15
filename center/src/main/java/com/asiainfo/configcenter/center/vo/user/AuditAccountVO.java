package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

public class AuditAccountVO implements Serializable{
    private int[] accountIds;
    private int operate;//1:审核通过 2:审核不通过

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public int[] getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(int[] accountIds) {
        this.accountIds = accountIds;
    }
}
