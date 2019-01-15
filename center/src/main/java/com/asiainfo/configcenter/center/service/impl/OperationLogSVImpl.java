package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.dao.mapper.OperationLogMapper;
import com.asiainfo.configcenter.center.dao.repository.OperationLogRepository;
import com.asiainfo.configcenter.center.entity.complex.CXCcOperationLogEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOperationLogSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.system.OperateLogReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oulc on 2018/7/20.
 */
@Service
public class OperationLogSVImpl implements IOperationLogSV {
    @Resource
    private OperationLogRepository operationLogRepository;

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private IUserSV userSV;
    @Override
    public PageResultContainer<CXCcOperationLogEntity> getOperateLog(PageRequestContainer<OperateLogReqVO> pageRequestContainer, int operator) {
        ValidateUtil.checkPageParam(pageRequestContainer);
        int userId = 0;
        if(userSV.isAdministrator(operator)){
            //是管理员
            userId = pageRequestContainer.getData().getUserId() == 0 ? operator:pageRequestContainer.getData().getUserId();

        }else{
            //不是管理员
            userId = operator;
        }

        byte operateType = pageRequestContainer.getData().getOperateType();
        int page = pageRequestContainer.getCurrentPage();
        int size = pageRequestContainer.getPageSize();

        String startDate = pageRequestContainer.getData().getStartDate()== 0 ? null : TimeUtil.timeFormat(pageRequestContainer.getData().getStartDate());
        String endDate = pageRequestContainer.getData().getEndDate() == 0 ? null : TimeUtil.timeFormat(pageRequestContainer.getData().getEndDate());

        List<CXCcOperationLogEntity> list = operationLogMapper.findByOperatorAndTypeAndTime(userId,operateType,startDate,endDate,page*size,size);
        long count = operationLogMapper.findCountByOperatorAndTypeAndTime(userId,operateType,startDate,endDate);

        PageResultContainer<CXCcOperationLogEntity> pageResultContainer = new PageResultContainer<>();
        pageResultContainer.setEntities(list);
        pageResultContainer.setCount(count);
        return pageResultContainer;
    }


}
