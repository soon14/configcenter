package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.*;
import com.asiainfo.configcenter.center.dao.mapper.AppEnvMapper;
import com.asiainfo.configcenter.center.dao.repository.AppEnvRepository;
import com.asiainfo.configcenter.center.entity.CcAppEntity;
import com.asiainfo.configcenter.center.entity.CcAppEnvEntity;
import com.asiainfo.configcenter.center.entity.CcConfigFileEntity;
import com.asiainfo.configcenter.center.entity.CcConfigItemEntity;
import com.asiainfo.configcenter.center.entity.complex.CXCcAppEnvEntity;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.util.TimeUtil;
import com.asiainfo.configcenter.center.util.ValidateUtil;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.env.AppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.DelAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.QueryAppEnvReqVO;
import com.asiainfo.configcenter.center.vo.env.UpdateAppEnvReqVO;
import com.asiainfo.configcenter.zookeeper.cczk.CCServerZKManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 应用环境业务层
 * Created by oulc on 2018/7/24.
 */
@Service
public class AppEnvSVImpl implements IAppEnvSV {

    private static Logger logger = Logger.getLogger(AppEnvSVImpl.class);

    @Resource
    private AppEnvRepository appEnvRepository;

    @Resource
    private IAppSV iAppSV;

    @Resource
    private CCServerZKManager ccServerZKManager;

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private IConfigItemSV iConfigItemSV;

    @Resource
    private AppEnvMapper appEnvMapper;

    @Resource
    private IGitSV iGitSV;

    @Resource
    private INetDiskSV iNetDiskSV;

    @Resource
    private IUserSV iUserSV;

    @Transactional
    @Override
    public int createAppEnv(AppEnvReqVO createAppEnvReqVO, int userId) {
        //校验数据
        checkCreateAppEnvVO(createAppEnvReqVO);

        //获取应用的名称
        String appName = iAppSV.getAppNameByAppId(createAppEnvReqVO.getAppId());

        //保存环境数据至表中
        CcAppEnvEntity ccAppEnvEntity = saveAppEnv(createAppEnvReqVO.getAppId(),createAppEnvReqVO.getEnvName(),createAppEnvReqVO.getDesc(),userId);

        //保存节点数据
        createEnvNode(appName,createAppEnvReqVO.getEnvName(),ccAppEnvEntity.getId());

        //创建git目录并初始化
        iGitSV.createEnvGitDirAndInit(getAppInfoByEnvId(ccAppEnvEntity.getId()));

        return ccAppEnvEntity.getId();
    }

    /**
     * 创建环境是校验数据
     * @param createAppEnvReqVO 创建环境的入参
     * @author oulc
     * @date 2018/7/24 15:31
     */
    private void checkCreateAppEnvVO(AppEnvReqVO createAppEnvReqVO){
        //基本校验
        Assert4CC.isTrue(createAppEnvReqVO.getAppId() != 0 ,"项目主键不能为空");
        Assert4CC.hasLength(createAppEnvReqVO.getEnvName() ,"环境名称不能为空");
        Assert4CC.isTrue(ValidateUtil.checkAppEnvName(createAppEnvReqVO.getEnvName()),"项目名称不合法，环境名称长度为2-64个字符，只能由英文字母和数字加下划线组成，首字符只能为字母");

        //校验环境信息 校验相同App下不能存在相同名称的环境
        CcAppEntity ccAppEntity = iAppSV.getAppByIdCheck(createAppEnvReqVO.getAppId());
        List<CcAppEnvEntity> list = getEnvByAppIdAndEnvName(ccAppEntity.getId(),createAppEnvReqVO.getEnvName());
        Assert4CC.isTrue( list == null || list.size() == 0 ,ResultCodeEnum.APP_COMMON_ERROR,"app已经存在相同名称的环境");

    }

    @Override
    public CcAppEnvEntity getEnvByEnvId(int envId) {
        return appEnvRepository.findByIdAndStatus(envId  ,ProjectConstants.STATUS_VALID);
    }

    @Override
    public CcAppEnvEntity getEnvByEnvIdCheck(int envId){
        CcAppEnvEntity ccAppEnvEntity = getEnvByEnvId(envId);
        Assert4CC.notNull(ccAppEnvEntity,ResultCodeEnum.APP_COMMON_ERROR,"环境不存在，环境主键:"+envId);
        return ccAppEnvEntity;
    }

    @Override
    public List<CcAppEnvEntity> getEnvByAppIdAndEnvName(int appId, String envName) {
        return appEnvRepository.findByAppIdAndEnvNameAndStatus(appId,envName,ProjectConstants.STATUS_VALID);
    }

