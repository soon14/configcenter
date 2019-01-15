package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.system.SelectDataVO;

import java.util.List;

/**
 * 公共数据服务接口
 * Created by bawy on 2018/7/23 14:14.
 */
public interface ICommonDataSV {

    /**
     * 获取下来列表数据
     * @param key 关键字
     * @return 返回对应的下拉列表数据
     */
    List<SelectDataVO> getSelectData(String key);

    /**
     * 清除菜单、权限等缓存
     * @author bawy
     * @date 2018/7/26 16:39
     * @param userId 当前登录的用户标识
     */
    void cleanCache(int userId);
}
