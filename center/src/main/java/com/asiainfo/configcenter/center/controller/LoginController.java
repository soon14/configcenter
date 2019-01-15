package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.PermissionCommon;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.vo.system.LoginReqVO;
import com.asiainfo.configcenter.center.vo.system.MenuVO;
import com.asiainfo.configcenter.center.vo.user.UserInfoVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * 登录控制层
 * Created by bawy on 2018/7/3.
 */
@Controller
@RequestMapping("/center/oa")
public class LoginController extends BaseController{

    @Resource
    private IUserSV userSV;

    @ApiOperation(value = "用户登录", notes = "验证用户名和密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "body", name = "loginReqVO", value = "用户信息", required = true, dataType = "LoginReqVO"),
    })
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result<UserInfoVO> login(@RequestBody LoginReqVO loginReqVO, HttpSession session){
        Result<UserInfoVO> result = new Result<>();
        UserInfoVO userInfo = userSV.login(loginReqVO.getUsername(), loginReqVO.getPassword());
        session.setAttribute(ProjectConstants.CURRENT_USER, userInfo);
        result.setResult(userInfo);
        return result;
    }

    @ApiOperation(value = "用户注销", notes = "用户退出登录")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public Result<String> logout(HttpSession session) {
        Result<String> result = new Result<>();
        session.invalidate();
        //session.removeAttribute(ProjectConstants.CURRENT_USER);
        return result;
    }

    @ApiOperation(value = "获取菜单树", notes = "根据用户角色获取对应所有菜单列表")
    @RequestMapping(value = "/menu-tree",method = RequestMethod.POST)
    @ResponseBody
    public Result<MenuVO> getMenuTree(){
        UserInfoVO user = getCurrentUser();
        Result<MenuVO> result = new Result<>();
        int roleId = userSV.getRoleIdByUserId(user.getId());
        MenuVO menuVO = PermissionCommon.getMenuTreeByRoleId(roleId);
        result.setResult(menuVO);
        return result;
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public Result<UserInfoVO> test1(){
        Result<UserInfoVO> result = new Result<>();
        result.setResult(getCurrentUser());
        return result;
    }

}
