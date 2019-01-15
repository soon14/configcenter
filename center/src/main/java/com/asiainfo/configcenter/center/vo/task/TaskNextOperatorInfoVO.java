package com.asiainfo.configcenter.center.vo.task;

import com.asiainfo.configcenter.center.vo.audit.AuditStrategyStepVO;
import com.asiainfo.configcenter.center.vo.org.OrgUserVO;

import java.io.Serializable;
import java.util.List;

/**
 * 任务下一操作人员信息对象
 * Created by bawy on 2018/8/24 16:29.
 */
public class TaskNextOperatorInfoVO implements Serializable{

    private static final long serialVersionUID = 892631851898714491L;

    private AuditStrategyStepVO strategyStep;
    private List<OrgUserVO> orgUsers;

    public AuditStrategyStepVO getStrategyStep() {
        return strategyStep;
    }

    public void setStrategyStep(AuditStrategyStepVO strategyStep) {
        this.strategyStep = strategyStep;
    }

    public List<OrgUserVO> getOrgUsers() {
        return orgUsers;
    }

    public void setOrgUsers(List<OrgUserVO> orgUsers) {
        this.orgUsers = orgUsers;
    }
}
