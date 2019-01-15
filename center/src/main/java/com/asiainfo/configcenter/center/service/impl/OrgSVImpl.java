package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.OrgMapper;
import com.asiainfo.configcenter.center.dao.repository.OrgRepository;
import com.asiainfo.configcenter.center.dao.repository.OrgUserRelRepository;
import com.asiainfo.configcenter.center.entity.CcOrgEntity;
import com.asiainfo.configcenter.center.entity.CcOrgUserRelEntity;
import com.asiainfo.configcenter.center.entity.CcRoleEntity;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.service.interfaces.IOrgSV;
import com.asiainfo.configcenter.center.service.interfaces.IRoleSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.org.*;
import com.asiainfo.configcenter.center.vo.system.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 组织服务实现类
 * Created by bawy on 2018/8/16 20:07.
 */
@Service
public class OrgSVImpl implements IOrgSV {

    @Resource
    private OrgRepository orgRepository;

    @Resource
    private OrgUserRelRepository orgUserRelRepository;

    @Resource
    private OrgMapper orgMapper;

    @Resource
    private IUserSV userSV;

    @Resource
    private IRoleSV roleSV;



    @Override
    public List<CcOrgEntity> getAllLevelOneOrg() {
        return orgRepository.findByLevelAndStatus((byte)1, ProjectConstants.STATUS_VALID);
    }

