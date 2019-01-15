package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.BaseAppReqVO;

/**
 * 权限校验服务类
 * Created by bawy on 2018/8/23 19:35.
 */
public interface IPermissionCheckSV {

    /**
     * 应用权限检查
     * @author bawy
     * @date 2018/8/23 19:37
     * @param baseAppReq 应用接口基础请求参数
     * @param userId 当前登录用户
     * @param url 请求地址
     * @return 校验通过返回
     */
    boolean appPermissionCheck(BaseAppReqVO baseAppReq, int userId, String url);

}
