package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXCcOperationLogEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOperationLogSV;
import com.asiainfo.configcenter.center.vo.system.OperateLogReqVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/7/20.
 */
@Controller
@RequestMapping(value = "/center/log")
public class OperationLogController extends BaseController {

    @Resource
    private IOperationLogSV iOperationLogSV;

    @ApiOperation(value = "查询通知消息", notes = "查询通知消息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "pageRequestContainer", value = "查询条件", required = true, dataType = "PageRequestContainer"),
    })
    @RequestMapping(value = "/getOperateLog",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CXCcOperationLogEntity>> getOperateLog(@RequestBody PageRequestContainer<OperateLogReqVO> pageRequestContainer){
        Result<PageResultContainer<CXCcOperationLogEntity>> result = new Result<>();
        result.setResult(iOperationLogSV.getOperateLog(pageRequestContainer,getCurrentUser().getId()));
        return result;
    }
}
