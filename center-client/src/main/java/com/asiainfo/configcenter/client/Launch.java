package com.asiainfo.configcenter.client;

import com.asiainfo.configcenter.client.execute.UpdateConfig;
import com.asiainfo.configcenter.client.zookeeper.ZookeeperClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 客户端启动类
 * Created by bawy on 2018/9/10 14:51.
 */
public class Launch {

    private Logger logger  = LoggerFactory.getLogger(Launch.class);

    public void start() {
        try {
            ZookeeperClient zkClient = new ZookeeperClient();
            zkClient.init();
            logger.info("启动配置中心客户端成功");
        } catch (Exception e) {
            logger.error("启动配置中心客户端失败",e);
            throw new RuntimeException("启动配置中心客户端失败");
        } finally {
            UpdateConfig.pullAndUpdateFiles();
        }
    }

    public static void main(String[] args) throws Exception{
        Launch launch = new Launch();
        launch.start();
        Thread.sleep(1000*60*30);
    }
}
