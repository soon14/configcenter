package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOrgSV;
import com.asiainfo.configcenter.center.vo.org.*;
import com.asiainfo.configcenter.center.vo.system.RoleVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织控制层
 * Created by bawy on 2018/8/16 19:56.
 */
@Controller
@RequestMapping("/center/org")
public class OrgController extends BaseController{

    @Resource
    private IOrgSV orgSV;

    @ApiOperation(value = "获取所有一级组织", notes = "获取所有一级组织数据")
    @RequestMapping(value = "/getAllLevelOneOrg",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<CcOrgEntity>> getAllLevelOneOrg(){
        Result<List<CcOrgEntity>> result = new Result<>();
        result.setResult(orgSV.getAllLevelOneOrg());
        return result;
    }

    @ApiOperation(value = "获取用户所在一级组织", notes = "获取用户所在一级组织数据，如果是系统管理员角色则可以获取所有一级组织")
    @RequestMapping(value = "/getLevelOneOrg",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<OrgTreeVO>> getLevelOneOrg(){
        Result<List<OrgTreeVO>> result = new Result<>();
        result.setResult(orgSV.getLevelOneOrg(getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "新增组织", notes = "增加组织，上级组织ID（如果是添加一级组织则传0）、组织名称必填，组织描述、领导、邮件组选填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "addOrgReq", value = "添加组织请求参数", required = true, dataType = "AddOrgReqVO"),
    })
    @RequestMapping(value = "/addOrg",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> addOrg(@RequestBody AddOrgReqVO addOrgReq){
        Result<String> result = new Result<>();
        result.setResult(orgSV.addOrg(addOrgReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "删除组织", notes = "删除组织，组织ID必填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "delOrgReq", value = "删除组织请求参数", required = true, dataType = "DelOrgReqVO"),
    })
    @RequestMapping(value = "/delOrg",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> delOrg(@RequestBody DelOrgReqVO delOrgReq){
        Result<String> result = new Result<>();
        result.setResult(orgSV.delOrg(delOrgReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "修改组织", notes = "修改组织，组织ID必填，组织名称、组织描述、领导、邮件组选填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "modOrgReq", value = "修改组织请求参数", required = true, dataType = "ModOrgReqVO"),
    })
    @RequestMapping(value = "/modOrg",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modOrg(@RequestBody ModOrgReqVO modOrgReq){
        Result<String> result = new Result<>();
        result.setResult(orgSV.modOrg(modOrgReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "调整人员所属组织", notes = "调整人员所属组织，组织ID、用户ID、目标组织ID必填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "moveUserToOrgReq", value = "修改组织请求参数", required = true, dataType = "MoveUserToOrgReqVO"),
    })
    @RequestMapping(value = "/moveUser",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> moveUser(@RequestBody MoveUserToOrgReqVO moveUserToOrgReq){
        Result<String> result = new Result<>();
        result.setResult(orgSV.moveUser(moveUserToOrgReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "根据条件查询组织所有人员信息", notes = "根据条件查询组织所有人员信息，组织ID必填、其他参数选填")
    @RequestMapping(value = "/queryUsers",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<OrgUserRelVO>> queryUsers(@RequestBody PageRequestContainer<QueryOrgUserRelReqVO> requestContainer){
        Result<PageResultContainer<OrgUserRelVO>> result = new Result<>();
        result.setResult(orgSV.queryUsers(requestContainer));
        return result;
    }

    @ApiOperation(value = "修改组织内用户角色", notes = "修改用户在组织内的角色，组织ID、用户Id、新角色ID必填")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "modOrgUserRelReq", value = "修改组织内用户角色请求参数", required = true, dataType = "ModOrgUserRelReqVO"),
    })
    @RequestMapping(value = "/modUserRole",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> modUserRole(@RequestBody ModOrgUserRelReqVO modOrgUserRelReq){
        Result<String> result = new Result<>();
        result.setResult(orgSV.modUserRole(modOrgUserRelReq, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取组织详细信息", notes = "根据组织ID获取组织详细信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织标识", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getOrgInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrgDetailInfoVO> getOrgInfo(@RequestParam("orgId") int orgId){
        Result<OrgDetailInfoVO> result = new Result<>();
        result.setResult(orgSV.getOrgInfo(orgId, getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "获取所有组织角色", notes = "获取所有组织角色")
    @RequestMapping(value = "/getOrgRole",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<RoleVO>> getOrgRole(){
        Result<List<RoleVO>> result = new Result<>();
        result.setResult(orgSV.getOrgRole());
        return result;
    }

    @ApiOperation(value = "获取组织内所有人员", notes = "根据组织ID获取组织内所有人员（不包含该组织的领导者）")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织标识", required = true, dataType = "int"),
    })
    @RequestMapping(value = "/getOrgUsers",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<OrgUserVO>> getOrgUsers(@RequestParam("orgId") int orgId){
        Result<List<OrgUserVO>> result = new Result<>();
        result.setResult(orgSV.getOrgUsers(orgId, getCurrentUser().getId()));
        return result;
    }


}
