package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.home.HomePageRepVO;

public interface IHomeSV {

    /**
     * 获取主页信息
     * @author oulc
     * @date 18-8-28 上午11:17
     * @param userId 用户主键
     * @return 主页信息
     */
    HomePageRepVO getHomePageData(int userId);

    /**
     * 添加最近访问的项目
     * */
    void createRecentVisitedApp(int appId, int userId);
}
