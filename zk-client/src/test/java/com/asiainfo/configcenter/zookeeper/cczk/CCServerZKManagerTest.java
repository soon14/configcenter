package com.asiainfo.configcenter.zookeeper.cczk;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class CCServerZKManagerTest {
    private static CCServerZKManager ccServerZKManager;

    @BeforeClass
    public static void before()throws Exception{
        ccServerZKManager = CCServerZKManager.getInstance();
        //添加项目节点
    }

    /**
     * 测试获取实例的方法
     */
    @Test
    public void getInstance() {
        Assert.assertNotNull(CCServerZKManager.getInstance());
    }

    @Test
    public void getZKManager() {
        Assert.assertNotNull(ccServerZKManager.getZKManager());
    }

    @Test
    public void getProjectNodeOper(){
        Assert.assertNotNull(ccServerZKManager.getProjectNodeOper());
    }
    @Test
    public void getEnvNodeOper(){
        Assert.assertNotNull(ccServerZKManager.getEnvNodeOper());
    }
    @Test
    public void getConfigFileNodeOper(){
        Assert.assertNotNull(ccServerZKManager.getConfigFileNodeOper());
    }
    @Test
    public void getConfigItemNodeOper(){
        Assert.assertNotNull(ccServerZKManager.getConfigItemNodeOper());
    }

}