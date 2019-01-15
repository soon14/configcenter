package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.po.InstanceConfigPojo;
import com.asiainfo.configcenter.center.po.InstanceInfoPojo;
import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import org.apache.zookeeper.CreateMode;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

public class TimeUtilTest {


    @Test
    public void currentTime() {
        Integer a =  null;
        Assert.assertFalse(a!= null && a!=0);
    }

    @Test
    public void afterTime() {
        System.out.println(TimeUtil.timeFormat(1533698553000L));
    }

    @Test
    public void timeFormat()throws Exception{
        ZookeeperManager zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init("10.9.236.232:2181","/config-center",false);
        InstanceInfoPojo instanceInfoPojo = new InstanceInfoPojo();
        instanceInfoPojo.setIp("10.9.236.232");
        instanceInfoPojo.setInstanceId(1000000001);
        InstanceConfigPojo[] instanceConfigPojos = new InstanceConfigPojo[1];
        InstanceConfigPojo instanceConfigPojo = new InstanceConfigPojo();
        instanceConfigPojos[0] = instanceConfigPojo;
        instanceConfigPojo.setConfigType(ProjectConstants.CONFIG_TYPE_FILE);
        instanceConfigPojo.setConfigName("zhang123.txt");
        instanceConfigPojo.setConfigVersion("26280fd57e44627c5b13b871fa3dc1dab5c15bb2");
        instanceInfoPojo.setInstanceConfigPojos(instanceConfigPojos);
        zookeeperManager.writeNodeData("/apps/oulcTest/envs/oulcTestEnv/instances-info/ins2",JSONUtil.obj2JsonStr(instanceInfoPojo));

    }

    @Test
    public void test()throws Exception{
        ZookeeperManager zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init("10.9.236.232:2181","/config-center",false);
        long startTime = System.currentTimeMillis();
        List<String> child = zookeeperManager.getChild("/testNode",null);
        for(String nodeName:child){
            InstanceInfoPojo instanceInfoPojo = JSONUtil.jsonStrToBean(zookeeperManager.readNodeData("/testNode/"+nodeName),InstanceInfoPojo.class);
            System.out.println(instanceInfoPojo);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime)/1000+"秒");
    }
}