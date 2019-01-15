package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.service.interfaces.ICommonDataSV;
import com.asiainfo.configcenter.center.vo.system.SelectDataVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 公共数据服务类单元测试
 * Created by bawy on 2018/7/23 15:19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonDataSVImplTest {

    @Resource
    private ICommonDataSV commonDataSV;
    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void getSelectData() throws Exception {
        List<SelectDataVO> selectDatas = commonDataSV.getSelectData("NOTIFICATION_TYPE");
        Assert.assertNotNull(selectDatas);
    }

    @Test
    public void redisTest(){
        /*List<SelectDataVO> list = new ArrayList<>();
        SelectDataVO selectDataVO1 = new SelectDataVO();
        selectDataVO1.setText("ceshi");
        selectDataVO1.setValue("1");
        list.add(selectDataVO1);
        SelectDataVO selectDataVO2 = new SelectDataVO();
        selectDataVO2.setText("ceshi2");
        selectDataVO2.setValue("2");
        list.add(selectDataVO2);*/
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<String> test = new ArrayList<>();
        test.add("123");
        test.add("456");
        //redisTemplate.opsForList().leftPush( "static_data:test",list);
        //redisTemplate.opsForList().leftPop("static_data:test");
        //redisTemplate.opsForList().leftPushAll("static_data:test",test);
        //List<String> result = (List<String>) redisTemplate.opsForList().leftPop("static_data:test");
        //valueOperations.set("static_data:test", null);
        List<String> result = (List<String>) valueOperations.get("static_data:test");
        System.out.println(result);
    }

}