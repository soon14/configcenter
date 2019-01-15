package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.ICommonDataSV;
import com.asiainfo.configcenter.center.vo.system.SelectDataVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公共数据请求访问控制器
 * Created by bawy on 2018/7/23 14:06.
 */
@Controller
@RequestMapping("/center/data")
public class CommonDataController extends BaseController{

    @Resource
    private ICommonDataSV commonDataSV;

    @ApiOperation(value = "获取下拉框数据", notes = "根据key获取对应的下拉框列表数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "key", value = "下拉框数据关键字", defaultValue = "NOTIFICATION_TYPE",required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getSelectData",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectDataVO>> getSelectData(@RequestParam("key") String key){
        Result<List<SelectDataVO>> result = new Result<>();
        result.setResult(commonDataSV.getSelectData(key));
        return result;
    }

    @ApiOperation(value = "清除缓存", notes = "清理缓存")
    @RequestMapping(value = "/cleanCache",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> cleanCache(){
        Result<String> result = new Result<>();
        commonDataSV.cleanCache(getCurrentUser().getId());
        result.setResult("缓存清理成功");
        return result;
    }


}
