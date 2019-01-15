package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.CcInstanceEntity;
import com.asiainfo.configcenter.center.po.InstanceConfigPojo;
import com.asiainfo.configcenter.center.service.interfaces.IInstanceSV;
import com.asiainfo.configcenter.center.vo.instance.DeleteInsVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInsByConfigReqVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInsConfigInfoReqVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInstanceReqVO;
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
 * Created by oulc on 2018/7/26.
 * 实例controller
 */
@Controller
@RequestMapping(value = "/center/ins")
public class InstanceController extends BaseController {
    @Resource
    private IInstanceSV iInstanceSV;

    @ApiOperation(value = "查询环境下的实例", notes = "查询环境下的实例")
    @ApiImplicitParams(value = {
            /*@ApiImplicitParam(paramType = "body", name = "pageRequestContainer", value = "查询条件", required = true,
                    dataType = "PageRequestContainer"),*/
    })
    @RequestMapping(value = "/getIns",method = RequestMethod.POST)
    @AppPermission
    @ResponseBody
    public Result<PageResultContainer<CcInstanceEntity>> getIns(
            @RequestBody PageRequestContainer<QueryInstanceReqVO> pageRequestContainer){
        Result<PageResultContainer<CcInstanceEntity>> result = new Result<>();
        result.setResult(iInstanceSV.findInstance(pageRequestContainer));
        return result;
    }

    @ApiOperation(value = "获取实例的配置信息", notes = "只需要环境主键和实例主键")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryInsConfigInfoReqVO", value = "环境主键", required = true, dataType = "QueryInsConfigInfoReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getInsConfigInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<InstanceConfigPojo[]> getInsConfigInfo(@RequestBody QueryInsConfigInfoReqVO queryInsConfigInfoReqVO){
        Result<InstanceConfigPojo[]> result = new Result<>();
        result.setResult(iInstanceSV.getInstanceConfigInfo(queryInsConfigInfoReqVO.getInsId()));
        return result;
    }

    @ApiOperation(value = "删除实例", notes = "删除实例")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "deleteInsVO", value = "环境主键", required = true, dataType = "DeleteInsVO"),
    })
    @AppPermission
    @RequestMapping(value = "/deleteIns",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteIns(@RequestBody DeleteInsVO deleteInsVO){
        Result<String> result = new Result<>();
        iInstanceSV.deleteInstance(deleteInsVO.getInsId());
        result.setResult("删除实例成功");
        return result;
    }

    //根据配置文件查询实例
    @ApiOperation(value = "根据配置文件查询实例", notes = "envId:环境主键;configType:配置类型，1:配置文件,2:配置类型;configId:配置主键;configVersion:配置版本")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryInsByConfigReqVO", value = "配置信息", required = true, dataType = "QueryInsByConfigReqVO"),
    })
    @AppPermission
    @RequestMapping(value = "/getInsByConfig",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CcInstanceEntity>> getInsByConfig(@RequestBody PageRequestContainer<QueryInsByConfigReqVO> pageRequestContainer){
        Result<PageResultContainer<CcInstanceEntity>> result = new Result<>();
        result.setResult(iInstanceSV.getInstanceByConfig(pageRequestContainer));
        return result;
    }

}
