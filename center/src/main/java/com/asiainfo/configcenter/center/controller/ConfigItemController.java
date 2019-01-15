package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IConfigItemSV;
import com.asiainfo.configcenter.center.vo.configItem.*;
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
 * 配置项控制层
 * Created by bawy on 2018/7/31 17:23.
 */
@Controller
@RequestMapping("/center/configItem")
public class ConfigItemController extends BaseController {

    @Resource
    private IConfigItemSV configItemSV;

    @ApiOperation(value = "查询环境配置项", notes = "根据条件查询指定环境配置项，环境Id必填、配置项名称、创建人、最后修改时间范围选填")
    @AppPermission
    @RequestMapping(value = "/getConfigItems",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<ConfigItemVO>> register(@RequestBody PageRequestContainer<ConfigItemQueryReqVO> requestContainer){
        Result<PageResultContainer<ConfigItemVO>> result = new Result<>();
        result.setResult(configItemSV.getConfigItemsByCondition(requestContainer));
        return result;
    }

    @ApiOperation(value = "新增配置项", notes = "新增配置项，环境标识、配置项key、配置项value必填，刷新策略标识、配置项描述选填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItem", value = "新增配置项请求参数", required = true, dataType = "ConfigItemReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/addConfigItem",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> addConfigItem(@RequestBody ConfigItemReqVO configItem){
        Result<String> result = new Result<>();
        result.setResult(configItemSV.addConfigItem(configItem, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "修改配置项", notes = "修改配置项，环境标识、配置项key、配置项value必填，刷新策略标识、配置项描述选填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItem", value = "修改配置项请求参数", required = true, dataType = "ConfigItemReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/modConfigItem",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modConfigItem(@RequestBody ConfigItemReqVO configItem){
        Result<String> result = new Result<>();
        result.setResult(configItemSV.modConfigItem(configItem, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "复制配置项", notes = "复制配置项，环境标识、源环境标识必填、配置项标识列表必须大于0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItemCopyReq", value = "复制配置项请求参数", required = true, dataType = "ConfigItemCopyReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/copyConfigItem",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> copyConfigItem(@RequestBody ConfigItemCopyReqVO configItemCopyReq){
        Result<String> result = new Result<>();
        result.setResult(configItemSV.copyConfigItem(configItemCopyReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "删除配置项", notes = "删除配置项，环境标识、配置项key必填，其他属性无需传入")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItem", value = "删除配置项请求参数", required = true, dataType = "ConfigItemReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/delConfigItem",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> delConfigItem(@RequestBody ConfigItemReqVO configItem){
        Result<String> result = new Result<>();
        result.setResult(configItemSV.delConfigItem(configItem, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取配置项历史数据", notes = "查询指定配置项的历史数据（不分页，只显示前20条历史数据），环境标识、配置项标识必填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItemHisReq", value = "查询配置项历史数据请求参数", required = true, dataType = "ConfigItemHisReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getConfigItemHis",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ConfigItemHisVO>> getConfigItemHis(@RequestBody ConfigItemHisReqVO configItemHisReq){
        Result<List<ConfigItemHisVO>> result = new Result<>();
        result.setResult(configItemSV.getConfigItemHis(configItemHisReq));
        return result;
    }

    @ApiOperation(value = "获取配置项指定版本数据", notes = "获取指定配置项最新版本的数据，环境标识、配置项标识必填，版本ID选填（不填即为最新版本）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "configItemHisReq", value = "查询配置项历史数据请求参数", required = true, dataType = "ConfigItemHisReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getVersionContent",method = RequestMethod.POST)
    @ResponseBody
    public Result<ConfigItemHisVO> getVersionContent(@RequestBody ConfigItemHisReqVO configItemHisReq){
        Result<ConfigItemHisVO> result = new Result<>();
        result.setResult(configItemSV.getVersionContent(configItemHisReq));
        return result;
    }
}