    @Transactional
    @Override
    public CcAppEnvEntity saveAppEnv(int appId, String envName, String desc,int userId) {
        CcAppEnvEntity ccAppEnvEntity = new CcAppEnvEntity();
        ccAppEnvEntity.setAppId(appId);
        ccAppEnvEntity.setEnvName(envName);
        ccAppEnvEntity.setDescription(desc);
        ccAppEnvEntity.setCreator(userId);
        ccAppEnvEntity.setCreateTime(TimeUtil.currentTime());
        ccAppEnvEntity.setUpdateTime(TimeUtil.currentTime());
        ccAppEnvEntity.setModifier(userId);
        ccAppEnvEntity.setStatus(ProjectConstants.STATUS_VALID);
        appEnvRepository.save(ccAppEnvEntity);
        return ccAppEnvEntity;
    }

    /**
     * 创建环境zookeeper节点
     * @param appName 应用名称
     * @param envName 环境名称
     * @param envId 环境主键
     * @author oulc
     * @date 2018/7/31 9:41
     */
    private void createEnvNode(String appName, String envName,int envId) {
        try {
            ccServerZKManager.getEnvNodeOper().createEnvNode(appName,envName,""+envId);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }

    @Override
    public void updateAppEnv(UpdateAppEnvReqVO updateAppEnvReqVO) {
        //校验
        checkUpdateAppEnvReqVO(updateAppEnvReqVO);

        //获取项目名称
        String appName = iAppSV.getAppNameByEnvId(updateAppEnvReqVO.getEnvId());

        String newEnvName = updateAppEnvReqVO.getEnvName();
        int envId = updateAppEnvReqVO.getEnvId();
        String desc = updateAppEnvReqVO.getDesc();

        //更新环境表数据 并获得旧环境名称
        String oldEnvName = updateAppEnvDataBaseInfo(envId,newEnvName,desc);

        //更新节点信息
        updateAppEnvNode(appName,oldEnvName,newEnvName,envId);
    }
    @Transactional
    @Override
    public String updateAppEnvDataBaseInfo(int envId,String envName,String desc){
        CcAppEnvEntity ccAppEnvEntity = getEnvByEnvIdCheck(envId);
        String oldEnvName = ccAppEnvEntity.getEnvName();
        ccAppEnvEntity.setEnvName(envName);
        ccAppEnvEntity.setDescription(desc);
        ccAppEnvEntity.setUpdateTime(TimeUtil.currentTime());
        appEnvRepository.save(ccAppEnvEntity);
        return oldEnvName;
    }

    /**
     * 更新环境校验
     * @param updateAppEnvReqVO 环境数据
     * @author oulc
     * @date 2018/7/25 16:22
     */
    private void checkUpdateAppEnvReqVO(UpdateAppEnvReqVO updateAppEnvReqVO){
        //基本校验
        Assert4CC.isTrue(updateAppEnvReqVO.getEnvId() != 0,"环境主键不能为空");
        Assert4CC.hasLength(updateAppEnvReqVO.getEnvName() ,"环境名称不能为空");
        Assert4CC.isTrue(ValidateUtil.checkAppEnvName(updateAppEnvReqVO.getEnvName()),"项目名称不合法，环境名称长度为2-64个字符，只能由英文字母和数字加下划线组成，首字符只能为字母");
        Assert4CC.isTrue(updateAppEnvReqVO.getEnvName().length() < 1001,"描述长度不能超过1000");

        //校验环境下不能有配置
        checkAppEnvHasNoneConfig(updateAppEnvReqVO.getEnvId());

        //校验环境 环境必须存在 名称不能重复等
        CcAppEnvEntity ccAppEnvEntity = appEnvRepository.findByIdAndStatus(updateAppEnvReqVO.getEnvId(),ProjectConstants.STATUS_VALID);
        Assert4CC.notNull(ccAppEnvEntity,ResultCodeEnum.APP_ENV_NOT_EXIST_ERROR);

        if(! updateAppEnvReqVO.getEnvName().equals(ccAppEnvEntity.getEnvName())){//如果名称发生改变
            List<CcAppEnvEntity> list = getEnvByAppIdAndEnvName(ccAppEnvEntity.getAppId(),updateAppEnvReqVO.getEnvName());
            Assert4CC.isTrue( list == null || list.size() == 0,ResultCodeEnum.APP_COMMON_ERROR,"环境名称不能重复");
        }
    }


    private void updateAppEnvNode(String appName,String oldEnvName,String newEnvName,int envId){
        //如果变更了环境名称 变更节点信息
        if(! oldEnvName.equals(newEnvName)){
            try {
                if(ccServerZKManager.getEnvNodeOper().envNodeExist(appName,oldEnvName)){
                    ccServerZKManager.getEnvNodeOper().deleteEnvNode(appName,oldEnvName);
                }
                ccServerZKManager.getEnvNodeOper().createEnvNode(appName,newEnvName,""+envId);
            }catch (KeeperException|InterruptedException e){
                logger.info(ErrorInfo.errorInfo(e));
                throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
            }
        }
    }

    @Override
    public List<CXCcAppEnvEntity> getEnvByAppId(int appId) {
        return appEnvMapper.findCXAppEnvByAppId(appId);
    }

    @Override
    public List<CXCcAppEnvEntity> getEnvByAppId(QueryAppEnvReqVO queryAppEnvReqVO) {
        Assert4CC.isTrue(queryAppEnvReqVO.getAppId() != 0,"app主键不能为空");
        return getEnvByAppId(queryAppEnvReqVO.getAppId());
    }

    @Transactional
    @Override
    public void deleteEnv(int envId) {
        CcAppEnvEntity ccAppEnvEntity = getEnvByEnvIdCheck(envId);
        ccAppEnvEntity.setStatus(ProjectConstants.STATUS_INVALID);
        ccAppEnvEntity.setUpdateTime(TimeUtil.currentTime());
        appEnvRepository.save(ccAppEnvEntity);
    }



    @Transactional
    @Override
    public void deleteEnv(DelAppEnvReqVO delAppEnvReqVO) {
        Assert4CC.isTrue(delAppEnvReqVO.getEnvId()!= 0 ,"环境主键不能为空");
        //校验环境下不能存在配置文件或者配置项

        checkAppEnvHasNoneConfig(delAppEnvReqVO.getEnvId());

        AppInfoVO appInfoVO = getAppInfoByEnvId(delAppEnvReqVO.getEnvId());
        int envId = delAppEnvReqVO.getEnvId();

        //删除表数据
        deleteEnv(envId);

        //删除节点数据
        deleteEnvNode(appInfoVO.getAppName(),appInfoVO.getEnvName());

        //删除网盘目录
        String netEnvDirPath = iNetDiskSV.getNetDiskFullFilePath(appInfoVO.getAppName(),appInfoVO.getEnvName(),"");
        String netHisDir = iNetDiskSV.getNetDiskFullFilePath(appInfoVO.getAppName(),"",""+envId);
        File netFile = new File(netEnvDirPath);
        if(netFile.exists()){
            CcFileUtils.moveDirectory(new File(netEnvDirPath),new File(netHisDir));
        }
        //删除git目录
        String gitEnvDirPath = iGitSV.getGitFullPath(appInfoVO,"");
        appInfoVO.setEnvName("");
        String gitHisDir = iGitSV.getGitFullPath(appInfoVO,""+envId);
        CcFileUtils.moveDirectory(new File(gitEnvDirPath),new File(gitHisDir));
    }

    /**
     * 删除环境节点
     * @param appName 项目名称
     * @param envName 环境名称
     */
    private void deleteEnvNode(String appName,String envName){
        try {
            ccServerZKManager.getEnvNodeOper().deleteEnvNode(appName,envName);
        }catch (KeeperException | InterruptedException e){
            logger.info(ErrorInfo.errorInfo(e));
            throw new ErrorCodeException(ResultCodeEnum.ZK_ERROR);
        }
    }



    @Override
    public void checkAppEnvHasNoneConfig(int envId) {
        //查询没有配置文件
        List<CcConfigFileEntity> fileList = iConfigFileSV.getConfigFilesByEnvId(envId);
        Assert4CC.isTrue(fileList == null || fileList.size() == 0,ResultCodeEnum.APP_COMMON_ERROR,"环境下存在配置文件，无法删除");

        //查询没有配置项
        List<CcConfigItemEntity> itemList = iConfigItemSV.getConfigItemsByEnvId(envId);
        Assert4CC.isTrue(itemList == null || itemList.size() == 0,ResultCodeEnum.APP_COMMON_ERROR,"环境下存在配置项，无法删除");
    }

    @Override
    public AppInfoVO getAppInfoByEnvId(int envId) {
        AppInfoVO appInfoVO = new AppInfoVO();

        //获取到环境信息
        CcAppEnvEntity ccAppEnvEntity = getEnvByEnvIdCheck(envId);
        appInfoVO.setEnvId(envId);
        appInfoVO.setEnvName(ccAppEnvEntity.getEnvName());

        //获取到应用信息
        CcAppEntity ccAppEntity = iAppSV.getAppByIdCheck(ccAppEnvEntity.getAppId());
        appInfoVO.setAppId(ccAppEnvEntity.getAppId());
        appInfoVO.setAppName(ccAppEntity.getAppName());

        return appInfoVO;
    }

    @Override
    public long getEnvCountByUserId(int userId) {
        if(iUserSV.isAdministrator(userId)){
            return appEnvMapper.getEnvCount();
        }else{
            return appEnvMapper.getEnvCountByUserId(userId);
        }
    }
}
