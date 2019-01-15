package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionCheckSV;
import com.asiainfo.configcenter.center.service.interfaces.ITaskAuditStrategySV;
import com.asiainfo.configcenter.center.vo.audit.AddAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.AuditStrategyStepVO;
import com.asiainfo.configcenter.center.vo.audit.DelAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.audit.ModAuditStrategyStepReqVO;
import com.asiainfo.configcenter.center.vo.env.BaseAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.org.OrgTreeVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审核策略请求控制类
 * Created by bawy on 2018/8/23 14:11.
 */
@Controller
@RequestMapping("/center/audit")
public class TaskAuditStrategyController extends BaseController {

    @Resource
    private ITaskAuditStrategySV taskAuditStrategySV;

    @Resource
    private IPermissionCheckSV permissionCheckSV;


    @ApiOperation(value = "获取环境审核策略", notes = "获取环境审核策略")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "appEnvReq", value = "获取环境审核策略请求参数", required = true, dataType = "BaseAppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getAuditStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<AuditStrategyStepVO>> getAuditStrategy(@RequestBody BaseAppEnvReqVO appEnvReq){
        Result<List<AuditStrategyStepVO>> result = new Result<>();
        result.setResult(taskAuditStrategySV.getAuditStrategy(appEnvReq.getEnvId()));
        return result;
    }

    @ApiOperation(value = "增加审核策略步骤", notes = "增加审核策略步骤")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "addAuditStrategyStepReq", value = "增加审核策略步骤请求参数", required = true, dataType = "AddAuditStrategyStepReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/addAuditStrategyStep",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> addAuditStrategyStep(@RequestBody AddAuditStrategyStepReqVO addAuditStrategyStepReq){
        Result<Integer> result = new Result<>();
        result.setResult(taskAuditStrategySV.addAuditStrategyStep(addAuditStrategyStepReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "修改审核策略步骤", notes = "修改审核策略步骤")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "modAuditStrategyStepReq", value = "修改审核策略步骤请求参数", required = true, dataType = "ModAuditStrategyStepReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/modAuditStrategyStep",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modAuditStrategyStep(@RequestBody ModAuditStrategyStepReqVO modAuditStrategyStepReq){
        Result<String> result = new Result<>();
        result.setResult(taskAuditStrategySV.modAuditStrategyStep(modAuditStrategyStepReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "删除审核策略步骤", notes = "删除审核策略步骤")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "delAuditStrategyStepReq", value = "删除审核策略步骤请求参数", required = true, dataType = "DelAuditStrategyStepReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/delAuditStrategyStep",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> delAuditStrategyStep(@RequestBody DelAuditStrategyStepReqVO delAuditStrategyStepReq){
        Result<String> result = new Result<>();
        result.setResult(taskAuditStrategySV.delAuditStrategyStep(delAuditStrategyStepReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取组织树", notes = "获取所有组织对应的组织树")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "baseAppEnvReq", value = "获取组织树请求参数", required = true, dataType = "BaseAppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getOrgTree",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<OrgTreeVO>> getOrgTree(@RequestBody BaseAppEnvReqVO baseAppEnvReq){
        Result<List<OrgTreeVO>> result = new Result<>();
        result.setResult(taskAuditStrategySV.getOrgTree(baseAppEnvReq.getEnvId()));
        return result;
    }

    @ApiOperation(value = "权限校验", notes = "判断用户是否有修改权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "baseAppEnvReq", value = "获取组织树请求参数", required = true, dataType = "BaseAppEnvReqVO"),
    })
    @RequestMapping(value = "/checkPermission",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> checkPermission(@RequestBody BaseAppEnvReqVO baseAppEnvReq){
        Result<Boolean> result = new Result<>();
        result.setResult(permissionCheckSV.appPermissionCheck(baseAppEnvReq, getCurrentUser().getId(), "/center/audit/checkPermission"));
        return result;
    }

}
