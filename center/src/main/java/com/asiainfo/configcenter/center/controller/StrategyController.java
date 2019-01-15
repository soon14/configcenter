package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigUpdateStrategyEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.vo.app.AppReqVO;
import com.asiainfo.configcenter.center.vo.strategy.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/8/9.
 * 策略controller
 */
@Controller
@RequestMapping(value = "/center/strategy")
public class StrategyController extends BaseController{
    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Resource
    private IStrategyStepFieldSV iStrategyStepFieldSV;

    @Resource
    private IStrategyStepConstructorSV iStrategyStepConstructorSV;

    @Resource
    private IStrategyStepSV iStrategyStepSV;

    @Resource
    private IStrategyStepMethodSV iStrategyStepMethodSV;

    @ApiOperation(value = "创建/更新配置更新策略", notes = "创建/更新配置更新策略")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "createConfigUpdateStrategyReqVO", value = "策略信息", required = true, dataType = "CreateConfigUpdateStrategyReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/createUpdateStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> createUpdateStrategy(@RequestBody CreateConfigUpdateStrategyReqVO createConfigUpdateStrategyReqVO){
        Result<Integer> result = new Result<>();
        result.setResult(iConfigUpdateStrategySV.createConfigUpdateStrategy(createConfigUpdateStrategyReqVO,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "创建更新策略(类变量)", notes = "创建更新策略(类变量)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "createStrategyFieldReqVO", value = "类变量更新策略信息", required = true, dataType = "CreateStrategyFieldReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/createStrategyField",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> createStrategyField(@RequestBody CreateStrategyFieldReqVO createStrategyFieldReqVO){
        Result<Integer> result = new Result<>();
        result.setResult(iStrategyStepFieldSV.createStrategyField(createStrategyFieldReqVO,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "创建更新策略(方法)", notes = "创建更新策略(方法)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "createStrategyMethodReqVO", value = "类变量更新策略信息", required = true, dataType = "CreateStrategyMethodReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/createStrategyMethod",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> createStrategyMethod(@RequestBody CreateStrategyMethodReqVO createStrategyMethodReqVO){
        Result<Integer> result = new Result<>();
        result.setResult(iStrategyStepMethodSV.createStrategyMethod(createStrategyMethodReqVO,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "创建更新策略(构造器)", notes = "创建更新策略(构造器)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "createStrategyConstructorReqVO", value = "类变量更新策略信息", required = true, dataType = "CreateStrategyConstructorReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/createStrategyConstructor",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> createStrategyConstructor(@RequestBody CreateStrategyConstructorReqVO createStrategyConstructorReqVO){
        Result<Integer> result = new Result<>();
        result.setResult(iStrategyStepConstructorSV.createStrategyConstructor(createStrategyConstructorReqVO,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "更新策略步骤（步数、参数值）", notes = "更新策略步骤（步数、参数值）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "updateStrategyStepNumVO", value = "策略步骤信息", required = true, dataType = "UpdateStrategyStepNumVO"),
    })
    @AppPermission
    @RequestMapping(value = "/updateStrategyStepNums",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updateStrategyStepNums(@RequestBody UpdateStrategyStepNumVO updateStrategyStepNumVO){
        Result<String> result = new Result<>();
        iStrategyStepSV.updateStrategyStepNums(updateStrategyStepNumVO,getCurrentUser().getId());
        result.setResult("更新成功");
        return result;
    }

    @ApiOperation(value = "查询策略", notes = "查询策略")
    @ApiImplicitParams(value = {
            /*@ApiImplicitParam(paramType = "body", name = "updateStrategyStepNumVO", value = "策略步骤信息", required = true, dataType = "UpdateStrategyStepNumVO"),*/
    })
    @AppPermission
    @RequestMapping(value = "/queryStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CXCcConfigUpdateStrategyEntity>> updateStrategyStepNums(@RequestBody PageRequestContainer<QueryUpdateStrategyReqVO> pageRequestContainer){
        Result<PageResultContainer<CXCcConfigUpdateStrategyEntity>> result = new Result<>();
        result.setResult(iConfigUpdateStrategySV.findConfigUpdateStrategy(pageRequestContainer));
        return result;
    }


    @ApiOperation(value = "根据策略主键查询策略所有信息(包括步骤)", notes = "根据策略主键查询策略所有信息(包括步骤)")
    @ApiImplicitParams(value = {
           @ApiImplicitParam(paramType = "body", name = "queryStrategyAllReqVo", value = "策略信息", required = true, dataType = "QueryStrategyAllReqVo"),
    })
    @AppPermission
    @RequestMapping(value = "/getUpdateStrategyAllInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<ConfigUpdateStrategyVO> getUpdateStrategyAllInfo(@RequestBody QueryStrategyAllReqVo queryStrategyAllReqVo){
        Result<ConfigUpdateStrategyVO> result = new Result<>();
        result.setResult(iConfigUpdateStrategySV.getConfigUpdateStrategyAllById(queryStrategyAllReqVo.getStrategyId(),queryStrategyAllReqVo.getAppId()));
        return result;
    }

    @ApiOperation(value = "删除策略步骤", notes = "删除策略步骤")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "deleteStrategyStepReqVO", value = "策略步骤信息", required = true, dataType = "DeleteStrategyStepReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/deleteStrategyStep",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteStrategyStep(@RequestBody DeleteStrategyStepReqVO deleteStrategyStepReqVO){
        Result<String> result = new Result<>();
        iStrategyStepSV.deleteStrategyStep(deleteStrategyStepReqVO.getStrategyId(),deleteStrategyStepReqVO.getStrategyStepId(),deleteStrategyStepReqVO.getAppId(),getCurrentUser().getId());
        result.setResult("删除策略步骤成功");
        return result;
    }

    @ApiOperation(value = "删除配置更新策略", notes = "删除配置更新策略")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "deleteConfigUpdateStrategyReqVO", value = "策略步骤信息", required = true, dataType = "DeleteConfigUpdateStrategyReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/deleteConfigUpdateStrategy",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteConfigUpdateStrategy(@RequestBody DeleteConfigUpdateStrategyReqVO deleteConfigUpdateStrategyReqVO){
        Result<String> result = new Result<>();
        iConfigUpdateStrategySV.deleteConfigUpdateStrategy(deleteConfigUpdateStrategyReqVO,getCurrentUser().getId());
        result.setResult("删除配置更新策略");
        return result;
    }

    @ApiOperation(value = "获取刷新策略名称", notes = "获取刷新策略名称")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryStrategyAllReq", value = "获取刷新策略名称请求参数", required = true, dataType = "QueryStrategyAllReqVo"),
    })
    @AppPermission
    @RequestMapping(value = "/getStrategyName",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> getStrategyName(@RequestBody QueryStrategyAllReqVo queryStrategyAllReq){
        Result<String> result = new Result<>();
        result.setResult(iConfigUpdateStrategySV.getStrategyName(queryStrategyAllReq));
        return result;
    }

    @ApiOperation(value = "查询用户是否有新增更新策略的权限", notes = "查询用户是否有新增更新策略的权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryStrategyAllReq", value = "获取刷新策略名称请求参数", required = true, dataType = "QueryStrategyAllReqVo"),
    })
    @RequestMapping(value = "/checkUserPermission",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> checkUserPermission(@RequestBody AppReqVO appReq){
        Result<Boolean> result = new Result<>();
        result.setResult(iConfigUpdateStrategySV.checkUserHasCreatePermission(appReq.getAppId(),getCurrentUser().getId()));
        return result;
    }


}
