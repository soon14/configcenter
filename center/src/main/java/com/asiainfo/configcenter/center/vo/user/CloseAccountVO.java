package com.asiainfo.configcenter.center.vo.user;

import java.io.Serializable;

public class CloseAccountVO implements Serializable {
    private int[] accountIds;

    public int[] getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(int[] accountIds) {
        this.accountIds = accountIds;
    }
}
