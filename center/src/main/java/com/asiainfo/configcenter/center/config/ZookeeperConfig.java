package com.asiainfo.configcenter.center.config;

import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by oulc on 2018/7/25.
 */
@Configuration
public class ZookeeperConfig {
    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${zookeeper.prefix}")
    private String zookeeperDefaultPrefix;

    private CCServerZKManager ccServerZKManager;

    @Bean
    public CCServerZKManager ccServerZKManager(){
        if(ccServerZKManager == null){
            CCServerZKManager.init(zookeeperHost,zookeeperDefaultPrefix);
            ccServerZKManager = CCServerZKManager.getInstance();
        }
        return CCServerZKManager.getInstance();
    }
}
