package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.JunitConstants;
import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.entity.CcInstanceEntity;
import com.asiainfo.configcenter.center.service.SV.IJunitSV;
import com.asiainfo.configcenter.center.service.interfaces.IInstanceSV;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInstanceReqVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 实例业务层代码 Junit
 * Created by oulc on 2018/7/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InstanceSVImplTest {
    @Resource
    private IInstanceSV iInstanceSV;

    @Resource
    private IJunitSV iJunitSV;


    /**
     * 测试 实例连接时出现网络问题  导致数据库少了一条数据
     * @throws Exception 异常
     */
    @Transactional
    @Rollback
    @Test
    public void fixData()throws Exception{
        //创建应用和环境
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();

        //只创建一个实例
        iJunitSV.createInsAll(appInfoVO,1);
        Page<CcInstanceEntity> page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)-1,null,0,10);
        Assert.assertEquals(1,page.getTotalElements());//只能查询从户一个实例

        //创建一个多余的实例
        iJunitSV.createInstanceNode(appInfoVO,2);

        iInstanceSV.fixData(appInfoVO.getEnvId());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)-1,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());

        iJunitSV.rollBack();
    }

    /**
     * 测试 实例连接时出现网络问题  导致数据库有一条数据断开连接了
     * @throws Exception 异常
     */
    @Transactional
    @Rollback
    @Test
    public void fixData2()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();

        //创建实例
        iJunitSV.createInsAll(appInfoVO,1);
        int insId2 = iJunitSV.createInsAll(appInfoVO,2);

        Page<CcInstanceEntity> page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)-1,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());

        //查询表置为失联状态
        iInstanceSV.setInstanceAliveStatus(insId2,ProjectConstants.STATUS_INVALID);

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)1,null,0,10);
        Assert.assertEquals(1,page.getTotalElements());

        iInstanceSV.fixData(appInfoVO.getEnvId());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)1,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());

    }

    /**
     * 测试 实例连接时出现网络问题  导致数据库中多了一条正在连接的实例
     * findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer)
     * @throws Exception 异常
     */
    @Transactional
    @Rollback
    @Test
    public void fixData3()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();

        //创建实例
        iJunitSV.createInsAll(appInfoVO,1);
        iJunitSV.createInsAll(appInfoVO,2);

        Page<CcInstanceEntity> page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,ProjectConstants.STATUS_VALID,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());

        iInstanceSV.createInstance(appInfoVO.getAppName(),appInfoVO.getEnvName(),JunitConstants.insName+3,appInfoVO.getEnvId(),JunitConstants.insIp);

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,ProjectConstants.STATUS_VALID,null,0,10);
        Assert.assertEquals(3,page.getTotalElements());

        iInstanceSV.fixData(appInfoVO.getEnvId());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,ProjectConstants.STATUS_VALID,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());
    }

    /**
     * 测试 查询实例 测试查询条件为空异常
     * findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer)
     * @throws Exception 异常
     */
    @Test
    public void findInstance1()throws Exception{
        try {
            PageRequestContainer<QueryInstanceReqVO> pageRequestContainer = new PageRequestContainer<>();
            pageRequestContainer.setCurrentPage(0);
            pageRequestContainer.setPageSize(10);
            iInstanceSV.findInstance(pageRequestContainer);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,data参数不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试 查询实例 环境主键为空异常
     * findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer)
     * @throws Exception 异常
     */
    @Test
    public void findInstance2()throws Exception{
        try {
            PageRequestContainer<QueryInstanceReqVO> pageRequestContainer = new PageRequestContainer<>();
            pageRequestContainer.setCurrentPage(0);
            pageRequestContainer.setPageSize(10);
            QueryInstanceReqVO queryInstanceReqVO = new QueryInstanceReqVO();
            pageRequestContainer.setData(queryInstanceReqVO);
            iInstanceSV.findInstance(pageRequestContainer);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,环境主键不能为空",e.getMessage());
            return;
        }
        Assert.fail();
    }

    /**
     * 测试 查询实例 正常查询
     * findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer)
     * @throws Exception 异常
     */
    @Transactional
    @Rollback
    @Test
    public void findInstance3()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        //创建实例
        iJunitSV.createInsAll(appInfoVO,1);
        iJunitSV.createInsAll(appInfoVO,2);

        PageRequestContainer<QueryInstanceReqVO> pageRequestContainer = new PageRequestContainer<>();
        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(10);
        QueryInstanceReqVO queryInstanceReqVO = new QueryInstanceReqVO();
        queryInstanceReqVO.setEnvId(appInfoVO.getEnvId());
        pageRequestContainer.setData(queryInstanceReqVO);

        PageResultContainer<CcInstanceEntity> page = iInstanceSV.findInstance(pageRequestContainer);
        Assert.assertEquals(2,page.getCount());

        iJunitSV.rollBack();
    }

    /**
     * 测试 查询实例 正常查询
     * findInstance(int envId , String insName, byte isAlive, String insIp, int currentPage, int size)
     * @throws Exception 异常
     */
    @Transactional
    @Rollback
    @Test
    public void findInstance4()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();

        //创建实例
        iJunitSV.createInsAll(appInfoVO,1);
        iJunitSV.createInsAll(appInfoVO,2);

        Page<CcInstanceEntity> page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null,(byte)-1,null,0,10);
        Assert.assertEquals(2,page.getTotalElements());
        List<CcInstanceEntity> insList = page.getContent();
        Assert.assertEquals(2,insList.size());
        Assert.assertEquals(JunitConstants.insName + 1,insList.get(0).getInsName());
        Assert.assertEquals(JunitConstants.insName + 2,insList.get(1).getInsName());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),JunitConstants.insName + 1 ,(byte)-1,null,0,10);
        Assert.assertEquals(1,page.getTotalElements());
        insList = page.getContent();
        Assert.assertEquals(1,insList.size());
        Assert.assertEquals(JunitConstants.insName + 1,insList.get(0).getInsName());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null ,ProjectConstants.STATUS_INVALID,null,0,10);
        Assert.assertEquals(0,page.getTotalElements());

        page = iInstanceSV.findInstance(appInfoVO.getEnvId(),null ,(byte)-1,JunitConstants.insIp+2,0,10);
        Assert.assertEquals(0,page.getTotalElements());

        iJunitSV.rollBack();
    }

    /**
     * 测试 获取zk中存活的实例
     */
    @Transactional
    @Rollback
    @Test
    public void getZkAliveInstance()throws Exception{
        AppInfoVO appInfoVO = iJunitSV.createAppAndEnv();
        iJunitSV.createInsAll(appInfoVO,1);
        iJunitSV.createInsAll(appInfoVO,2);

        List<String>  ins = iInstanceSV.getZkAliveInstance(appInfoVO.getAppName(),appInfoVO.getEnvName());
        Assert.assertEquals(2,ins.size());
        Assert.assertTrue(ins.contains(JunitConstants.insName+1));
        Assert.assertTrue(ins.contains(JunitConstants.insName+2));
    }

    @Transactional
    @Rollback
    @Test
    public void getInstanceByIdCheck(){
        try {
            iInstanceSV.getInstanceByIdCheck(-1);
        }catch (Exception e){
            Assert.assertEquals("实例不存在，实例主键:-1",e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void getInstancesByNotIn(){
        int []ids = {1000000002,2,3,};
        List<CcInstanceEntity> list = iInstanceSV.getInstancesByNotIn(10000047,ids,"","");
        System.out.println(1);
    }

    @Test
    public void getInstancesByConfigIdAndVersion(){
        //List<CcInstanceEntity> list = iInstanceSV.getInstancesByConfigIdAndVersion(10000047,ProjectConstants.CONFIG_TYPE_FILE,10000039,"51ae2a80ef44862e10ed702e2a0a2f534110284a");
        System.out.println(1);
    }
}