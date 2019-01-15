package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXCcAppEnvEntity;
import com.asiainfo.configcenter.center.service.interfaces.IAppEnvSV;
import com.asiainfo.configcenter.center.vo.env.AppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.DelAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.QueryAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.UpdateAppEnvReqVO;
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
 * Created by oulc on 2018/7/24.
 */
@Controller
@RequestMapping(value = "/center/appEnv")
public class AppEnvController extends BaseController{

    @Resource
    private IAppEnvSV iAppEnvSV;


    @ApiOperation(value = "创建项目环境", notes = "appId,envName必填，desc可填可不填，envId不需要填写")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "createAppEnvReqVO", value = "环境信息", required = true, dataType = "AppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/createAppEnv",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> createAppEnv(@RequestBody AppEnvReqVO createAppEnvReqVO){
        Result<String> result = new Result();
        iAppEnvSV.createAppEnv(createAppEnvReqVO,getCurrentUser().getId());
        result.setResult("创建环境成功");
        return result;
    }

    @ApiOperation(value = "更新项目环境", notes = "更新项目环境")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "updateAppEnvReqVO", value = "环境主键", required = true, dataType = "UpdateAppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/updateAppEnv",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updateAppEnv(@RequestBody UpdateAppEnvReqVO updateAppEnvReqVO){
        Result<String> result = new Result();
        iAppEnvSV.updateAppEnv(updateAppEnvReqVO);
        result.setResult("修改环境成功");
        return result;
    }

    @ApiOperation(value = "删除环境", notes = "删除环境")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "delAppEnvReqVO", value = "环境主键", required = true, dataType = "DelAppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/deleteAppEnv",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteAppEnv(@RequestBody DelAppEnvReqVO delAppEnvReqVO){
        Result<String> result = new Result();
        iAppEnvSV.deleteEnv(delAppEnvReqVO);
        result.setResult("删除环境成功");
        return result;
    }


    @ApiOperation(value = "查询环境", notes = "查询环境")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryAppEnvReqVO", value = "环境主键", required = true, dataType = "QueryAppEnvReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getAppEnv",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<CXCcAppEnvEntity>> getAppEnv(@RequestBody QueryAppEnvReqVO queryAppEnvReqVO){
        Result<List<CXCcAppEnvEntity>> result = new Result<>();
        result.setResult(iAppEnvSV.getEnvByAppId(queryAppEnvReqVO));
        return result;
    }


}
