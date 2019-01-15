package com.asiainfo.configcenter.zookeeper;

import com.asiainfo.configcenter.zookeeper.cczk.ConfigFileNodeOperTest;
import org.apache.zookeeper.CreateMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.util.Properties;

public class ZkBaseTest {
    protected static ZookeeperManager zookeeperManager;
    private static String defaultPreFix;
    public static final String envName = "product";
    public static final String projectName = "zk-client";
    @BeforeClass
    public static void before()throws Exception{
        Properties properties = new Properties();
        properties.load(ConfigFileNodeOperTest.class.getResourceAsStream("/zookeeper.properties"));
        String host = properties.getProperty("zookeeper.host");
        defaultPreFix = properties.getProperty("zookeeper.prefix");
        zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init(host,defaultPreFix,false);
        //创建前缀节点
        if(zookeeperManager.existNode("")){
            zookeeperManager.deleteNodeRecursion("");
        }
        zookeeperManager.createNode("","", CreateMode.PERSISTENT);
        zookeeperManager.createNode("/apps","", CreateMode.PERSISTENT);
        String appNodePath = "/apps/"+projectName;//项目节点
        zookeeperManager.createNode(appNodePath,"", CreateMode.PERSISTENT);//创建项目节点
        zookeeperManager.createNode(appNodePath+"/envs","",CreateMode.PERSISTENT);//创建环境根节点
        zookeeperManager.createNode(appNodePath+"/envs/"+envName,"",CreateMode.PERSISTENT);//创建环境节点
    }
    @AfterClass
    public static void after()throws Exception{
        zookeeperManager.deleteNodeRecursion("");
    }
}
