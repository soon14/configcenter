package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IClientSV;
import com.asiainfo.configcenter.center.vo.client.ClientConfigFilesResp;
import com.asiainfo.configcenter.center.vo.client.ClientReqVO;
import com.asiainfo.configcenter.center.vo.client.ClientRespVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value="/center/client")
public class ClientController {

    @Resource
    private IClientSV iClientSV;

    @ApiOperation(value = "获取推送的配置文件内容和刷新策略", notes = "获取推送的配置文件内容和刷新策略")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "clientReqVO", value = "推送配置信息", required = true, dataType = "ClientReqVO"),
    })
    @ResponseBody
    @RequestMapping(value = "/getPushFileAndStrategy",method = RequestMethod.POST)
    public Result<List<ClientRespVO>> getPushFileAndStrategy(@RequestBody ClientReqVO clientReqVO) {
        Result<List<ClientRespVO>> result = new Result<>();
        result.setResult(iClientSV.getPushFileAndStrategy(clientReqVO));
        return result;
    }

    @ApiOperation(value = "下载配置项数据", notes = "根据推送的配置项信息(配置项版本号)，下载该配置项")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "clientConfigReqVO", value = "推送配置型信息", required = true, dataType = "ClientConfigItemReqVo")
    })
    @ResponseBody
    @RequestMapping(value = "/getPushConfigItem", method = RequestMethod.POST)
    public Result<List<ClientRespVO>> getPushConfigItem(@RequestBody ClientReqVO clientReqVO){
        Result<List<ClientRespVO>> result = new Result<>();
        result.setResult(iClientSV.getPushConfigItem(clientReqVO));
        return result;
    }

    @ApiOperation(value = "下载所有配置文件", notes = "根据appName,envName下载该环境下APP所有的配置文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "appName", value = "应用名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "envName", value = "环境名称", required = true, dataType = "String"),
    })
    @ResponseBody
    @RequestMapping(value = "/getAllFile", method = RequestMethod.GET)
    public Result<ClientConfigFilesResp> getAllFile(@RequestParam String appName, String envName) {
        Result<ClientConfigFilesResp> result = new Result<>();
        result.setResult(iClientSV.getAllFile(appName, envName));
        return result;
    }
}