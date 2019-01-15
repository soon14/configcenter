package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcUserInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.vo.user.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息管理控制层
 * Created by bawy on 2018/7/17 10:28.
 */
@Controller
@RequestMapping(value = "/center/user")
public class UserController extends BaseController{

    @Resource
    private IUserSV userSV;

    @ApiOperation(value = "用户注册", notes = "用户注册帐号")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "registerReqVO", value = "用户注册信息", required = true, dataType = "RegisterReqVO"),
    })
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> register(@RequestBody RegisterReqVO registerReqVO){
        Result<String> result = new Result<>();
        String response = userSV.register(registerReqVO);
        result.setResult(response);
        return result;
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户基本信息和扩展信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "userInfoReqVO", value = "用户信息", required = true, dataType = "UserInfoReqVO"),
    })
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> updateUserInfo(@RequestBody UserInfoReqVO userInfoReqVO){
        Result<Boolean> result = new Result<>();
        userSV.updateUserInfo(userInfoReqVO,getCurrentUser().getId());
        result.setResult(true);
        return result;
    }

    @ApiOperation(value = "查询用户基本信息、扩展信息、角色信息", notes = "查询用户基本信息、扩展信息、角色信息")
    @ApiImplicitParams(value = {
            //@ApiImplicitParam(paramType = "body", name = "pageRequestContainer", value = "页数、用户名、用户状态", required = true, dataType = "PageRequestContainer<UserInfoAndRoleReqVO>"),
    })
    @RequestMapping(value = "/getUserInfoAndRole",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CXCcUserInfoEntity>> getUserInfoAndRole(@RequestBody PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer){
        Result<PageResultContainer<CXCcUserInfoEntity>> result = new Result<>();
        result.setResult(userSV.getUserInfoAndRole(pageRequestContainer,getCurrentUser().getId()));
        return result;
    }

    @ApiOperation(value = "注销账户", notes = "管理员注销账户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "closeAccountVO", value = "账户信息", required = true, dataType = "CloseAccountVO"),
    })
    @RequestMapping(value = "/closeAccount",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> closeAccount(@RequestBody CloseAccountVO closeAccountVO){
        Result<Boolean> result = new Result<>();
        userSV.closeAccounts(closeAccountVO,getCurrentUser().getId());
        result.setResult(true);
        return result;
    }

    @ApiOperation(value = "审核账户", notes = "普通用户注册后，管理员审核账户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "auditAccountVO", value = "账户、审核信息", required = true, dataType = "AuditAccountVO"),
    })
    @RequestMapping(value = "/auditAccount",method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> auditAccount(@RequestBody AuditAccountVO auditAccountVO){
        Result<Boolean> result = new Result<>();
        userSV.auditAccounts(auditAccountVO,getCurrentUser().getId());
        result.setResult(true);
        return result;
    }

    @ApiOperation(value = "更新用户密码", notes = "校验原密码，通过后更新密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "passwordVO", value = "原密码、新密码", required = true, dataType = "PasswordVO"),
    })
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updatePassword(@RequestBody PasswordVO passwordVO){
        Result<String> result = new Result<>();
        userSV.updatePassword(passwordVO,getCurrentUser().getId());
        result.setResult("更新成功");
        return result;
    }

    @ApiOperation(value = "获取邮箱验证码", notes = "匹配用户名和邮箱，通过发送验证码到邮箱")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "email", value = "用户邮箱", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getAuthCode(@RequestParam("username") String username, @RequestParam("email") String email){
        Result<String> result = new Result<>();
        userSV.getAuthCode(username, email);
        result.setResult("验证码已发送至用户邮箱");
        return result;
    }

    @ApiOperation(value = "重置密码", notes = "根据验证码重置密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "resetPasswordVO", value = "重置密码对象", required = true, dataType = "ResetPasswordVO"),
    })
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> resetPassword(@RequestBody ResetPasswordVO resetPasswordVO){
        Result<String> result = new Result<>();
        userSV.resetPassword(resetPasswordVO);
        result.setResult("密码重置成功,请到登录页面重新登录");
        return result;
    }

    @ApiOperation(value = "获取用户列表", notes = "根据用户名或昵称获取用户名列表")
    @ApiImplicitParams(value = {
    })
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @ResponseBody
    public Result<PageResultContainer<CcUserEntity>> getUserList(@RequestBody PageRequestContainer<UserInfoReqVO> pageRequestContainer){
        Result<PageResultContainer<CcUserEntity>> result = new Result<>();
        result.setResult(userSV.getUserList(pageRequestContainer));
        return result;
    }

}
