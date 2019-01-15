package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXAppInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.IAppSV;
import com.asiainfo.configcenter.center.vo.app.AppReqVO;
import com.asiainfo.configcenter.center.vo.app.AppUserRelVO;
import com.asiainfo.configcenter.center.vo.app.AppUserReqVO;
import com.asiainfo.configcenter.center.vo.app.GetAppReqVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * app控制器
 * Created by bawy on 2018/7/24 11:13.
 */
@Controller
@RequestMapping("/center/app")
public class AppController extends BaseController {

    @Resource
    private IAppSV appSV;

    @ApiOperation(value = "创建App", notes = "用户创建新的App")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "app", value = "创建App请求参数，名称必填、描述选填、id不需要填",required = true, dataType = "AppReqVO"),
    })
    @RequestMapping(value = "/createApp",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> createApp(@RequestBody AppReqVO app){
        Result<String> result = new Result<>();
        appSV.createApp(app, getCurrentUser().getId());
        result.setResult("创建App成功");
        return result;
    }

    @ApiOperation(value = "获取用户加入的所有App", notes = "获取当前登录用户已经加入的所有App的信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "appName", value = "app名称", defaultValue = "", dataType = "String"),
    })
    @RequestMapping(value = "/getMyApps",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<CXAppInfoEntity>> getMyApps(@RequestBody GetAppReqVO getAppReqVO){
        Result<List<CXAppInfoEntity>> result = new Result<>();
        result.setResult(appSV.getMyApps(getAppReqVO.getAppName(), getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "删除App", notes = "删除App")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "app", value = "删除App请求参数，appId必填",required = true, dataType = "AppReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/delApp",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> delApp(@RequestBody AppReqVO app){
        Result<String> result = new Result<>();
        appSV.delApp(app.getAppId(), getCurrentUser().getId());
        result.setResult("删除App成功");
        return result;
    }

    @ApiOperation(value = "修改App", notes = "修改App")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "app", value = "修改App的请求参数，appId必填、名称、描述至少填一个",required = true, dataType = "AppReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/modApp",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modApp(@RequestBody AppReqVO app){
        Result<String> result = new Result<>();
        appSV.modApp(app, getCurrentUser().getId());
        result.setResult("修改成功");
        return result;
    }

    @ApiOperation(value = "获取App所有用户", notes = "获取App包含的所有用户,appId必填,用户昵称和角色Id选填")
    @AppPermission
    @RequestMapping(value = "/getAppUsers",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<AppUserRelVO>> getAppUsers(@RequestBody PageRequestContainer<AppUserReqVO> requestContainer){
        Result<PageResultContainer<AppUserRelVO>> result = new Result<>();
        result.setResult(appSV.getAppUsers(requestContainer));
        return result;
    }

    @ApiOperation(value = "用户加入应用", notes = "给应用添加用户并设置角色")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "appUser", value = "App添加用户的请求参数，appId、userId、roleId必填",required = true, dataType = "AppUserReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> addUser(@RequestBody AppUserReqVO appUser){
        Result<String> result = new Result<>();
        appSV.addUser(appUser, getCurrentUser().getId());
        result.setResult("添加用户成功");
        return result;
    }

    @ApiOperation(value = "修改应用用户角色", notes = "修改用户在此应用中的角色")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "appUser", value = "修改App用户的请求参数，appId、userId、roleId必填",required = true, dataType = "AppUserReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/modUser",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modUser(@RequestBody AppUserReqVO appUser){
        Result<String> result = new Result<>();
        appSV.modUser(appUser, getCurrentUser().getId());
        result.setResult("修改用户角色成功");
        return result;
    }

    @ApiOperation(value = "用户移除应用", notes = "将用户移除应用")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "appUser", value = "App移除用户的请求参数，appId、userId必填",required = true, dataType = "AppUserReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/delUser",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> delUser(@RequestBody AppUserReqVO appUser){
        Result<String> result = new Result<>();
        appSV.delUser(appUser, getCurrentUser().getId());
        result.setResult("删除用户成功");
        return result;
    }
}
