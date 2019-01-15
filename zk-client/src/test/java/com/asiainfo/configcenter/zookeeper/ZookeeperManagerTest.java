package com.asiainfo.configcenter.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class ZookeeperManagerTest {

    private static ZookeeperManager zookeeperManager;
    private static String host;

    /**
     * 初始化zk客户端 创建单元测试专用的节点/config-center-test
     * @throws Exception
     */
    @BeforeClass
    public static void init()throws Exception{
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("zookeeper.properties");
        properties.load(inputStream);
        host = properties.getProperty("zookeeper.host");
        zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init(host,"",true);

        // 创建/config-center-test节点，如果已经存在，删除后再创建
        String junitRootPath = "/config-center-test";
        if(zookeeperManager.existNode(junitRootPath)){
            zookeeperManager.deleteNodeRecursion(junitRootPath);
        }
        zookeeperManager.createNode(junitRootPath,"",null,null,CreateMode.PERSISTENT);
    }

    @AfterClass
    public static void AfterClass()throws Exception{
        zookeeperManager.deleteNodeRecursion("/config-center-test");
    }

    /**
     * 创建节点(永久节点、临时节点)
     * @throws Exception
     */
    @Test
    public void createNode()throws Exception{
        String testPath = "/config-center-test/createNodeTest";
        String value = "createNodeTestValue";

        //创建普通的节点
        zookeeperManager.createNode(testPath,value,null,null, CreateMode.PERSISTENT);
        Assert.assertTrue(zookeeperManager.existNode(testPath));
        Assert.assertEquals(value,zookeeperManager.readNodeData(testPath,null,null));
    }




    /**
     * 递归删除节点
     * @throws Exception
     */
    @Test
    public void deleteNodeRecursion()throws Exception{
        String fishPath = "/config-center-test/fish";
        String sharkPath = "/config-center-test/fish/shark";

        //创建/config-center-test/fish节点
        zookeeperManager.createNode(fishPath,"", CreateMode.PERSISTENT);
        Assert.assertTrue(zookeeperManager.existNode(fishPath));
        //创建/config-center-test/fish/shark节点
        zookeeperManager.createNode(sharkPath,"", CreateMode.PERSISTENT);
        Assert.assertTrue(zookeeperManager.existNode(sharkPath));
        //递归删除/config-center-test/fish节点并校验
        zookeeperManager.deleteNodeRecursion(fishPath);
        Assert.assertFalse(zookeeperManager.existNode(fishPath));
    }

    /**
     * 测试读取节点数据
     * @throws Exception
     */
    @Test
    public void readNodeData()throws Exception{
        String nodePath = "/config-center-test/readNode";
        String value = "TestValue";
        zookeeperManager.createNode(nodePath,value,CreateMode.PERSISTENT);
        String nodeData = zookeeperManager.readNodeData(nodePath);
        Assert.assertEquals(value,nodeData);
    }

    @Test
    public void writeNodeData()throws Exception{
        String nodePath = "/config-center-test/writeNode";
        String value = "writeNodeValue";

        zookeeperManager.createNode(nodePath,"",CreateMode.PERSISTENT);
        zookeeperManager.writeNodeData(nodePath,value);
        String nodeData =zookeeperManager.readNodeData(nodePath);
        Assert.assertEquals(nodeData,value);
    }

    /**
     * 测试existNode节点
     * @throws Exception
     */
    @Test
    public void existNode()throws Exception{
        String nodePath = "/config-center-test/existNode";
        String value = "existNodeValue";

        Assert.assertFalse(zookeeperManager.existNode(nodePath));
        zookeeperManager.createNode(nodePath,value,CreateMode.PERSISTENT);
        Assert.assertTrue(zookeeperManager.existNode(nodePath));
    }

    @Test
    public void getChild()throws Exception{
        String nodePath = "/config-center-test/getChildNodeRoot";
        String childNodePath1 = "/config-center-test/getChildNodeRoot/child1";
        String childNodePath2 = "/config-center-test/getChildNodeRoot/child2";
        zookeeperManager.createNode(nodePath,"",CreateMode.PERSISTENT);
        zookeeperManager.createNode(childNodePath1,"",CreateMode.PERSISTENT);
        zookeeperManager.createNode(childNodePath2,"",CreateMode.PERSISTENT);
        List<String> child = zookeeperManager.getChild(nodePath,null);
        Assert.assertEquals(2,child.size());
        Assert.assertTrue(child.contains("child1"));
        Assert.assertTrue(child.contains("child2"));
    }



    private static void setDefaultPrefix(String defaultPrefix)throws Exception{
        Class clz = zookeeperManager.getClass();
        Field field = clz.getDeclaredField("defaultPrefixStr");
        field.setAccessible(true);
        field.set(zookeeperManager,defaultPrefix);
    }

    /**
     * 测试fixPath
     * @throws Exception
     */
    @Test
    public void fixPath()throws Exception{


        Class clz = zookeeperManager.getClass();
        Method method = clz.getDeclaredMethod("fixPath",String.class);//fixPath（）方法对象
        method.setAccessible(true);
        String result;

        setDefaultPrefix("/config-center-test");
        result = (String)method.invoke(zookeeperManager,"/test");
        Assert.assertEquals("/config-center-test/test",result);

        setDefaultPrefix("");
        result = (String)method.invoke(zookeeperManager,"/test");
        Assert.assertEquals("/test",result);
    }

}
