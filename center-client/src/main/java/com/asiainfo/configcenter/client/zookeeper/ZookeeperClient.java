package com.asiainfo.configcenter.client.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class ZookeeperClient {

    public void init() throws IOException, InterruptedException, KeeperException {
        ZookeeperConnection.zkConnection();
        ZookeeperConnection.createNode();
    }

}
