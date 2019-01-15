package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.entity.complex.CXCcOperationLogEntity;
import com.asiainfo.configcenter.center.vo.system.OperateLogReqVO;

/**
 * Created by oulc on 2018/7/20.
 */
public interface IOperationLogSV {
    /**
     *
     * @param pageRequestContainer
     * @param userId
     * @return
     * @author oulc
     * @date 2018/7/20 15:50
     */
    PageResultContainer<CXCcOperationLogEntity> getOperateLog(PageRequestContainer<OperateLogReqVO> pageRequestContainer, int userId);

}
