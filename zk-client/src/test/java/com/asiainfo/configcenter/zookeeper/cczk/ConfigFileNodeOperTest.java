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

public class ConfigFileNodeOperTest extends ZkBaseTest{
    private static ConfigFileNodeOper configFileNodeOper = new ConfigFileNodeOper(zookeeperManager);

    @Test
    public void createConfigFileNode() throws Exception{
        String configFileName = "configFileForCreate";
        configFileNodeOper.createConfigFileNode(projectName,envName,configFileName,"test1");
        Assert.assertTrue(configFileNodeOper.configFileNodeExist(projectName,envName,configFileName));
        Assert.assertEquals("test1",configFileNodeOper.readConfigFileNodeData(projectName,envName,configFileName));
    }

    @Test
    public void deleteConfigFileNode()throws Exception {
        String configFileName = "configFileForDelete";
        configFileNodeOper.createConfigFileNode(projectName,envName,configFileName,"");
        Assert.assertTrue(configFileNodeOper.configFileNodeExist(projectName,envName,configFileName));
        configFileNodeOper.deleteConfigFileNode(projectName,envName,configFileName);
        Assert.assertFalse(configFileNodeOper.configFileNodeExist(projectName,envName,configFileName));
    }

    @Test
    public void writeConfigFileNodeData() throws Exception{
        String configFileName = "configFileForWrite";
        configFileNodeOper.createConfigFileNode(projectName,envName,configFileName,"test1");
        Assert.assertEquals("test1",configFileNodeOper.readConfigFileNodeData(projectName,envName,configFileName));
        configFileNodeOper.writeConfigFileNodeData(projectName,envName,configFileName,"test2");
        Assert.assertEquals("test2",configFileNodeOper.readConfigFileNodeData(projectName,envName,configFileName));
    }

    @Test
    public void readConfigFileNodeData() throws Exception{
        String configFileName = "configFileForRead";
        configFileNodeOper.createConfigFileNode(projectName,envName,configFileName,"test1");
        Assert.assertEquals("test1",configFileNodeOper.readConfigFileNodeData(projectName,envName,configFileName));
    }

    @Test
    public void configFileNodeExist()throws Exception{
        String configFileName = "configFileForExist";
        configFileNodeOper.createConfigFileNode(projectName,envName,configFileName,"test1");
        Assert.assertTrue(configFileNodeOper.configFileNodeExist(projectName,envName,configFileName));
    }
}