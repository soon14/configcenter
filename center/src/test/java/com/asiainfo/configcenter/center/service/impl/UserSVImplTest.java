package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.PageRequestContainer;
import com.asiainfo.configcenter.center.common.PageResultContainer;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.dao.repository.UserExtInfoRepository;
import com.asiainfo.configcenter.center.dao.repository.UserRepository;
import com.asiainfo.configcenter.center.dao.repository.UserRoleRelRepository;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.entity.CcUserExtInfoEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcUserInfoEntity;
import com.asiainfo.configcenter.center.entity.CcUserRoleRelEntity;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.util.SecurityUtil;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.vo.user.CloseAccountVO;
import com.asiainfo.configcenter.center.vo.user.UserInfoAndRoleReqVO;
import com.asiainfo.configcenter.center.vo.user.UserInfoReqVO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSVImplTest {
    private int userId;

    @Autowired
    private IUserSV iUserSV;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExtInfoRepository userExtInfoRepository;

    @Resource
    private UserRoleRelRepository userRoleRelRepository;

    @Before
    public void before(){
        CcUserEntity ccUserEntity = new CcUserEntity();
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        ccUserEntity.setEmail("976100102@qq.com");
        ccUserEntity.setNickname("test-nickname1");
        ccUserEntity.setPassword(SecurityUtil.getMD5Code("123456"));
        ccUserEntity.setCreateTime(TimeUtil.currentTime());
        ccUserEntity.setStatus((byte)1);
        ccUserEntity.setUsername("test-user1");
        userRepository.save(ccUserEntity);
        userId = ccUserEntity.getId();
    }


    @After
    public void after(){
        userRepository.delete(userId);
        if(userExtInfoRepository.findOne(userId) != null){
            userExtInfoRepository.delete(userId);
        }
    }


    /**
     * 修改用户基本信息  新增手机号码
     * @author oulc
     * @date 2018/7/17 17:00
     */
    @Transactional
    @Rollback
    @Test
    public void updateUserInfo() {
        String mail = "976100101@qq.com";
        String phone = "17702184734";
        String nickName = "oulc-test2";

        UserInfoReqVO userInfoReqVO = new UserInfoReqVO();
        userInfoReqVO.setEmail(mail);
        userInfoReqVO.setNickname(nickName);
        userInfoReqVO.setPhone(phone);

        iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试

        //校验用户基本信息
        CcUserEntity ccUserEntity = userRepository.findByIdAndStatus(userId,(byte)1);
        Assert.assertEquals(mail ,ccUserEntity.getEmail());
        Assert.assertEquals(nickName,ccUserEntity.getNickname());
        Assert.assertTrue(System.currentTimeMillis()-ccUserEntity.getUpdateTime().getTime() < 5000);


        //校验用户扩展信息(手机号码)
        CcUserExtInfoEntity ccUserExtInfoEntity = userExtInfoRepository.findByUserIdAndExtInfoKeyAndStatus(userId, ProjectConstants.USER_EXT_INFO_PHONE,(byte)1);
        Assert.assertNotNull(ccUserExtInfoEntity);
        Assert.assertEquals(phone,ccUserExtInfoEntity.getExtInfoValue());
        Assert.assertTrue(System.currentTimeMillis()-ccUserExtInfoEntity.getCreateTime().getTime() < 5000);
        Assert.assertTrue(System.currentTimeMillis()-ccUserExtInfoEntity.getUpdateTime().getTime() < 5000);
    }

    /**
     * 修改用户基本信息  修改手机号码
     */
    @Transactional
    @Rollback
    @Test
    public void updateUserInfo1(){
        String mail = "976100101@qq.com";
        String phone = "17702184734";
        String nickName = "oulc-test2";

        UserInfoReqVO userInfoReqVO = new UserInfoReqVO();
        userInfoReqVO.setEmail(mail);
        userInfoReqVO.setNickname(nickName);
        userInfoReqVO.setPhone(phone);

        iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        userInfoReqVO.setPhone("17702184735");
        iUserSV.updateUserInfo(userInfoReqVO,userId);
        CcUserExtInfoEntity ccUserExtInfoEntity = userExtInfoRepository.findByUserIdAndExtInfoKeyAndStatus(userId, ProjectConstants.USER_EXT_INFO_PHONE,(byte)1);
        Assert.assertNotNull(ccUserExtInfoEntity);
        Assert.assertEquals("17702184735",ccUserExtInfoEntity.getExtInfoValue());
        Assert.assertTrue(System.currentTimeMillis()-ccUserExtInfoEntity.getUpdateTime().getTime() < 5000);
    }
    /**
     * 修改用户基本信息  删除手机号码
     */
    @Transactional
    @Rollback
    @Test
    public void updateUserInfo2(){
        String mail = "976100101@qq.com";
        String phone = "17702184734";
        String nickName = "oulc-test2";

        UserInfoReqVO userInfoReqVO = new UserInfoReqVO();
        userInfoReqVO.setEmail(mail);
        userInfoReqVO.setNickname(nickName);
        userInfoReqVO.setPhone(phone);
        iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        userInfoReqVO.setPhone(null);
        iUserSV.updateUserInfo(userInfoReqVO,userId);

        CcUserExtInfoEntity ccUserExtInfoEntity = userExtInfoRepository.findByUserIdAndExtInfoKeyAndStatus(userId, ProjectConstants.USER_EXT_INFO_PHONE,(byte)1);
        Assert.assertNull(ccUserExtInfoEntity);
    }

    /**
     * 修改用户基本信息  测试校验项
     */
    @Transactional
    @Rollback
    @Test
    public void updateUserInfo3(){
        UserInfoReqVO userInfoReqVO =  null;

        //userId为空
        userInfoReqVO = createUserInfoReqVO();
        try {
            iUserSV.updateUserInfo(userInfoReqVO,0);//开始测试
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户Id不能为空",e.getMessage());
        }

        //昵称为空
        userInfoReqVO = createUserInfoReqVO();
        userInfoReqVO.setNickname(null);
        try {
            iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,昵称不能为空",e.getMessage());
        }
        //邮箱为空
        userInfoReqVO = createUserInfoReqVO();
        userInfoReqVO.setEmail(null);
        try {
            iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,邮箱不能为空",e.getMessage());
        }
        //邮箱格式不正确
        userInfoReqVO = createUserInfoReqVO();
        userInfoReqVO.setEmail("oulc@@shpso.com");
        try {
            iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,邮箱格式不正确",e.getMessage());
        }
        //手机格式不正确
        userInfoReqVO = createUserInfoReqVO();
        userInfoReqVO.setPhone("01770218473");
        try {
            iUserSV.updateUserInfo(userInfoReqVO,userId);//开始测试
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,手机号码格式不正确",e.getMessage());
        }
    }

    private UserInfoReqVO createUserInfoReqVO(){
        String mail = "976100101@qq.com";
        String phone = "17702184734";
        String nickName = "oulc-test2";
        UserInfoReqVO userInfoReqVO = new UserInfoReqVO();
        userInfoReqVO.setEmail(mail);
        userInfoReqVO.setNickname(nickName);
        userInfoReqVO.setPhone(phone);
        return userInfoReqVO;
    }

    /**
     * 查询用户基本信息 角色
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole(){
        int length = 6;
        int userIds[] = new int[length];


        for(int i = 0 ;i <length ;i++){
            userIds[i] = createTestUserAddRole("junitTestUser"+i,"junitTestNickName"+i);
        }

        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(1);

        //第一页
        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(5);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        PageResultContainer<CXCcUserInfoEntity> pageResultContainer = iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        Assert.assertNotNull(pageResultContainer);
        Assert.assertEquals(length,pageResultContainer.getCount());
        List<CXCcUserInfoEntity> entities = pageResultContainer.getEntities();
        Assert.assertNotNull(entities);
        Assert.assertEquals(5,entities.size());
        for(int i =0 ;i <entities.size() ;i++){
            Assert.assertEquals("junitTestUser"+i,entities.get(i).getUsername());
        }

        //第二页
        pageRequestContainer.setCurrentPage(1);
        pageResultContainer = iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        Assert.assertNotNull(pageResultContainer);
        Assert.assertEquals(length,pageResultContainer.getCount());
        entities = pageResultContainer.getEntities();
        Assert.assertNotNull(entities);
        Assert.assertEquals(1,entities.size());
        Assert.assertEquals("junitTestUser5",entities.get(0).getUsername());

        //status为1
        CcUserEntity ccUserEntity = userRepository.findOne(userIds[0]);
        ccUserEntity.setStatus(ProjectConstants.STATUS_INVALID);
        userRepository.save(ccUserEntity);
        pageRequestContainer.setCurrentPage(0);
        userInfoAndRoleReqVO.setUserStatus(1);
        pageResultContainer = iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        Assert.assertNotNull(entities);
        Assert.assertEquals(5,pageResultContainer.getCount());

    }

    /**
     * 查询用户没有角色
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole1(){
        int userId = createTestUser("junitTestUser1","junitTestNickName1");
        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(1);

        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(5);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        try {
            iUserSV.getUserInfoAndRole(pageRequestContainer,userId);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户:"+userId+"，不合法，没有关联角色",e.getMessage());
        }

    }

    /**
     * 查询用户不是管理员
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole2(){
        int userId = createTestUserAddRole("junitTestUser1","junitTestNickName1");
        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(1);

        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(5);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        try {
            iUserSV.getUserInfoAndRole(pageRequestContainer,userId);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户："+userId+"，不合法，不是管理员",e.getMessage());
        }
    }

    /**
     * currentPage不合法
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole3(){
        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(1);

        pageRequestContainer.setCurrentPage(-1);
        pageRequestContainer.setPageSize(5);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        try {
            iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,currentPage："+pageRequestContainer.getCurrentPage()+",不合法,必须要大于等于0",e.getMessage());
        }
    }

    /**
     * pageSize不合法
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole4(){
        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(1);

        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(0);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        try {
            iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,pageSize："+pageRequestContainer.getPageSize()+",不合法，必须要大于0",e.getMessage());
        }
    }
    /**
     * userStatus不合法
     */
    @Transactional
    @Rollback
    @Test
    public void getUserInfoAndRole5(){
        PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer = new PageRequestContainer<>();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = new UserInfoAndRoleReqVO();
        userInfoAndRoleReqVO.setNickName("junit");
        userInfoAndRoleReqVO.setUserStatus(3);

        pageRequestContainer.setCurrentPage(0);
        pageRequestContainer.setPageSize(5);
        pageRequestContainer.setData(userInfoAndRoleReqVO);
        try {
            iUserSV.getUserInfoAndRole(pageRequestContainer,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户状态:3不合法，只能为0、1、2",e.getMessage());
        }
    }


    /**
     * 注销账户 操作用户不是管理员
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccounts2(){
        //创建注册使用的账户
        int userId1 = createTestUserAddRole("userNameForTest1","nickNameForTest1");
        Assert.assertTrue(userRepository.findByIdAndStatus(userId1,ProjectConstants.STATUS_VALID) != null);
        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = {userId1};
        closeAccountVO.setAccountIds(accountIds);
        try {
            iUserSV.closeAccounts(closeAccountVO,userId1);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户："+userId1+"，不合法，不是管理员" ,e.getMessage());
            return;
        }
        Assert.assertTrue(false);
    }

    /**
     * 注销账户 操作用户没有关联角色
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccounts3(){
        //创建注册使用的账户
        int userId1 = createTestUser("userNameForTest1","nickNameForTest1");
        Assert.assertTrue(userRepository.findByIdAndStatus(userId1,ProjectConstants.STATUS_VALID) != null);
        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = {userId1};
        closeAccountVO.setAccountIds(accountIds);
        try {
            iUserSV.closeAccounts(closeAccountVO,userId1);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户:"+userId1+"，不合法，没有关联角色" ,e.getMessage());
            return;
        }
        Assert.assertTrue(false);
    }


    /**
     * 注销账户 正常注销
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccounts(){
        //创建注册使用的账户
        int userId1 = createTestUser("userNameForTest1","nickNameForTest1");
        Assert.assertNotNull(userRepository.findByIdAndStatus(userId1,ProjectConstants.STATUS_VALID));
        int userId2 = createTestUser("userNameForTest2" , "nickNameForTest2");
        Assert.assertNotNull(userRepository.findByIdAndStatus(userId2,ProjectConstants.STATUS_VALID));
        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = {userId1,userId2};
        closeAccountVO.setAccountIds(accountIds);
        iUserSV.closeAccounts(closeAccountVO,ProjectConstants.ACCOUNT_ROOT);
        Assert.assertNull(userRepository.findByIdAndStatus(userId1,ProjectConstants.STATUS_VALID));
        Assert.assertNull(userRepository.findByIdAndStatus(userId2,ProjectConstants.STATUS_VALID));
    }

    /**
     * 注销账户 其中一个账户不存在
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccounts1(){
        //创建注册使用的账户
        int userId1 = createTestUserAddRole("userNameForTest1","nickNameForTest1");
        Assert.assertTrue(userRepository.findByIdAndStatus(userId1,ProjectConstants.STATUS_VALID) != null);

        int userId2 = createTestUserAddRole("userNameForTest2" , "nickNameForTest2");
        Assert.assertTrue(userRepository.findByIdAndStatus(userId2,ProjectConstants.STATUS_VALID) != null);

        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = {userId1,userId2,-1};
        closeAccountVO.setAccountIds(accountIds);
        try {
            iUserSV.closeAccounts(closeAccountVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,用户:-1不存在" ,e.getMessage());
            /*Assert.assertNotNull(userRepository.findByIdAndStatus(userId2,ProjectConstants.STATUS_VALID));*/
            return;
        }
        Assert.assertTrue(false);
    }
    /**
     * 注销账户 账户为空
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccountParamsNull(){
        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = null;
        closeAccountVO.setAccountIds(accountIds);
        try {
            iUserSV.closeAccounts(closeAccountVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,注销账户不能为空" ,e.getMessage());
            return;
        }
        Assert.assertTrue(false);
    }
    /**
     * 注销账户 账户为空
     */
    @Transactional
    @Rollback
    @Test
    public void closeAccountParamsEmpty(){
        CloseAccountVO closeAccountVO = new CloseAccountVO();
        int []accountIds = {};
        closeAccountVO.setAccountIds(accountIds);
        try {
            iUserSV.closeAccounts(closeAccountVO,ProjectConstants.ACCOUNT_ROOT);
        }catch (Exception e){
            Assert.assertEquals("参数校验错误,注销账户不能为空" ,e.getMessage());
            return;
        }
        Assert.assertTrue(false);
    }



    /**
     * 创建普通用户 有角色
     * @param userName
     * @param nickName
     * @return
     */
    @Transactional
    public int createTestUserAddRole(String userName,String nickName){
        CcUserEntity ccUserEntity = new CcUserEntity();
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        ccUserEntity.setEmail("976100102@qq.com");
        ccUserEntity.setNickname(nickName);
        ccUserEntity.setPassword(SecurityUtil.getMD5Code("123456"));
        ccUserEntity.setCreateTime(TimeUtil.currentTime());
        ccUserEntity.setStatus((byte)1);
        ccUserEntity.setUsername(userName);
        userRepository.save(ccUserEntity);
        int userId = ccUserEntity.getId();

        CcUserRoleRelEntity ccUserRoleRelEntity = new CcUserRoleRelEntity();
        ccUserRoleRelEntity.setRoleId(ProjectConstants.ROLE_NORMAL);
        ccUserRoleRelEntity.setStatus(ProjectConstants.STATUS_VALID);
        ccUserRoleRelEntity.setUserId(userId);
        ccUserRoleRelEntity.setCreateTime(TimeUtil.currentTime());
        ccUserRoleRelEntity.setUpdateTime(TimeUtil.currentTime());
        userRoleRelRepository.save(ccUserRoleRelEntity);
        return userId;
    }

    /**
     * 创建普通用户 无角色
     * @param userName
     * @param nickName
     * @return
     */
    @Transactional
    public int createTestUser(String userName,String nickName){
        CcUserEntity ccUserEntity = new CcUserEntity();
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        ccUserEntity.setEmail("976100102@qq.com");
        ccUserEntity.setNickname(nickName);
        ccUserEntity.setPassword(SecurityUtil.getMD5Code("123456"));
        ccUserEntity.setCreateTime(TimeUtil.currentTime());
        ccUserEntity.setStatus((byte)1);
        ccUserEntity.setUsername(userName);
        userRepository.save(ccUserEntity);
        int userId = ccUserEntity.getId();
        return userId;
    }

    @Transactional
    @Rollback
    @Test
    public void auditAccounts(){

    }
    @Test
    public void findAppManager(){
        List<CcUserEntity> list = iUserSV.findAppManager(100002);
        System.out.println(1);
    }


}