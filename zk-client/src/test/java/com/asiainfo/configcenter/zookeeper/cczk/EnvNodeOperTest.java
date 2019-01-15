package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZkBaseTest;
import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import org.apache.zookeeper.CreateMode;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Properties;

import static org.junit.Assert.*;

public class EnvNodeOperTest extends ZkBaseTest{
    private static EnvNodeOper envNodeOper = new EnvNodeOper(zookeeperManager);
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test
    public void createEnvNode() throws Exception{
        String envName = "createEnvNode";
        envNodeOper.createEnvNode(projectName,envName,"test1");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        Assert.assertEquals("test1",envNodeOper.readEnvNodeData(projectName,envName));
    }

    /**
     * 删除没有配置子节点的环境节点
     * @throws Exception
     */
    @Test
    public void deleteEnvNode() throws Exception{
        String envName = "deleteEnvNode";
        envNodeOper.createEnvNode(projectName,envName,"");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        envNodeOper.deleteEnvNode(projectName,envName);
        Assert.assertFalse(envNodeOper.envNodeExist(projectName,envName));
    }

    /**
     * 删除有配置文件子节点的环境节点
     * @throws Exception
     */
    @Test
    public void deleteEnvNode1()throws Exception{
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("项目:zk-client,环境:deleteEnvNode1删除失败，原因：环境下存在配置文件子节点:[configFile1]");
        String envName = "deleteEnvNode1";
        envNodeOper.createEnvNode(projectName,envName,"");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        CCServerZKManager.getInstance().getConfigFileNodeOper().createConfigFileNode(projectName,envName,"configFile1","");
        envNodeOper.deleteEnvNode(projectName,envName);
        Assert.assertFalse(envNodeOper.envNodeExist(projectName,envName));
    }

    /**
     * 删除有配置项子节点的环境节点
     * @throws Exception
     */
    @Test
    public void deleteEnvNode2()throws Exception{
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("项目:zk-client,环境:deleteEnvNode2删除失败，原因：环境下存在配置项子节点:[configItem1]");
        String envName = "deleteEnvNode2";
        envNodeOper.createEnvNode(projectName,envName,"");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        CCServerZKManager.getInstance().getConfigItemNodeOper().createConfigItemNode(projectName,envName,"configItem1","");
        envNodeOper.deleteEnvNode(projectName,envName);
        Assert.assertFalse(envNodeOper.envNodeExist(projectName,envName));
    }

    @Test
    public void writeEnvNodeData() throws Exception{
        String envName = "writeEnvNodeData";
        envNodeOper.createEnvNode(projectName,envName,"");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        envNodeOper.writeEnvNodeData(projectName,envName,"test1");
        Assert.assertEquals("test1",envNodeOper.readEnvNodeData(projectName,envName));
    }

    @Test
    public void readEnvNodeData() throws Exception{
        String envName = "readEnvNodeData";
        envNodeOper.createEnvNode(projectName,envName,"test2");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        Assert.assertEquals("test2",envNodeOper.readEnvNodeData(projectName,envName));
    }

    @Test
    public void envNodeExist()throws Exception {
        String envName = "envNodeExist";
        envNodeOper.createEnvNode(projectName,envName,"test3");
        Assert.assertTrue(envNodeOper.envNodeExist(projectName,envName));
        Assert.assertEquals("test3",envNodeOper.readEnvNodeData(projectName,envName));
    }
}