package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IConfigPushSV;
import com.asiainfo.configcenter.center.vo.configpush.PushConfigReqVO;
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
@RequestMapping("/center/push")
public class PushConfigController extends BaseController {
    @Resource
    private IConfigPushSV iConfigPushSV;

    @ApiOperation(value = "推送配置", notes = "推送配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "pushConfigReqVO", value = "推送信息", required = true, dataType = "PushConfigReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/pushConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> pushConfig(@RequestBody PushConfigReqVO pushConfigReqVO){
        Result<String> result = new Result<>();
        iConfigPushSV.pushOneConfig(pushConfigReqVO, getCurrentUser().getId());
        result.setResult("推送配置文件已加入待提交任务列表");
        return result;
    }
}
