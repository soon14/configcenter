package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZkBaseTest;
import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import org.apache.zookeeper.CreateMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class ConfigItemNodeOperTest extends ZkBaseTest{
    private static ConfigItemNodeOper configItemNodeOper = new ConfigItemNodeOper(zookeeperManager);

    @Test
    public void createConfigItemNode() throws Exception{
        String configItemName = "configItemForCreate";
        configItemNodeOper.createConfigItemNode(projectName,envName,configItemName,"test1");
        Assert.assertTrue(configItemNodeOper.configItemNodeExist(projectName,envName,configItemName));
        Assert.assertEquals("test1",configItemNodeOper.readConfigItemNodeData(projectName,envName,configItemName));
    }

    @Test
    public void deleteConfigItemNode()throws Exception {
        String configItemName = "configItemForDelete";
        configItemNodeOper.createConfigItemNode(projectName,envName,configItemName,"");
        Assert.assertTrue(configItemNodeOper.configItemNodeExist(projectName,envName,configItemName));
        configItemNodeOper.deleteConfigItemNode(projectName,envName,configItemName);
        Assert.assertFalse(configItemNodeOper.configItemNodeExist(projectName,envName,configItemName));
    }

    @Test
    public void writeConfigItemNodeData()throws Exception {
        String configItemName = "configItemForWrite";
        configItemNodeOper.createConfigItemNode(projectName,envName,configItemName,"test1");
        Assert.assertEquals("test1",configItemNodeOper.readConfigItemNodeData(projectName,envName,configItemName));
        configItemNodeOper.writeConfigItemNodeData(projectName,envName,configItemName,"test2");
        Assert.assertEquals("test2",configItemNodeOper.readConfigItemNodeData(projectName,envName,configItemName));
    }

    @Test
    public void readConfigItemNodeData() throws Exception{
        String configItemName = "configItemForRead";
        configItemNodeOper.createConfigItemNode(projectName,envName,configItemName,"test1");
        Assert.assertEquals("test1",configItemNodeOper.readConfigItemNodeData(projectName,envName,configItemName));
    }

    @Test
    public void configItemNodeExist()throws Exception {
        String configItemName = "configItemForExist";
        configItemNodeOper.createConfigItemNode(projectName,envName,configItemName,"test1");
        Assert.assertTrue(configItemNodeOper.configItemNodeExist(projectName,envName,configItemName));
    }
}