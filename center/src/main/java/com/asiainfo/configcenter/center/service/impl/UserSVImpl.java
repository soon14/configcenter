package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.UserMapper;
import com.asiainfo.configcenter.center.dao.repository.*;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.entity.complex.CXCcUserInfoEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.*;
import com.asiainfo.configcenter.center.vo.user.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息服务实现类
 * Created by bawy on 2018/7/3.
 */
@Service
public class UserSVImpl implements IUserSV {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserExtInfoRepository userExtInfoRepository;

    @Resource
    private UserRoleRelRepository userRoleRelRepository;

    @Resource
    private AuthCodeRepository authCodeRepository;

    @Resource
    private INotificationSV notificationSV;

    @Resource
    private IOrgSV orgSV;

    @Resource
    private ITaskSV iTaskSV;

    @Resource
    private IAppSV iAppSV;

    @Resource
    private UserMapper userMapper;

    @Resource
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String from;

    //管理员集合
    //private List<Integer> administrators;
    @Resource
    private ValueOperations<String, List<Integer>> valueOperations;

    @Override
    public UserInfoVO login(String username, String password) {
        Assert4CC.hasLength(username, "用户名不能为空");
        Assert4CC.hasLength(password, "密码不能为空");
        password = SecurityUtil.getMD5Code(password);
        CcUserEntity userEntity = userRepository.findByUsernameAndPassword(username, password);
        Assert4CC.notNull(userEntity, ResultCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        byte status = userEntity.getStatus();
        Assert4CC.isTrue(status == ProjectConstants.STATUS_VALID, ResultCodeEnum.USER_STATUS_ABNORMAL, status == ProjectConstants.STATUS_AUDIT?"等待审核中":"已经失效");
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userEntity.getId());
        userInfoVO.setUsername(userEntity.getUsername());
        userInfoVO.setNickname(userEntity.getNickname());
        userInfoVO.setEmail(userEntity.getEmail());
        userInfoVO.setPhone(getUserExtInfoByKey(userEntity.getId(), ProjectConstants.USER_EXT_INFO_PHONE));
        userInfoVO.setIsAdmin(isAdministrator(userEntity.getId()));
        userInfoVO.setCreateTime(userEntity.getCreateTime());
        userInfoVO.setUpdateTime(userEntity.getUpdateTime());
        return userInfoVO;
    }


    @Override
    public int getRoleIdByUserId(int userId) {
        CcUserRoleRelEntity userRoleRelEntity = userRoleRelRepository.findByUserIdAndStatus(userId, ProjectConstants.STATUS_VALID);
        return userRoleRelEntity.getRoleId();
    }

