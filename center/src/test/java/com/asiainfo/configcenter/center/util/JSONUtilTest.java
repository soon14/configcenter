package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.google.gson.JsonObject;
import org.apache.zookeeper.Environment;
import org.junit.Test;

import java.net.InetAddress;
import java.util.HashMap;

import static org.junit.Assert.*;

public class JSONUtilTest {
    @Test
    public void obj2JsonStr() {
        HashMap map = new HashMap();
        map.put("test","1");
        System.out.println(JSONUtil.obj2JsonStr(map));
    }
    @Test
    public void jsonStr2JsonObject(){
        String jsonStr = "{'oulc':'test'}";
        JsonObject jsonObject = JSONUtil.jsonStr2JsonObject(jsonStr);
        System.out.println(1);
    }

    @Test
    public void test()throws Exception{
        long startTime = System.currentTimeMillis();
        /*ZookeeperManager zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init("localhost:2181","",false);*/
        System.out.println(InetAddress.getLocalHost());
        System.out.println(InetAddress.getLocalHost().getCanonicalHostName());

        long endTime = System.currentTimeMillis();

        System.out.println("zookeeper启动耗时:"+(endTime - startTime)/1000+"秒");
    }
}