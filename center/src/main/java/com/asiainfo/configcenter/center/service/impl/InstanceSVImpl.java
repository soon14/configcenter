package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.InstanceMapper;
import com.asiainfo.configcenter.center.dao.repository.InstanceRepository;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import com.asiainfo.configcenter.center.entity.CcInstanceEntity;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.po.InstanceConfigPojo;
import com.asiainfo.configcenter.center.po.InstanceInfoPojo;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.JSONUtil;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInsByConfigReqVO;
import com.asiainfo.configcenter.center.vo.instance.QueryInstanceReqVO;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用实例的业务层
 * Created by oulc on 2018/7/26.
 */
@Service
public class InstanceSVImpl implements IInstanceSV {
    private static Logger logger = Logger.getLogger(InstanceSVImpl.class);

    @Resource
    private InstanceRepository instanceRepository;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private IAppSV iAppSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private IMailSV iMailSV;

    @Resource
    private IUserSV iUserSV;

    @Resource
    private IConfigSV iConfigSV;

    @Resource
    private InstanceMapper instanceMapper;

    @Resource
    private IGitSV iGitSV;

    @Resource
    private IConfigItemSV iConfigItemSV;

    @Override
    public PageResultContainer<CcInstanceEntity> findInstance(PageRequestContainer<QueryInstanceReqVO> pageRequestContainer) {
        ValidateUtil.checkPageParam(pageRequestContainer);

        QueryInstanceReqVO queryInstanceReqVO = pageRequestContainer.getData();
        //基本校验
        Assert4CC.isTrue(queryInstanceReqVO.getEnvId() != 0,"环境主键不能为空");

        //如果数据异常(判断zk数据数量和数据库数据数量是否一致) ，则修复数据
        fixData(queryInstanceReqVO.getEnvId());

        //查询表里的实例的信息
        Page<CcInstanceEntity> page = findInstance(queryInstanceReqVO.getEnvId(),
                queryInstanceReqVO.getInsName(),
                queryInstanceReqVO.getIsAlive(),
                queryInstanceReqVO.getInsIp(),
                pageRequestContainer.getCurrentPage(),
                pageRequestContainer.getPageSize());

        return new PageResultContainer<>(page);
    }