    @Override
    @Transactional
    public String register(RegisterReqVO registerReqVO) {
        String username = registerReqVO.getUsername();
        String password = registerReqVO.getPassword();
        String email = registerReqVO.getEmail();
        int orgId = registerReqVO.getOrgId();
        int orgRoleId = registerReqVO.getOrgRoleId();
        Assert4CC.hasLength(username, "用户名不可为空");
        Assert4CC.hasLength(password, "密码不可为空");
        Assert4CC.hasLength(email,"邮箱不可为空");
        Assert4CC.isTrue(orgId>0, "所属一级组织必选");
        Assert4CC.isTrue(orgRoleId>0, "角色必选");
        //校验参数是否符合规则
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_USERNAME, username), "用户名不符合规则");
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_PASSWORD, password), "密码不符合规则");
        //校验邮箱和帐号是否已经存在
        Assert4CC.isNull(userRepository.findByEmailAndStatusNot(email, ProjectConstants.STATUS_INVALID), ResultCodeEnum.EMAIL_IS_USE);
        Assert4CC.isNull(userRepository.findByUsernameAndStatusNot(username, ProjectConstants.STATUS_INVALID), ResultCodeEnum.USERNAME_IS_USE);
        //保存用户信息
        CcUserEntity userEntity = new CcUserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(SecurityUtil.getMD5Code(password));
        userEntity.setEmail(email);
        userEntity.setNickname(registerReqVO.getNickname());
        userEntity.setStatus(ProjectConstants.STATUS_AUDIT);
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        userEntity.setCreateTime(nowTime);
        userEntity.setUpdateTime(nowTime);
        userEntity = userRepository.saveAndFlush(userEntity);
        //保存用户角色
        CcUserRoleRelEntity userRoleRelEntity = new CcUserRoleRelEntity();
        userRoleRelEntity.setUserId(userEntity.getId());
        userRoleRelEntity.setRoleId(ProjectConstants.ROLE_NORMAL);
        userRoleRelEntity.setCreateTime(nowTime);
        userRoleRelEntity.setUpdateTime(nowTime);
        userRoleRelEntity.setStatus(ProjectConstants.STATUS_VALID);
        userRoleRelRepository.saveAndFlush(userRoleRelEntity);
        //加入一级组织
        orgSV.addUserToLevelOrg(userEntity.getId(), orgId, orgRoleId);
        //生成消息
        CcNotificationEntity notificationEntity = NotificationUtil.createNotification(NotificationUtil.NOTIFICATION_TYPE_REGISTER, "注册审核", "用户" + username + "注册，等待审批", ProjectConstants.ACCOUNT_ROOT, userEntity.getId(), null);
        notificationSV.createNotification(notificationEntity);
        return "注册成功，等待管理员审核";
    }

    @Transactional
    @Override
    public void updateUserInfo(UserInfoReqVO userInfoReqVO, int userId) {
        //校验数据
        checkUserInfoReqVO(userInfoReqVO);

        //查询用户信息
        CcUserEntity ccUserEntity = userRepository.findByIdAndStatus(userId , ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(ccUserEntity,"用户主键:" + userId + "用户不存在");

        //更新数据并保存
        ccUserEntity.setNickname(userInfoReqVO.getNickname());
        ccUserEntity.setEmail(userInfoReqVO.getEmail());
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());

        //更新手机信息
        CcUserExtInfoEntity ccUserExtInfoEntity = updatePhoneInfo(userInfoReqVO.getPhone(),userId);

        //保存
        userRepository.save(ccUserEntity);
        if(ccUserExtInfoEntity != null){
            userExtInfoRepository.save(ccUserExtInfoEntity);
        }
    }

    /**
     * 更新用户信息时校验用户信息
     * @param userInfoReqVO 用户信息
     * @author oulc
     * @date 2018/7/17 14:45
     */
    private void checkUserInfoReqVO(UserInfoReqVO userInfoReqVO){
        //校验昵称不能为空
        Assert4CC.hasLength(userInfoReqVO.getNickname(),"昵称不能为空");
        //校验邮箱不能为空
        Assert4CC.hasLength(userInfoReqVO.getEmail(),"邮箱不能为空");
        //校验邮箱格式
        Assert4CC.isTrue(ValidateUtil.checkMail(userInfoReqVO.getEmail()) , "邮箱格式不正确");

        //如果有手机号码则校验手机号码格式
        if(StringUtils.isNotBlank(userInfoReqVO.getPhone())){
            Assert4CC.isTrue(ValidateUtil.checkPhone(userInfoReqVO.getPhone()),"手机号码格式不正确");
        }
    }

    /**
     * 更新手机信息
     * @param phone 手机号码
     * @param userId 用户信息
     * @return 实体
     * @author oulc
     * @date 2018/7/17 15:30
     */
    private CcUserExtInfoEntity updatePhoneInfo(String phone ,int userId){
        //查询用户扩展信息(手机)
        CcUserExtInfoEntity ccUserExtInfoEntity = userExtInfoRepository.findByUserIdAndExtInfoKeyAndStatus(userId,ProjectConstants.USER_EXT_INFO_PHONE,ProjectConstants.STATUS_VALID);
        if(StringUtils.isNotBlank(phone)){
            if(ccUserExtInfoEntity ==  null){
                //新增手机扩展信息
                ccUserExtInfoEntity = createPhoneEntity(userId,phone);
            }else{
                //更新手机扩展信息
                ccUserExtInfoEntity.setExtInfoValue(phone);
                ccUserExtInfoEntity.setUpdateTime(TimeUtil.currentTime());
            }
        }else{
            if(ccUserExtInfoEntity !=  null){
                //删除手机扩展信息
                ccUserExtInfoEntity.setStatus(ProjectConstants.STATUS_INVALID);
                ccUserExtInfoEntity.setUpdateTime(TimeUtil.currentTime());
            }
        }
        return ccUserExtInfoEntity;
    }

    /**
     * 创建手机扩展实体
     * @param userId 用户主键
     * @param phone 手机号码
     * @return 实体类
     * @author oulc
     * @date 2018/7/17 15:19
     */
    private CcUserExtInfoEntity createPhoneEntity(int userId , String phone){
        CcUserExtInfoEntity ccUserExtInfoEntity = new CcUserExtInfoEntity();
        ccUserExtInfoEntity.setExtInfoKey(ProjectConstants.USER_EXT_INFO_PHONE);
        ccUserExtInfoEntity.setExtInfoValue(phone);
        ccUserExtInfoEntity.setUserId(userId);
        ccUserExtInfoEntity.setStatus(ProjectConstants.STATUS_VALID);
        ccUserExtInfoEntity.setCreateTime(TimeUtil.currentTime());
        ccUserExtInfoEntity.setUpdateTime(TimeUtil.currentTime());
        return ccUserExtInfoEntity;
    }

    @Override
    public PageResultContainer<CXCcUserInfoEntity> getUserInfoAndRole(PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer, int userId) {
        ValidateUtil.checkPageParam(pageRequestContainer);
        //校验数据
        checkUserInfoAndRoleReqVO(pageRequestContainer,userId);
        //查询
        int page = pageRequestContainer.getCurrentPage();
        int size = pageRequestContainer.getPageSize();
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = pageRequestContainer.getData();
        String nickName = userInfoAndRoleReqVO.getNickName();
        int userStatus = userInfoAndRoleReqVO.getUserStatus();

        List<CXCcUserInfoEntity> list = userMapper.findCcUserInfoComplexInfo(nickName,userStatus,page*size,size);
        long count = userMapper.findCcUserInfoComplexInfoCount(nickName,userStatus);

        PageResultContainer<CXCcUserInfoEntity> pageResultContainer = new PageResultContainer<>();
        pageResultContainer.setCount(count);
        pageResultContainer.setEntities(list);
        return pageResultContainer;
    }

    @Transactional
    @Override
    public void updatePassword(PasswordVO passwordVO, int userId) {
        String oldPass = passwordVO.getOldPass();
        String newPass = passwordVO.getNewPass();
        Assert4CC.notNull(oldPass, "原密码不可为空");
        Assert4CC.notNull(newPass, "新密码不可为空");
        //校验参数是否符合规则
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_PASSWORD, newPass), "新密码不符合规则");
        //查询用户信息
        CcUserEntity ccUserEntity = userRepository.findByIdAndStatus(userId , ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(ccUserEntity,ResultCodeEnum.USER_STATUS_ABNORMAL,"当前登录用户已失效");
        Assert4CC.isTrue(SecurityUtil.getMD5Code(oldPass).equals(ccUserEntity.getPassword()), ResultCodeEnum.OLD_PASSWORD_ERROR);
        ccUserEntity.setPassword(SecurityUtil.getMD5Code(newPass));
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        userRepository.saveAndFlush(ccUserEntity);
    }


    private void checkUserInfoAndRoleReqVO(PageRequestContainer<UserInfoAndRoleReqVO> pageRequestContainer,int userId){
        //校验当前用户是否时管理员用户
        Assert4CC.isTrue(isAdministrator(userId) , ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "当前用户:"+userId+"不是管理员");

        //校验当前页
        ValidateUtil.checkPageParam(pageRequestContainer);

        //校验用户状态
        UserInfoAndRoleReqVO userInfoAndRoleReqVO = pageRequestContainer.getData();
        if(userInfoAndRoleReqVO != null && userInfoAndRoleReqVO.getUserStatus() != -1){
            int userStatus = userInfoAndRoleReqVO.getUserStatus();
            Assert4CC.isTrue( userStatus == 0 || userStatus == 1 || userStatus == 2 ,"用户状态:"+userStatus+"不合法，只能为0、1、2");
        }
    }



    @Transactional
    @Override
    public void closeAccounts(CloseAccountVO closeAccountVO, int userId) {
        //校验入参
        checkCloseAccountVO(closeAccountVO,userId);

        //删除账户
        int[] accounts = closeAccountVO.getAccountIds();
        for(int accountId : accounts){
            closeAccount(accountId);
        }
    }

    /**
     * 注销账户校验
     * @param closeAccountVO 账户信息
     * @param userId 操作用户
     * @author oulc
     * @date 2018/7/18 15:26
     */
    private void checkCloseAccountVO(CloseAccountVO closeAccountVO, int userId){
        //校验当前用户是否时管理员用户
        Assert4CC.isTrue(isAdministrator(userId) , ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "当前用户:"+userId+"不是管理员");

        //校验注销的账户不能为空
        Assert4CC.notNull(closeAccountVO.getAccountIds(),"注销账户不能为空");

        //校验注销账户不能为空
        Assert4CC.isTrue(closeAccountVO.getAccountIds().length > 0,"注销账户不能为空");
    }

    @Transactional
    @Override
    public void closeAccount(int userId){
        CcUserEntity ccUserEntity = userRepository.findByIdAndStatus(userId,ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(ccUserEntity,"用户:" + userId + "不存在");
        //删除用户之前 校验
        closeAccountPrepare(userId);
        ccUserEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        userRepository.save(ccUserEntity);
    }

    @Override
    public void closeAccountPrepare(int userId) {
        //校验用户是否为管理员
        if (isAdministrator(userId)){
            throw new ErrorCodeException(ResultCodeEnum.USER_COMMON_ERROR, "不可以将管理员帐号置失效");
        }
        //校验用户已经没有未完成的任务
        iTaskSV.checkUserHasNotUnfinishedTask(userId);
        //判断用户是某个组织的领导
        orgSV.checkUserIsNotOrgLeader(userId);
        //判断用户是某个应用的开发经理
        iAppSV.checkUserIsNotAppManager(userId);
    }

    @Transactional
    @Override
    public void auditAccounts(AuditAccountVO auditAccountVO, int userId) {
        //校验参数
        checkAuditAccountVO(auditAccountVO,userId);

        //审核
        int operate = auditAccountVO.getOperate();
        int []accountIds = auditAccountVO.getAccountIds();
        for( int accountId : accountIds){
            auditAccount(accountId,operate);
        }
    }

    /**
     * 校验
     * @param auditAccountVO 审核信息
     * @param userId 操作用户
     * @author oulc
     * @date 2018/7/19 9:54
     */
    private void checkAuditAccountVO(AuditAccountVO auditAccountVO, int userId){
        //校验当前用户是否时管理员用户
        Assert4CC.isTrue(isAdministrator(userId) , ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "当前用户:"+userId+"不是管理员");

        //校验注销的账户不能为空
        Assert4CC.notNull(auditAccountVO.getAccountIds(),"注销账户不能为空");

        //校验注销账户不能为空
        Assert4CC.notNull(auditAccountVO.getAccountIds().length > 0,"注销账户不能为空");

        //校验操作合法
        int operate = auditAccountVO.getOperate();
        Assert4CC.isTrue(operate == 1 || operate == 2 ,"operate:"+operate+"操作不合法");
    }

    @Transactional
    @Override
    public void auditAccount(int userId,int operate) {
        CcUserEntity ccUserEntity = userRepository.findOne(userId);
        Assert4CC.notNull(ccUserEntity,"账户:"+userId+"不存在");
        Assert4CC.isTrue(ccUserEntity.getStatus() == ProjectConstants.STATUS_AUDIT ,"账户:"+userId+"不是待审核状态");
        if(operate == 1){
            ccUserEntity.setStatus(ProjectConstants.STATUS_VALID);
            ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        }else if(operate == 2){
            ccUserEntity.setStatus(ProjectConstants.STATUS_INVALID);
            ccUserEntity.setUpdateTime(TimeUtil.currentTime());
        }
        userRepository.save(ccUserEntity);
    }

    @Override
    @Transactional
    public void getAuthCode(String username, String email) {
        Assert4CC.notNull(username, "用户名不可为空");
        Assert4CC.notNull(email, "邮箱不可为空");
        CcUserEntity userEntity = userRepository.findByUsernameAndEmailAndStatus(username, email, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(userEntity, ResultCodeEnum.SYSTEM_COMMON_ERROR, "用户名或邮箱不正确，请返回上一步进行修改");
        String number = NumberUtil.getRandomStr(10);
        CcAuthCodeEntity authCodeEntity = new CcAuthCodeEntity();
        authCodeEntity.setCode(number);
        authCodeEntity.setUserId(userEntity.getId());
        authCodeEntity.setCreateTime(TimeUtil.currentTime());
        authCodeEntity.setExpireTime(TimeUtil.afterTime(1800));
        authCodeEntity.setStatus(ProjectConstants.STATUS_VALID);
        authCodeRepository.saveAndFlush(authCodeEntity);
        SendEmailUtil sendEmail = new SendEmailUtil(sender, from);
        sendEmail.setTitle("密码重置");
        sendEmail.setContent("您的随机验证码为<b>"+number+"</b>,30分钟内有效。");
        sendEmail.addAddress(userEntity.getEmail());
        new Thread(sendEmail).start();
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordVO resetPasswordVO) {
        String code = resetPasswordVO.getCode();
        String newPass = resetPasswordVO.getNewPass();
        Assert4CC.notNull(code, "验证码不可为空");
        Assert4CC.notNull(newPass, "密码不可为空");
        Assert4CC.isTrue(ValidateUtil.check(ValidateUtil.CHECK_PASSWORD, newPass), "新密码不符合规则");
        CcAuthCodeEntity authCodeEntity = authCodeRepository.findByCodeAndStatusAndExpireTimeAfter(code, ProjectConstants.STATUS_VALID, TimeUtil.currentTime());
        Assert4CC.notNull(authCodeEntity, "验证码错误");
        authCodeEntity.setStatus(ProjectConstants.STATUS_INVALID);
        authCodeRepository.saveAndFlush(authCodeEntity);
        newPass = SecurityUtil.getMD5Code(newPass);
        userRepository.updatePassword(authCodeEntity.getUserId(), newPass, TimeUtil.currentTime());
    }

    /**
     * 根据key获取用户对应的扩展信息
     * @author bawy
     * @date 2018/7/18 17:27
     * @param userId 用户标识
     * @param key 关键字
     * @return 对应扩展信息
     */
    private String getUserExtInfoByKey(int userId, String key){
        CcUserExtInfoEntity ccUserExtInfoEntity = userExtInfoRepository.findByUserIdAndExtInfoKeyAndStatus(userId, key, ProjectConstants.STATUS_VALID);
        if (ccUserExtInfoEntity == null){
            return null;
        }else{
            return ccUserExtInfoEntity.getExtInfoValue();
        }
    }

    @Override
    public boolean isAdministrator(int userId) {
        List<Integer> administrators = valueOperations.get(ProjectConstants.REDIS_ADMINISTRATOR_LIST_KEY);
        if(administrators == null){
            List<CcUserRoleRelEntity> userRoleRelEntities = userRoleRelRepository.findByRoleIdAndStatus(ProjectConstants.ROLE_ROOT, ProjectConstants.STATUS_VALID);
            if (userRoleRelEntities != null){
                administrators = new ArrayList<>();
                for (CcUserRoleRelEntity userRoleRelEntity:userRoleRelEntities){
                    administrators.add(userRoleRelEntity.getUserId());
                }
            }
            Assert4CC.notNull(administrators, ResultCodeEnum.SYSTEM_COMMON_ERROR, "数据异常，无法获取管理员列表");
            valueOperations.set(ProjectConstants.REDIS_ADMINISTRATOR_LIST_KEY, administrators);
        }
        return administrators.contains(userId);
    }

    @Override
    public PageResultContainer<CcUserEntity> getUserList(PageRequestContainer<UserInfoReqVO> pageRequestContainer) {
        ValidateUtil.checkPageParam(pageRequestContainer);
        UserInfoReqVO requestParams = pageRequestContainer.getData();
        Pageable pageable = new PageRequest(pageRequestContainer.getCurrentPage(), pageRequestContainer.getPageSize(), Sort.Direction.ASC, "id");
        Page<CcUserEntity> page = userRepository.findAll(new Specification<CcUserEntity>(){
            @Override
            public Predicate toPredicate(Root<CcUserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotEmpty(requestParams.getUsername())){
                    list.add(criteriaBuilder.like(root.get("username").as(String.class), "%"+requestParams.getUsername()+"%"));
                }
                if(StringUtils.isNotEmpty(requestParams.getNickname())){
                    list.add(criteriaBuilder.like(root.get("nickname").as(String.class), "%"+requestParams.getNickname()+"%"));
                }
                list.add(criteriaBuilder.equal(root.get("status").as(byte.class), ProjectConstants.STATUS_VALID));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return new PageResultContainer<>(page);
    }

    @Override
    public List<CcUserEntity> findAppManager(int appId) {
        return userRepository.findAppManager(appId);
    }

    @Override
    public void cleanCache() {
        valueOperations.getOperations().delete(ProjectConstants.REDIS_ADMINISTRATOR_LIST_KEY);
    }

    @Override
    public CcUserEntity getUserById(int userId) {
        return userRepository.findByIdAndStatus(userId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public List<CcUserEntity> getAllUserByOrgId(int orgId) {
        return userRepository.findUserByOrgId(orgId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public List<CcUserEntity> getAllUserByOrgIdAndRoleId(int orgId, int roleId){
        return userRepository.findUserByOrgIdAndRoleId(orgId, roleId, ProjectConstants.STATUS_VALID);
    }

}
