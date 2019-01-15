package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.AppPermission;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.complex.CXCcConfigFileEntity;
import com.asiainfo.configcenter.center.service.interfaces.IConfigFileSV;
import com.asiainfo.configcenter.center.vo.configfile.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 配置文件控制层类
 * Created by oulc on 2018/7/31.
 * 配置文件controller
 */
@Controller
@RequestMapping(value = "/center/configFile")
public class ConfigFileController extends BaseController {
    @Resource
    private IConfigFileSV iConfigFileSV;

    @ApiOperation(value = "上传单个配置文件", notes = "上传单个配置文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "uploadConfigFile", value = "配置文件内容", required = true, dataType = "UpOneConfigFileVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/upConfigFileAndCreate",method = RequestMethod.POST)
    public Result<String> upConfigFileAndCreate(@RequestBody UpOneConfigFileVO uploadConfigFile){
        Result<String> result = new Result<>();
        iConfigFileSV.upOneConfigFileForCreate(uploadConfigFile,getCurrentUser().getId());
        result.setResult("上传配置文件已加入待提交任务列表");
        return result;
    }


    @ApiOperation(value = "批量上传配置文件(zip)", notes = "批量上传配置文件(zip)")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "upManyConfigFileVO", value = "配置文件内容", required = true, dataType = "UpManyConfigFileVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/upConfigFileZipAndCreate",method = RequestMethod.POST)
    public Result<String> upConfigFileZipAndCreate(@RequestBody UpManyConfigFileVO upManyConfigFileVO){
        Result<String> result = new Result<>();
        iConfigFileSV.upManyConfigFileForCreate(upManyConfigFileVO,getCurrentUser().getId());
        result.setResult("批量上传配置文件已加入待提交任务列表");
        return result;
    }

