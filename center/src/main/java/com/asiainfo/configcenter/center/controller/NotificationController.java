package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXCcNotificationEntity;
import com.asiainfo.configcenter.center.service.interfaces.INotificationSV;
import com.asiainfo.configcenter.center.vo.system.ConsumeNotificationVO;
import com.asiainfo.configcenter.center.vo.system.NotificationVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/center/notify")
public class NotificationController extends BaseController{
    @Resource
    private INotificationSV iNotificationSV;

    @ApiOperation(value = "查询通知消息", notes = "查询通知消息")
    @ApiImplicitParams(value = {
            /*@ApiImplicitParam(paramType = "body", name = "pageRequestContainer", value = "查询条件", required = true,
                    dataType = "PageRequestContainer"),*/
    })
    @RequestMapping(value = "/getNotificationInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CXCcNotificationEntity>> getNotificationInfo(
            @RequestBody PageRequestContainer<NotificationVO> pageRequestContainer){
        Result<PageResultContainer<CXCcNotificationEntity>> result = new Result<>();
        result.setResult(iNotificationSV.getNotification(pageRequestContainer,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "消费消息", notes = "消费消息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "consumeNotificationVO", value = "消息主键", required = true,
                    dataType = "ConsumeNotificationVO"),
    })
    @RequestMapping(value = "/consumeNotification",method = RequestMethod.POST)
    @ResponseBody
        public Result<String> consumeNotification(@RequestBody ConsumeNotificationVO consumeNotificationVO){
            Result<String> result = new Result<>();
            iNotificationSV.consumeNotification(consumeNotificationVO.getId(),getCurrentUser().getId());
            result.setResult("此条消息已读");
            return result;
    }
}
