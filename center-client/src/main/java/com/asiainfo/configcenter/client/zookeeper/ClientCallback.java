package com.asiainfo.configcenter.client.zookeeper;

import com.asiainfo.configcenter.client.util.StringUtil;
import com.asiainfo.configcenter.zookeeper.cczk.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端zk重连回调类
 * Created by bawy on 2018/9/11 17:39.
 */
public class ClientCallback implements Callback {

    private Logger logger = LoggerFactory.getLogger(ClientCallback.class);

    @Override
    public void deal() {
        try {
            ZookeeperConnection.createNode();
        }catch (Exception e){
            logger.error(StringUtil.printStackTraceToString(e));
            logger.error("执行ZK断开重连的回调方法失败");
        }
    }
}
