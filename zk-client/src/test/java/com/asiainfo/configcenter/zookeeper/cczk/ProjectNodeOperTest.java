package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZkBaseTest;
import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Properties;

import static org.junit.Assert.*;

public class ProjectNodeOperTest extends ZkBaseTest{
    private static ProjectNodeOper projectNodeOper = new ProjectNodeOper(zookeeperManager);
    private static String projectName =  "zk-client1";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createProjectNode() throws Exception{
        projectNodeOper.createProjectNode(projectName,"");
        Assert.assertTrue(projectNodeOper.projectNodeExist(projectName));
        projectNodeOper.deleteProjectNode(projectName);
    }

    /**
     * 删除没有子环境节点的项目节点
     * @throws Exception
     */
    @Test
    public void deleteProjectNode()throws Exception {
        projectNodeOper.createProjectNode(projectName,"");
        Assert.assertTrue(projectNodeOper.projectNodeExist(projectName));
        projectNodeOper.deleteProjectNode(projectName);
        Assert.assertFalse(projectNodeOper.projectNodeExist(projectName));
    }

    @Test
    public void projectNodeExist() throws Exception{
        projectNodeOper.createProjectNode(projectName,"");
        Assert.assertTrue(projectNodeOper.projectNodeExist(projectName));
        projectNodeOper.deleteProjectNode(projectName);
    }

    /**
     * 删除有自环境节点的项目节点
     */
    @Test
    public void deleteProjectNode1()throws Exception{
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("项目：/apps/zk-client2下存在环境节点：[envs]");
        String projectName1 =  "zk-client2";
        projectNodeOper.createProjectNode(projectName1,"");
        Assert.assertTrue(projectNodeOper.projectNodeExist(projectName1));
        CCServerZKManager.getInstance().getEnvNodeOper().createEnvNode(projectName1,envName,"");
        projectNodeOper.deleteProjectNode(projectName1);
        Assert.assertFalse(projectNodeOper.projectNodeExist(projectName1));
    }
}