package com.asiainfo.configcenter.center.vo.configfile;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * Created by oulc on 2018/8/7.
 */
public class QueryConfigContentsVO implements BaseAppReqVO {
    private int envId;
    private QueryConfigContentVO []queryConfigContentVO;

    @Override
    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public QueryConfigContentVO[] getQueryConfigContentVO() {
        return queryConfigContentVO;
    }

    public void setQueryConfigContentVO(QueryConfigContentVO[] queryConfigContentVO) {
        this.queryConfigContentVO = queryConfigContentVO;
    }

    @Override
    public int getAppId() {
        return 0;
    }
}