    @ApiOperation(value = "上传配置文件进行更新", notes = "上传配置文件进行更新")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "upOneConfigFileForUpdateVO", value = "配置文件内容", required = true, dataType = "UpOneConfigFileForUpdateVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/upConfigFileAndUpdate",method = RequestMethod.POST)
    public Result<String> upConfigFileAndUpdate(@RequestBody UpOneConfigFileForUpdateVO upOneConfigFileForUpdateVO){
        Result<String> result = new Result<>();
        iConfigFileSV.upConfigFileForUpdate(upOneConfigFileForUpdateVO,getCurrentUser().getId());
        result.setResult("上传配置文件已加入待提交任务列表");
        return result;
    }

    @ApiOperation(value = "修改配置文件刷新策略", notes = "上传配置文件进行更新")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "changeStrategyReq", value = "修改刷新策略请求参数", required = true, dataType = "ChangeStrategyReqVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/changeStrategy",method = RequestMethod.POST)
    public Result<String> changeStrategy(@RequestBody ChangeStrategyReqVO changeStrategyReq){
        Result<String> result = new Result<>();
        iConfigFileSV.changeStrategy(changeStrategyReq,getCurrentUser().getId());
        result.setResult("配置文件刷新策略变更已加入待提交任务列表");
        return result;
    }


    @ApiOperation(value = "查询配置文件", notes = "查询配置文件")
    @ApiImplicitParams(value = {
            /*@ApiImplicitParam(paramType = "body", name = "pageRequestContainer", value = "查询条件", required = true,
                    dataType = "PageRequestContainer"),*/
    })
    @AppPermission
    @RequestMapping(value = "/getConfigFiles",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CXCcConfigFileEntity>> getConfigFiles(
            @RequestBody PageRequestContainer<QueryConfigFileVO> pageRequestContainer){
        Result<PageResultContainer<CXCcConfigFileEntity>> result = new Result<>();
        result.setResult(iConfigFileSV.getConfigFiles(pageRequestContainer));
        return result;
    }


    @ApiOperation(value = "删除配置文件", notes = "删除配置文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "delConfigFileVO", value = "配置文件信息", required = true, dataType = "DelConfigFileVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/delConfigFile",method = RequestMethod.POST)
    public Result<String> delConfigFile(@RequestBody DelConfigFileVO delConfigFileVO){
        Result<String> result = new Result<>();
        iConfigFileSV.deleteConfigFile(delConfigFileVO,getCurrentUser().getId());
        result.setResult("删除配置文件已加入待提交任务列表");
        return result;
    }


    @ApiOperation(value = "复制配置文件", notes = "originEnvId:源环境,envId:目标环境,configIds:被复制配置文件主键")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "copyConfigFileVO", value = "配置文件信息", required = true, dataType = "CopyConfigFileVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/copyConfigFile",method = RequestMethod.POST)
    public Result<String> copyConfigFile(@RequestBody CopyConfigFileVO copyConfigFileVO){
        Result<String> result = new Result<>();
        iConfigFileSV.copyConfigFile(copyConfigFileVO,getCurrentUser().getId());
        result.setResult("复制配置文件已加入待提交任务列表");
        return result;
    }


    @ApiOperation(value = "读取配置文件内容", notes = "配置文件信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryConfigContentsVO", value = "配置文件信息", required = true, dataType = "QueryConfigContentsVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/getConfigFilesContent",method = RequestMethod.POST)
    public Result<List<ConfigFileContent>> getConfigFilesContent(@RequestBody QueryConfigContentsVO queryConfigContentsVO){
        Result<List<ConfigFileContent>> result = new Result<>();
        result.setResult(iConfigFileSV.getConfigContents(queryConfigContentsVO));
        return result;
    }


    @ApiOperation(value = "在线编辑配置文件保存", notes = "在线编辑配置文件保存")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "editConfigContentVO", value = "配置文件信息", required = true, dataType = "EditConfigContentVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/saveConfigFileContent",method = RequestMethod.POST)
    public Result<String> saveConfigFileContent(@RequestBody EditConfigContentVO editConfigContentVO){
        Result<String> result = new Result<>();
        iConfigFileSV.editConfigContent(editConfigContentVO,getCurrentUser().getId());
        result.setResult("修改配置文件已加入待提交任务列表");
        return result;
    }


    @ApiOperation(value = "获取配置文件历史版本信息", notes = "获取配置文件历史版本信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryFileHisVO", value = "配置文件信息", required = true, dataType = "QueryFileHisVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/getFileHis",method = RequestMethod.POST)
    public Result<List<ConfigHisVo>> getFileHis(@RequestBody QueryFileHisVO queryFileHisVO){
        Result<List<ConfigHisVo>> result = new Result<>();
        result.setResult(iConfigFileSV.getConfigFileHis(queryFileHisVO.getConfigId(),queryFileHisVO.getEnvId()));
        return result;
    }

    @ApiOperation(value = "获取指定配置文件最新版本信息", notes = "获取指定配置文件最新版本信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "queryFileHis", value = "配置文件信息", required = true, dataType = "QueryFileHisVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/getLastVersion",method = RequestMethod.POST)
    public Result<ConfigFileContent> getLastVersion(@RequestBody QueryFileHisVO queryFileHis){
        Result<ConfigFileContent> result = new Result<>();
        result.setResult(iConfigFileSV.getLastVersion(queryFileHis.getConfigId(),queryFileHis.getEnvId()));
        return result;
    }

    @ApiOperation(value = "获取指定配置文件当前", notes = "获取指定配置文件最新版本信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "tempFileReq", value = "配置文件信息", required = true, dataType = "TempFileReqVO"),
    })
    @AppPermission
    @ResponseBody
    @RequestMapping(value = "/getTempFileContent",method = RequestMethod.POST)
    public Result<ConfigFileContent> getTempFileContent(@RequestBody TempFileReqVO tempFileReq){
        Result<ConfigFileContent> result = new Result<>();
        result.setResult(iConfigFileSV.getTempFileContent(tempFileReq.getEnvId(), tempFileReq.getTaskDetailId()));
        return result;
    }

    @ApiOperation(value = "获取应用制定环境的所有配置文件", notes = "获取应用制定环境的所有配置文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "envAllConfigFileRepVO", value = "环境信息", required = true, dataType = "EnvAllConfigFileRepVO"),
    })
    @RequestMapping(value = "/getAllConfigFile",method = RequestMethod.GET)
    public String getTempFileContent(@RequestParam("envId")int envId, HttpServletResponse response){
        File file = iConfigFileSV.downLoadEnvConfigFiles(envId,getCurrentUser().getId());

        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
