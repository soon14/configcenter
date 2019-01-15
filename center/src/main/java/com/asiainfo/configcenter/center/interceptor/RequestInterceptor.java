package com.asiainfo.configcenter.center.interceptor;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.controller.BaseController;
import com.asiainfo.configcenter.center.service.interfaces.IPermissionSV;
import com.asiainfo.configcenter.center.vo.user.UserInfoVO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 请求拦截器
 * Created by bawy on 2018/7/3.
 */
@Component("requestInterceptor")
public class RequestInterceptor implements HandlerInterceptor{


    @Resource
    private IPermissionSV permissionSV;

    @Value("${web.server.url}")
    private String webServerUrl;

    private List<String> unInterceptUrls = new ArrayList<>();

    @PostConstruct
    private void init() {
        unInterceptUrls = permissionSV.getUnInterceptUrls();
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            return true;
        }
        String uri = request.getRequestURI();
        if (unInterceptUrls.contains(uri)) {
            return true;
        }
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute(ProjectConstants.CURRENT_USER);
        if (userInfoVO == null){
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setHeader("Access-Control-Allow-Origin", webServerUrl);
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACES");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "*");
            Result result = new Result();
            result.setSuccess(false);
            result.setErrorCode(HttpServletResponse.SC_UNAUTHORIZED+"");
            result.setErrorMsg("用户未登录!");
            Gson gson = new Gson();
            gson.toJson(result,response.getWriter());
            return false;
        }
        BaseController.setCurrentUser(userInfoVO);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
