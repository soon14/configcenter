package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.dao.repository.OperationLogRepository;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by oulc on 2018/7/20.
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class OperationLogSVImplTest {
    @Resource
    private OperationLogRepository operationLogRepository;
    @Test
    public void test(){
       System.out.println(System.currentTimeMillis());
    }

}