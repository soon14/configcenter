package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IHomeSV;
import com.asiainfo.configcenter.center.vo.home.AppInfo;
import com.asiainfo.configcenter.center.vo.home.HomePageRepVO;
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
@RequestMapping("/center/home")
public class HomePageController extends BaseController {

    @Resource
    private IHomeSV iHomeSV;

    @ApiOperation(value = "获取首页信息", notes = "获取首页信息")
    @ApiImplicitParams(value = {
            /*@ApiImplicitParam(paramType = "body", name = "app", value = "创建App请求参数，名称必填、描述选填、id不需要填",required = true, dataType = "AppReqVO"),*/
    })
    @RequestMapping(value = "/getHomePageData",method = RequestMethod.POST)
    @ResponseBody
    public Result<HomePageRepVO> getHomePageData(){
        Result<HomePageRepVO> result = new Result<>();
        result.setResult(iHomeSV.getHomePageData(getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "添加最近访问的项目", notes = "添加最近访问的项目")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name= "appInfo", value = "AppId必填，AppName不需要",required = true, dataType = "AppInfo")
    })
    @RequestMapping(value = "/addRecentApp",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> createRecentVisitedApp(@RequestBody AppInfo appInfo) {
        Result<String> result = new Result<>();
        iHomeSV.createRecentVisitedApp(appInfo.getAppId(), getCurrentUser().getId());
        result.setResult("添加用户最近访问应用成功");
        return result;
    }
}