    @Override
    public List<OrgTreeVO> getLevelOneOrg(int userId) {
        if (userSV.isAdministrator(userId)){
            return getAllOrgTree();
        }else{
            CcOrgEntity orgEntity = orgRepository.findParentByUserIdAndStatus(userId, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(orgEntity, ResultCodeEnum.DATA_ERROR, "当前用户未加入任何组织");
            CcOrgEntity levelOneOrg = getLevelOneOrgByOrg(orgEntity);
            OrgTreeVO orgTree = PermissionCommon.getOrgTreeByOrgId(levelOneOrg.getId());
            List<OrgTreeVO> orgTrees = new ArrayList<>();
            orgTrees.add(orgTree);
            return orgTrees;
        }
    }

    @Override
    public List<OrgTreeVO> getAllOrgTree() {
        return PermissionCommon.getOrgTreeByOrgId(0).getChildren();
    }

    @Override
    public List<CcOrgEntity> getAllOrg() {
        return orgRepository.findByStatusOrderByLevelAsc(ProjectConstants.STATUS_VALID);
    }

    @Override
    @Transactional
    public String addOrg(AddOrgReqVO addOrgReq, int creator) {
        int orgId = addOrgReq.getOrgId();
        String name = addOrgReq.getName();
        Assert4CC.isTrue(orgId>=0, "上级组织标识不可为空");
        Assert4CC.notNull(name, "组织名称不可为空");
        checkIsLeader(orgId, creator);
        byte level = 1;
        CcOrgUserRelEntity orgLeaderRel = null;
        if (orgId != 0){
            //在指定组织下添加组织，需要校验上级组织是否存在
            CcOrgEntity parentOrg = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(parentOrg, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织"+orgId+"不存在");
            level = (byte)(parentOrg.getLevel()+1);
            Assert4CC.isTrue(level<=5, ResultCodeEnum.ORG_LEVEL_LIMIT);
            orgLeaderRel = setOrgLeader(parentOrg, addOrgReq.getLeader());
        }
        CcOrgEntity orgEntity = new CcOrgEntity();
        orgEntity.setParentId(orgId);
        orgEntity.setLevel(level);
        orgEntity.setName(name);
        orgEntity.setDescription(addOrgReq.getDescription());
        orgEntity.setLeader(addOrgReq.getLeader());
        orgEntity.setEmail(addOrgReq.getEmail());
        Timestamp currentTime = TimeUtil.currentTime();
        orgEntity.setCreator(creator);
        orgEntity.setCreateTime(currentTime);
        orgEntity.setModifier(creator);
        orgEntity.setUpdateTime(currentTime);
        orgEntity.setStatus(ProjectConstants.STATUS_VALID);
        orgRepository.save(orgEntity);
        if (orgLeaderRel!=null){
            //将设置的领导加入组织
            orgLeaderRel.setOrgId(orgEntity.getId());
            orgLeaderRel.setModifier(creator);
            orgLeaderRel.setUpdateTime(currentTime);
            orgUserRelRepository.save(orgLeaderRel);
        }
        PermissionCommon.updateOrgTree(ProjectConstants.ORG_CHANGE_TYPE_ADD, orgEntity);
        return "新增组织成功";
    }

    @Override
    @Transactional
    public String delOrg(DelOrgReqVO delOrgReqVO, int modifier) {
        int orgId = delOrgReqVO.getOrgId();
        Assert4CC.isTrue(orgId>0, "被删除组织标识不可为空");
        checkIsParentLeader(orgId, modifier);
        //判断组织是否存在并且有效
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织（ID："+orgId+"）不存在或已经移除");
        //判断组织是否存在下级组织
        List<CcOrgEntity> childrenOrgs = orgRepository.findByParentIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.isTrue(childrenOrgs==null||childrenOrgs.size()==0, ResultCodeEnum.ORG_CAN_NOT_DEL_ERROR, "组织中存在下级组织，请先移除所有下级组织");
        //判断组织中是否有用户
        List<CcOrgUserRelEntity> orgUserRelEntities = orgUserRelRepository.findByOrgIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.isTrue(orgUserRelEntities==null||orgUserRelEntities.size()==0, ResultCodeEnum.ORG_CAN_NOT_DEL_ERROR, "组织中存在用户，请先移除所有用户");
        orgEntity.setModifier(modifier);
        orgEntity.setUpdateTime(TimeUtil.currentTime());
        orgEntity.setStatus(ProjectConstants.STATUS_INVALID);
        orgRepository.save(orgEntity);
        PermissionCommon.updateOrgTree(ProjectConstants.ORG_CHANGE_TYPE_DEL, orgEntity);
        return "删除组织成功";
    }

    @Override
    @Transactional
    public String modOrg(ModOrgReqVO modOrgReq, int modifier) {
        int orgId = modOrgReq.getOrgId();
        String name = modOrgReq.getName();
        Assert4CC.isTrue(orgId>=0, "上级组织标识不可为空");
        Assert4CC.notNull(name, "组织名称不可为空");
        checkIsParentLeader(orgId, modifier);
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织"+orgId+"不存在");
        orgEntity.setDescription(modOrgReq.getDescription());
        orgEntity.setLeader(modOrgReq.getLeader());
        orgEntity.setEmail(modOrgReq.getEmail());
        orgEntity.setName(modOrgReq.getName());
        orgEntity.setModifier(modifier);
        orgEntity.setUpdateTime(TimeUtil.currentTime());
        orgRepository.save(orgEntity);
        //修改组织领导者
        changeOrgLeader(orgEntity, orgEntity.getLeader(), modOrgReq.getLeader(), modifier);
        PermissionCommon.updateOrgTree(ProjectConstants.ORG_CHANGE_TYPE_MOD, orgEntity);
        return "修改组织成功";
    }

    @Override
    @Transactional
    public String moveUser(MoveUserToOrgReqVO moveUserToOrgReq, int modifier) {
        int userId = moveUserToOrgReq.getUserId();
        int orgId = moveUserToOrgReq.getOrgId();
        int targetOrgId = moveUserToOrgReq.getTargetOrgId();
        Assert4CC.isTrue(userId>0, "用户标识不可为空");
        Assert4CC.isTrue(orgId>0, "用户原组织标识不可为空");
        Assert4CC.isTrue(targetOrgId>0, "目标组织标识不可为空");
        checkIsLeader(orgId, modifier);
        //获取目标组织
        CcOrgEntity targetOrgEntity = orgRepository.findByIdAndStatus(targetOrgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(targetOrgEntity, ResultCodeEnum.DATA_ERROR, "无法获取目标组织"+targetOrgId+"的数据");
        //获取用户原本所属组织
        CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByOrgIdAndUserIdAndStatus(orgId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgUserRelEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "用户"+userId+"不在组织"+orgId+"中");
        CcOrgEntity sourceOrgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(sourceOrgEntity, ResultCodeEnum.DATA_ERROR, "无法获取用户原组织"+orgId+"的数据");
        if (isLeader(orgId, userId)){
            throw new ErrorCodeException(ResultCodeEnum.PARAM_ERROR, "该用户是当前组织的领导者，不可以移动，请先将组织领导者设置为其他人");
        }
        //获取用户原本所属组织的一级组织
        CcOrgEntity sourceLevelOneOrg = getLevelOneOrgByOrg(sourceOrgEntity);
        //获取目标组织所属一级组织
        CcOrgEntity targetLevelOneOrg = getLevelOneOrgByOrg(targetOrgEntity);
        Assert4CC.isTrue(targetLevelOneOrg.getId()==sourceLevelOneOrg.getId(), ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "目标组织所属一级组织与用户当前所属一级组织不一致，无法调整");
        //调整组织
        orgUserRelEntity.setOrgId(targetOrgId);
        orgUserRelEntity.setModifier(modifier);
        orgUserRelEntity.setUpdateTime(TimeUtil.currentTime());
        orgUserRelRepository.save(orgUserRelEntity);
        return "移动用户到新的组织成功";
    }

    @Override
    public PageResultContainer<OrgUserRelVO> queryUsers(PageRequestContainer<QueryOrgUserRelReqVO> requestContainer) {
        ValidateUtil.checkPageParam(requestContainer);
        QueryOrgUserRelReqVO queryOrgUserRelReq = requestContainer.getData();
        int orgId = queryOrgUserRelReq.getOrgId();
        Assert4CC.isTrue(orgId>0, "组织标识不可为空");
        String nickname = queryOrgUserRelReq.getNickname();
        String roleName = queryOrgUserRelReq.getRoleName();
        List<OrgUserRelVO> orgUserRels = orgMapper.queryUsers(orgId, nickname, roleName, requestContainer.getCurrentPage()*requestContainer.getPageSize(), requestContainer.getPageSize());
        int count = orgMapper.queryUsersCount(orgId, nickname, roleName);
        return new PageResultContainer<>(orgUserRels, count);
    }

    @Override
    @Transactional
    public String modUserRole(ModOrgUserRelReqVO modOrgUserRelReq, int modifier) {
        int orgId = modOrgUserRelReq.getOrgId();
        int userId = modOrgUserRelReq.getUserId();
        int newRoleId = modOrgUserRelReq.getNewRoleId();
        Assert4CC.isTrue(orgId>0, "组织标识不可为空");
        Assert4CC.isTrue(userId>0, "用户标识不可为空");
        Assert4CC.isTrue(newRoleId>0, "新角色标识不可为空");
        checkIsLeader(orgId, modifier);
        CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByOrgIdAndUserIdAndStatus(orgId, userId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgUserRelEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织（ID："+orgId+"）中不存在用户（ID："+userId+"）");
        CcRoleEntity roleEntity = roleSV.getRoleByIdAndRoleType(newRoleId, ProjectConstants.ROLE_TYPE_ORG);
        Assert4CC.notNull(roleEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "角色不存在");
        orgUserRelEntity.setRoleId(newRoleId);
        orgUserRelEntity.setModifier(modifier);
        orgUserRelEntity.setUpdateTime(TimeUtil.currentTime());
        orgUserRelRepository.save(orgUserRelEntity);
        return "修改用户角色成功";
    }

    @Override
    public CcOrgEntity getParentOrg(CcOrgEntity orgEntity) {
        //一级组织的上级组织直接返回null
        if (orgEntity.getLevel() == 1){
            return null;
        }
        CcOrgEntity parentOrgEntity = orgRepository.findByIdAndStatus(orgEntity.getParentId(), ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(parentOrgEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgEntity.getParentId()+"的上级组织");
        return parentOrgEntity;
    }

    /**
     * 获取指定组织的一级组织
     * @author bawy
     * @date 2018/8/20 15:36
     * @param orgEntity 组织
     * @return 所属一级组织
     */
    private CcOrgEntity getLevelOneOrgByOrg(CcOrgEntity orgEntity){
        while (orgEntity.getLevel()>1){
            orgEntity = getParentOrg(orgEntity);
        }
        return orgEntity;
    }

    @Override
    public OrgDetailInfoVO getOrgInfo(int orgId, int userId) {
        Assert4CC.isTrue(orgId>0, "组织标识不可为空");
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        OrgDetailInfoVO orgDetailInfo = new OrgDetailInfoVO();
        orgDetailInfo.setId(orgId);
        orgDetailInfo.setName(orgEntity.getName());
        byte level = orgEntity.getLevel();
        orgDetailInfo.setLevel(level);
        orgDetailInfo.setDescription(orgEntity.getDescription());
        orgDetailInfo.setEmail(orgEntity.getEmail());
        if (level>1){
            CcOrgEntity parentOrgEntity = orgRepository.findByIdAndStatus(orgEntity.getParentId(), ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(parentOrgEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgId+"的父级组织");
            orgDetailInfo.setParentOrgName(parentOrgEntity.getName());
        }
        Integer leaderId = orgEntity.getLeader();
        if (leaderId!=null){
            orgDetailInfo.setLeader(leaderId);
            CcUserEntity userEntity = userSV.getUserById(leaderId);
            Assert4CC.notNull(userEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgId+"的领导者");
            orgDetailInfo.setLeaderName(userEntity.getNickname());
        }
        return orgDetailInfo;
    }

    @Override
    @Transactional
    public void addUserToLevelOrg(int userId, int orgId, int roleId) {
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgId+"的信息");
        Assert4CC.isTrue(orgEntity.getLevel()==1, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "组织"+orgId+"不是一级组织");
        CcRoleEntity roleEntity = roleSV.getRoleByIdAndRoleType(roleId, ProjectConstants.ROLE_TYPE_ORG);
        Assert4CC.notNull(roleEntity, ResultCodeEnum.DATA_ERROR, "组织角色"+roleId+"不存在");
        orgUserRelRepository.save(createOrgUserRel(userId, orgEntity, roleId));
    }

    @Override
    @Transactional
    public void addUserToOrg(int userId, int orgId, int roleId) {
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgId+"的信息");
        CcRoleEntity roleEntity = roleSV.getRoleByIdAndRoleType(roleId, ProjectConstants.ROLE_TYPE_ORG);
        Assert4CC.notNull(roleEntity, ResultCodeEnum.DATA_ERROR, "组织角色"+roleId+"不存在");
        orgUserRelRepository.save(createOrgUserRel(userId, orgEntity, roleId));
    }

    @Override
    public List<RoleVO> getOrgRole() {
        return roleSV.getAllRoleByRoleType(ProjectConstants.ROLE_TYPE_ORG);
    }

    @Override
    public List<OrgUserVO> getOrgUsers(int orgId, int userId) {
        CcOrgEntity orgEntity = orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(orgEntity, ResultCodeEnum.DATA_ERROR, "无法获取组织"+orgId+"的数据");
        int leader = orgEntity.getLeader()==null?0:orgEntity.getLeader();
        List<CcUserEntity> userEntities = userSV.getAllUserByOrgId(orgId);
        List<OrgUserVO> orgUsers = new ArrayList<>();
        if (userEntities!=null){
            for (CcUserEntity userEntity:userEntities) {
                if (userEntity.getId()!=leader) {
                    OrgUserVO orgUser = new OrgUserVO();
                    orgUser.setUserId(userEntity.getId());
                    orgUser.setNickname(userEntity.getNickname());
                    orgUsers.add(orgUser);
                }
            }
        }
        return orgUsers;
    }

    @Override
    public List<OrgUserVO> getOrgUsers(int orgId, int roleId, int userId) {
        if (orgId == 0){
            CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByUserIdAndStatus(userId, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(orgUserRelEntity, "无法获取当前登录人员"+userId+"所属组织");
            orgId = orgUserRelEntity.getOrgId();
        }
        List<CcUserEntity> userEntities = userSV.getAllUserByOrgIdAndRoleId(orgId, roleId);
        List<OrgUserVO> orgUsers = new ArrayList<>();
        if (userEntities!=null){
            for (CcUserEntity userEntity:userEntities) {
                OrgUserVO orgUser = new OrgUserVO();
                orgUser.setUserId(userEntity.getId());
                orgUser.setNickname(userEntity.getNickname());
                orgUsers.add(orgUser);
            }
        }
        return orgUsers;
    }

    /**
     * 将用户加入组织
     * @author bawy
     * @date 2018/8/21 10:46
     * @param userId 用户标识
     * @param orgEntity 组织标识
     * @param roleId 角色标识
     * @return 组织用户关系对象
     */
    private CcOrgUserRelEntity createOrgUserRel(int userId, CcOrgEntity orgEntity, int roleId) {
        CcOrgUserRelEntity orgUserRelEntity = new CcOrgUserRelEntity();
        orgUserRelEntity.setOrgId(orgEntity.getId());
        orgUserRelEntity.setUserId(userId);
        orgUserRelEntity.setRoleId(roleId);
        orgUserRelEntity.setOrgLevel(orgEntity.getLevel());
        Timestamp currentTime = TimeUtil.currentTime();
        orgUserRelEntity.setCreator(ProjectConstants.ACCOUNT_ROOT);
        orgUserRelEntity.setCreateTime(currentTime);
        orgUserRelEntity.setModifier(ProjectConstants.ACCOUNT_ROOT);
        orgUserRelEntity.setUpdateTime(currentTime);
        orgUserRelEntity.setStatus(ProjectConstants.STATUS_VALID);
        return orgUserRelEntity;
    }

    @Override
    public void checkUserIsNotOrgLeader(int userId) {
        List<CcOrgEntity> orgList = orgRepository.findByLeaderAndStatus(userId,ProjectConstants.STATUS_VALID);
        boolean result = orgList ==  null || orgList.size() == 0;
        if(!result){
            String []orgNames = new String[orgList.size()];
            for( int i = 0 ;i < orgList.size() ; i++){
                orgNames[i] = orgList.get(i).getName();
            }
            throw new ErrorCodeException(ResultCodeEnum.ORG_COMMON_ERROR,"用户还是:"+ Arrays.toString(orgNames)+"组织的领导,无法注销用户");
        }
    }
    /**
     * 校验用户是否为管理员或者本组织领导
     * @author bawy
     * @date 2018/8/21 15:11
     * @param orgId 组织标识
     * @param operator 操作员
     */
    private void checkIsLeader(int orgId, int operator){
        if (!userSV.isAdministrator(operator)&&!isLeader(orgId, operator)){
            throw new ErrorCodeException(ResultCodeEnum.ORG_NO_PERMISSION_ERROR, "您非该组织的领导者");
        }
    }

    /**
     * 校验用户是否为管理员或者上级组织领导
     * @author bawy
     * @date 2018/8/21 15:17
     * @param orgId 组织标识
     * @param operator 操作员
     */
    private void checkIsParentLeader(int orgId, int operator){
        if (!userSV.isAdministrator(operator)&&!isParentLeader(orgId, operator)){
            throw new ErrorCodeException(ResultCodeEnum.ORG_NO_PERMISSION_ERROR, "您非该组织的上级组织的领导者");
        }
    }

    /**
     * 判断是否为上级组织的领导者
     * @author bawy
     * @date 2018/8/21 14:48
     * @param orgId 组织标识
     * @param operator 操作员
     */
    private boolean isParentLeader(int orgId, int operator){
        CcOrgEntity orgEntity = orgRepository.findParentByOrgIdAndLeaderAndStatus(orgId, operator, ProjectConstants.STATUS_VALID);
        return orgEntity!=null;

    }

    /**
     * 判断是否为本组织的领导者
     * @author bawy
     * @date 2018/8/21 14:51
     * @param orgId 组织标识
     * @param operator 操作员
     */
    private boolean isLeader(int orgId, int operator){
        CcOrgEntity orgEntity = orgRepository.findByIdAndLeaderAndStatus(orgId, operator, ProjectConstants.STATUS_VALID);
        return orgEntity!=null;
    }

    /**
     * 设置组织的领导
     * @author bawy
     * @date 2018/8/21 15:45
     * @param orgEntity 领导者选择范围组织
     * @param leader 领导
     */
    private CcOrgUserRelEntity setOrgLeader(CcOrgEntity orgEntity, Integer leader){
        if (leader!=null&&leader>0) {
            CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByOrgIdAndUserIdAndStatus(orgEntity.getId(), leader, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(orgUserRelEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "只能从组织“"+orgEntity.getName()+"”中选取人员设置为领导者");
            //更新角色
            orgUserRelEntity.setRoleId(roleSV.getLevelOneRoleIdByType(ProjectConstants.ROLE_TYPE_ORG));
            return orgUserRelEntity;
        }else {
            return null;
        }
    }

    /**
     * 修改组织领导者
     * @author bawy
     * @date 2018/8/21 16:33
     * @param orgEntity 组织
     * @param oldLeader 旧领导者
     * @param newLeader 新领导者
     * @param modifier 修改人
     */
    private void changeOrgLeader(CcOrgEntity orgEntity, Integer oldLeader, Integer newLeader, int modifier){
        if (newLeader!=null&&!newLeader.equals(oldLeader)) {
            CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByOrgIdAndUserIdAndStatus(orgEntity.getId(), newLeader, ProjectConstants.STATUS_VALID);
            Assert4CC.notNull(orgUserRelEntity, ResultCodeEnum.ILLEGAL_OPERATION_ERROR, "只能从组织“"+orgEntity.getName()+"”中选取人员设置为领导者");
            //更新角色
            orgUserRelEntity.setRoleId(roleSV.getLevelOneRoleIdByType(ProjectConstants.ROLE_TYPE_ORG));
            orgUserRelEntity.setModifier(modifier);
            orgUserRelEntity.setUpdateTime(TimeUtil.currentTime());
            orgUserRelRepository.save(orgUserRelEntity);
        }
    }

    @Override
    public CcOrgEntity getOrgById(int orgId) {
        return orgRepository.findByIdAndStatus(orgId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcOrgUserRelEntity getOrgByUserId(int userId) {
        return orgUserRelRepository.findByUserIdAndStatus(userId, ProjectConstants.STATUS_VALID);
    }

    @Override
    public boolean checkUserInOrg(int orgId, int roleId, int userId) {
        CcOrgUserRelEntity orgUserRelEntity = orgUserRelRepository.findByOrgIdAndUserIdAndRoleIdAndStatus(orgId, userId, roleId, ProjectConstants.STATUS_VALID);
        return  orgUserRelEntity!=null;
    }

}