    @Override
    public Page<CcInstanceEntity> findInstance(int envId , String insName, byte isAlive, String insIp, int currentPage, int size) {
        Pageable pageable = new PageRequest(currentPage, size, Sort.Direction.DESC, "isAlive","insName");
        return instanceRepository.findAll(new Specification<CcInstanceEntity>() {
            @Override
            public Predicate toPredicate(Root<CcInstanceEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("envId").as(int.class),envId));
                predicates.add(criteriaBuilder.equal(root.get("status").as(Byte.class),ProjectConstants.STATUS_VALID));
                if(StringUtils.isNotBlank(insName)){
                    predicates.add(criteriaBuilder.like(root.get("insName").as(String.class),"%"+insName+"%"));
                }
                if( isAlive != -1){
                    predicates.add(criteriaBuilder.equal(root.get("isAlive").as(short.class),isAlive));
                }
                if(StringUtils.isNotBlank(insIp)){
                    predicates.add(criteriaBuilder.like(root.get("insIp").as(String.class),"%"+insIp+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        },pageable);
    }

    @Override
    public List<String> getZkAliveInstance(String appName,String envName){
        try {
            return ccServerZKManager.getInstanceNodeOper().getInstances(appName,envName);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    @Transactional
    @Override
    public void fixData(int envId) {
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        try {
            //如果数据不一致则修复数据
            long dataBaseCount = instanceRepository.findCountByEnvIdAndIsAlive(envId, ProjectConstants.STATUS_VALID);
            List<String> zkChild = getZkAliveInstance(appInfoVO.getAppName(),appInfoVO.getEnvName());
            if(dataBaseCount != zkChild.size()){
                fixDataLoopDataBaseInfo(zkChild,envId);
                fixDataLoopZkInfo(zkChild,envId,appInfoVO.getAppName(),appInfoVO.getEnvName());
            }
        }catch (Exception e){
            //修复数据失败发送邮件给管理员
            logger.info(ErrorInfo.errorInfo(e));
            sendFixFailMail(appInfoVO.getAppId(),appInfoVO.getAppName(),appInfoVO.getEnvName());
            throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR,"实例数据异常，自动修复失败，已经发送邮件给管理人员");
        }

    }

    private void sendFixFailMail(int appId,String appName,String envName){
        //查询出应用的经理和维护人员邮箱
        List<CcUserEntity> userList = iUserSV.findAppManager(appId);
        Assert4CC.isTrue(userList != null && userList.size() >0,ResultCodeEnum.APP_COMMON_ERROR,"项目没有找到管理人员，项目名称:"+appName+",项目主键:"+appId);
        String [] address = new String[userList.size()];
        for(int i = 0 ;i <userList.size() ;i++){
            address[i] = userList.get(i).getEmail();
        }
        iMailSV.sendMail("实例修复数据失败","应用名称:"+appName+",\n环境名称:"+envName,address);
    }

    @Transactional
    @Override
    public void fixDataLoopDataBaseInfo(List<String> zkChild,int envId) {
        List<CcInstanceEntity> dataBaseResult = instanceRepository.findByEnvIdAndIsAliveAndStatus(envId,ProjectConstants.STATUS_VALID,ProjectConstants.STATUS_VALID);
        if(dataBaseResult != null && dataBaseResult.size() >0){
            for(CcInstanceEntity ccInstanceEntity:dataBaseResult){
                String insName = ccInstanceEntity.getInsName();
                if(!zkChild.contains(insName)){//如果zk中没有该实例数据
                    ccInstanceEntity.setIsAlive(ProjectConstants.STATUS_INVALID);
                    instanceRepository.save(ccInstanceEntity);
                }else{
                    zkChild.remove(insName);
                }
            }
        }
    }

    @Transactional
    @Override
    public void fixDataLoopZkInfo(List<String> zkChild,int envId,String appName,String envName)throws Exception{
        if(zkChild.size() > 0 ){
            for(String zkInsName:zkChild){
                //查询表里数据
                List<CcInstanceEntity> list = instanceRepository.findByEnvIdAndInsNameAndStatus(envId,zkInsName,ProjectConstants.STATUS_VALID);
                if(list != null && list.size() >0){
                    if(list.size() == 1){//如果存在断开连接的数据,把连接状态拉起
                        CcInstanceEntity ccInstanceEntity = list.get(0);
                        if(ccInstanceEntity.getIsAlive() == ProjectConstants.STATUS_INVALID){
                            ccInstanceEntity.setIsAlive(ProjectConstants.STATUS_VALID);
                            instanceRepository.save(ccInstanceEntity);
                        }
                    }else{
                        throw new ErrorCodeException(ResultCodeEnum.APP_COMMON_ERROR,"相同环境下的实例不能有两个相同的实例，环境主键:"+envId+",实例名称:"+zkInsName);
                    }
                }else{
                    //新增数据
                    createInstance(appName,envName,zkInsName,envId);
                }
            }
        }
    }

    @Transactional
    @Override
    public int createInstance(String appName ,String envName,String zkInsName,int envId) {
        String zkNodeData;
        try {
            zkNodeData = ccServerZKManager.getInstanceNodeOper().readInstanceNodeData(appName,envName,zkInsName);
        }catch (Exception e){
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
        JsonObject jsonObject = JSONUtil.jsonStr2JsonObject(zkNodeData);
        String zkIp = jsonObject.get(ProjectConstants.APP_INSTANCE_INFO_IP).getAsString();

        return createInstance(appName,envName,zkInsName,envId,zkIp);
    }

    @Transactional
    @Override
    public int createInstance(String appName, String envName, String zkInsName, int envId,String zkIp) {
        CcInstanceEntity ccInstanceEntity = new CcInstanceEntity();
        ccInstanceEntity.setEnvId(envId);
        ccInstanceEntity.setInsName(zkInsName);
        ccInstanceEntity.setInsIp(zkIp);
        ccInstanceEntity.setIsAlive(ProjectConstants.STATUS_VALID);
        ccInstanceEntity.setCreateTime(TimeUtil.currentTime());
        ccInstanceEntity.setUpdateTime(TimeUtil.currentTime());
        ccInstanceEntity.setStatus(ProjectConstants.STATUS_VALID);
        ccInstanceEntity.setLastConnectTime(TimeUtil.currentTime());
        instanceRepository.save(ccInstanceEntity);
        return ccInstanceEntity.getId();
    }

    @Transactional
    @Override
    public void deleteInstance(int insId) {
        AppInfoVO appInfoVO = getAppInfoByInsId(insId);
        boolean isAlive;

        //判断实例已经失去连接
        try {
            isAlive = ccServerZKManager.getInstanceNodeOper().instanceExist(appInfoVO.getAppName(),appInfoVO.getEnvName(),appInfoVO.getInsName());
        }catch ( KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
        Assert4CC.isTrue(!isAlive,ResultCodeEnum.APP_COMMON_ERROR,"实例存活，无法删除实例");
        //删除表数据
        deleteInstanceDataBaseInfo(insId);
    }

    @Override
    public AppInfoVO getAppInfoByInsId(int insId) {
        AppInfoVO appInfoVO = new AppInfoVO();

        //获取实例信息
        CcInstanceEntity ccInstanceEntity = getInstanceByIdCheck(insId);
        appInfoVO.setInsId(insId);
        appInfoVO.setInsName(ccInstanceEntity.getInsName());

        //获取环境信息
        int envId = ccInstanceEntity.getEnvId();
        CcAppEnvEntity ccAppEnvEntity = iAppEnvSV.getEnvByEnvIdCheck(envId);
        appInfoVO.setEnvId(envId);
        appInfoVO.setEnvName(ccAppEnvEntity.getEnvName());

        //获取应用信息
        int appId = ccAppEnvEntity.getAppId();
        CcAppEntity ccAppEntity = iAppSV.getAppByIdCheck(appId);
        appInfoVO.setAppId(appId);
        appInfoVO.setAppName(ccAppEntity.getAppName());
        return appInfoVO;
    }

    @Override
    public void deleteInstanceDataBaseInfo(int insId) {
        CcInstanceEntity ccInstanceEntity = getInstanceByIdCheck(insId);
        ccInstanceEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccInstanceEntity.setUpdateTime(TimeUtil.currentTime());
        ccInstanceEntity.setIsAlive(ProjectConstants.STATUS_INVALID);
        instanceRepository.save(ccInstanceEntity);
    }

    @Override
    public InstanceConfigPojo[] getInstanceConfigInfo(int insId) {
        AppInfoVO appInfoVO = getAppInfoByInsId(insId);
        String nodeData;
        List<InstanceConfigPojo> instanceInfoPojoList = new ArrayList<>();
        //获取实例
        try {
            nodeData = ccServerZKManager.getInstanceNodeOper().readInstanceNodeData(appInfoVO.getAppName(),appInfoVO.getEnvName(),appInfoVO.getInsName());
            InstanceConfigPojo[] instanceConfigPojos = JSONUtil.jsonStrToBean(nodeData,InstanceInfoPojo.class).getInstanceConfigPojos();
            for(int i=0;i<instanceConfigPojos.length;i++) {
                String configVersion = instanceConfigPojos[i].getConfigVersion();
                String configName = instanceConfigPojos[i].getConfigName();
                int configType = instanceConfigPojos[i].getConfigType();
                long commitTime = 0;
                if(configType == 1) {
                    commitTime = iGitSV.getFileCommitTimeByCommitName(appInfoVO,configVersion,configName);
                } else {
                    commitTime = iConfigItemSV.getConfigItemHisVersionByIdCheck(appInfoVO.getEnvId(),Integer.parseInt(configVersion)).getCreateTime().getTime();
                }
                instanceConfigPojos[i].setCommitTime(commitTime);
                instanceInfoPojoList.add(instanceConfigPojos[i]);
            }
        }catch ( KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
        InstanceConfigPojo[] configPojos = new InstanceConfigPojo[instanceInfoPojoList.size()];
        return instanceInfoPojoList.toArray(configPojos);
    }


    @Override
    public void setInstanceAliveStatus(int insId,byte isAlive) {
        CcInstanceEntity ccInstanceEntity = getInstanceByIdCheck(insId);
        ccInstanceEntity.setIsAlive(isAlive);
        if(isAlive == 1){
            ccInstanceEntity.setLastConnectTime(TimeUtil.currentTime());
        }
        ccInstanceEntity.setUpdateTime(TimeUtil.currentTime());
        instanceRepository.save(ccInstanceEntity);
    }

    @Override
    public CcInstanceEntity getInstanceById(int insId){
        return  instanceRepository.findByIdAndStatus(insId,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcInstanceEntity getInstanceByIdCheck(int insId){
        CcInstanceEntity ccInstanceEntity = getInstanceById(insId);
        Assert4CC.notNull(ccInstanceEntity,ResultCodeEnum.APP_COMMON_ERROR,"实例不存在，实例主键:"+insId);
        return ccInstanceEntity;
    }

    @Override
    public List<CcInstanceEntity> getInstancesByNotIn(int envId, int[] ids,String insName,String insIp) {
        if (ids.length == 0){
            return instanceRepository.findAllByEnvIdAndIdNotInAndStatusAndIsAliveAndInsNameLikeAndInsIpLike(envId,new int[]{0},ProjectConstants.STATUS_VALID,ProjectConstants.STATUS_VALID,"%"+insName+"%","%"+insIp+"%");
        }else {
            return instanceRepository.findAllByEnvIdAndIdNotInAndStatusAndIsAliveAndInsNameLikeAndInsIpLike(envId,ids,ProjectConstants.STATUS_VALID,ProjectConstants.STATUS_VALID,"%"+insName+"%","%"+insIp+"%");
        }
    }

    @Override
    public PageResultContainer<CcInstanceEntity> getInstancesByConfigIdAndVersion(int envId, byte configType, int configId, String configVersion,String insName,String insIp,int start,int size) {
        PageResultContainer<CcInstanceEntity> pageResultContainer = new PageResultContainer();
        List<CcInstanceEntity> resultList = new ArrayList<>();
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(envId);
        String configName = iConfigSV.getConfigNameByConfigTypeAndId(envId, configType, configId);

        List<String> instanceNames = getZkAliveInstance(appInfoVO.getAppName(),appInfoVO.getEnvName());
        if( instanceNames != null && instanceNames.size() > 0){
            int totalCount = 0;
            for(int j = 0 ;j < instanceNames.size() ;j ++){
                String instanceName = instanceNames.get(j);
                try {
                    String instanceData = ccServerZKManager.getInstanceNodeOper().readInstanceNodeData(appInfoVO.getAppName(),appInfoVO.getEnvName(),instanceName);
                    InstanceInfoPojo instanceInfoPojo = JSONUtil.jsonStrToBean(instanceData,InstanceInfoPojo.class);
                    int instanceId = instanceInfoPojo.getInstanceId();
                    InstanceConfigPojo []instanceConfigPojos = instanceInfoPojo.getInstanceConfigPojos();

                    if(instanceConfigPojos != null && instanceConfigPojos.length > 0){

                        for( int i = 0 ;i < instanceConfigPojos.length ;i ++){
                            InstanceConfigPojo instanceConfigPojo = instanceConfigPojos[i];
                            insName = insName == null ? "":insName;
                            insIp = insIp== null ? "" : insIp;
                            if(configType == instanceConfigPojo.getConfigType() &&
                                    configName.equals(instanceConfigPojo.getConfigName()) &&
                                    configVersion.equals(instanceConfigPojo.getConfigVersion())){
                                CcInstanceEntity ccInstanceEntity = getInstanceByIdAndNameAndIp(instanceId,insName,insIp);
                                if(ccInstanceEntity != null ){//如果查询到了数据 则count+1
                                    totalCount++;
                                    if(resultList.size() < size && j >= start){
                                        resultList.add(getInstanceByIdCheck(instanceId));
                                        break;
                                    }
                                }
                            }
                        }
                        pageResultContainer.setCount(totalCount);
                        pageResultContainer.setEntities(resultList);
                    }
                }catch (KeeperException | InterruptedException e){
                    logger.info(ErrorInfo.errorInfo(e));
                    throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
                }
            }

        }
        return pageResultContainer;
    }

    @Override
    public PageResultContainer<CcInstanceEntity> getInstanceByConfig(PageRequestContainer<QueryInsByConfigReqVO> pageRequestContainer ){
        ValidateUtil.checkPageParam(pageRequestContainer);
        QueryInsByConfigReqVO queryInsByConfigReqVO = pageRequestContainer.getData();
        getInstanceByConfigCheckParam(queryInsByConfigReqVO);
        return getInstancesByConfigIdAndVersion(queryInsByConfigReqVO.getEnvId(),
                queryInsByConfigReqVO.getConfigType(),
                queryInsByConfigReqVO.getConfigId(),
                queryInsByConfigReqVO.getConfigVersion(),
                queryInsByConfigReqVO.getInsName(),
                queryInsByConfigReqVO.getInsIp(),
                pageRequestContainer.getCurrentPage()*pageRequestContainer.getPageSize(),
                pageRequestContainer.getPageSize());
    }

    @Override
    public CcInstanceEntity getInstanceByIdAndNameAndIp(int id, String insName, String insIp) {
        return instanceRepository.findByIdAndInsNameLikeAndInsIpLikeAndStatusAndIsAlive(id,"%"+insName+"%","%"+insIp+"%",ProjectConstants.STATUS_VALID,ProjectConstants.STATUS_VALID);
    }

    private void getInstanceByConfigCheckParam(QueryInsByConfigReqVO queryInsByConfigReqVO){
        Assert4CC.isTrue(queryInsByConfigReqVO.getEnvId() !=0 ,"环境主键不能为空");
        Assert4CC.isTrue(queryInsByConfigReqVO.getConfigType() != 0 , "配置类型不能为空");
        Assert4CC.isTrue(queryInsByConfigReqVO.getConfigId() != 0,"配置主键不能为空");
        Assert4CC.hasLength(queryInsByConfigReqVO.getConfigVersion() , "配置版本不能为空");
    }

    @Override
    public long getInstanceCountByUserId(int userId) {
        if(iUserSV.isAdministrator(userId)){
            return instanceMapper.getInstanceCount();
        }else{
            return instanceMapper.getInstanceCountByUserId(userId);
        }
    }
}
