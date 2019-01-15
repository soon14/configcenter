package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ErrorCodeException;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.entity.*;
import com.asiainfo.configcenter.center.service.interfaces.*;
import com.asiainfo.configcenter.center.util.CcFileUtils;
import com.asiainfo.configcenter.center.vo.app.AppInfoVO;
import com.asiainfo.configcenter.center.vo.client.*;
import com.asiainfo.configcenter.center.vo.strategy.ConfigUpdateStrategyVO;
import com.asiainfo.configcenter.zookeeper.cczk.vo.ZKConfigVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientSVImpl implements IClientSV{

    @Resource
    private IAppSV iAppSV;

    @Resource
    private IAppEnvSV iAppEnvSV;

    @Resource
    private IGitSV iGitSV;

    @Resource
    private IConfigFileSV iConfigFileSV;

    @Resource
    private IConfigUpdateStrategySV iConfigUpdateStrategySV;

    @Resource
    private IConfigItemSV iConfigItemSV;

    @Override
    public List<ClientRespVO> getPushFileAndStrategy(ClientReqVO clientReqVO) {
        Assert4CC.hasLength(clientReqVO.getAppName(),"应用名称不能为空");
        Assert4CC.hasLength(clientReqVO.getEnvName(),"环境名称不能为空");
        List<ZKConfigVO> zkConfigs = clientReqVO.getZkConfigs();
        Assert4CC.isTrue(zkConfigs!=null&&zkConfigs.size()>0, "更新的配置文件数量不能为空");
        CcAppEntity ccAppEntity = iAppSV.getAppByName(clientReqVO.getAppName());
        Assert4CC.isTrue(ccAppEntity != null,"应用不存在");
        List<CcAppEnvEntity> ccAppEnvEntities = iAppEnvSV.getEnvByAppIdAndEnvName(ccAppEntity.getId(),clientReqVO.getEnvName());
        Assert4CC.isTrue(ccAppEnvEntities.size()>0,"环境不存在");

        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppId(ccAppEntity.getId());
        appInfoVO.setAppName(ccAppEntity.getAppName());
        appInfoVO.setEnvId(ccAppEnvEntities.get(0).getId());
        appInfoVO.setEnvName(ccAppEnvEntities.get(0).getEnvName());

        List<ClientRespVO> respList = new ArrayList<>();
        for(ZKConfigVO zkConfig : zkConfigs) {
            ClientRespVO clientRespVO = getFileInfo(appInfoVO,zkConfig.getName(), zkConfig.getVersion());
            respList.add(clientRespVO);
        }
        return respList;
    }

    private ClientRespVO getFileInfo(AppInfoVO appInfoVO,String fileName,String configFileVersion) {
        CcConfigFileEntity configFileEntity = iConfigFileSV.getConfigFileByEnvIdAndFileName(appInfoVO.getEnvId(),fileName);
        Assert4CC.notNull(configFileEntity,"没有找到对应的文件");
        Integer strategyId = configFileEntity.getStrategyId();
        Assert4CC.isTrue(strategyId!=null&&strategyId>0, "推送的配置文件没有配置刷新策略");
        ClientRespVO clientRespVO = new ClientRespVO();
        clientRespVO.setConfigId(configFileEntity.getId());
        clientRespVO.setName(fileName);
        clientRespVO.setVersion(configFileVersion);
        clientRespVO.setContent(iGitSV.getFileContentByCommitName(appInfoVO,configFileVersion,fileName));
        ConfigUpdateStrategyVO configUpdateStrategyVO = iConfigUpdateStrategySV.getConfigUpdateStrategyAllById(strategyId,appInfoVO.getAppId());
        clientRespVO.setConfigUpdateStrategyVO(configUpdateStrategyVO);
        return clientRespVO;
    }

    @Override
    public List<ClientRespVO> getPushConfigItem(ClientReqVO clientReqVO) {
        Assert4CC.hasLength(clientReqVO.getAppName(),"应用名称不能为空");
        Assert4CC.hasLength(clientReqVO.getEnvName(),"环境名称不能为空");
        List<ZKConfigVO> zkConfigs = clientReqVO.getZkConfigs();
        Assert4CC.isTrue(zkConfigs!=null&&zkConfigs.size()>0, "配置项版本号不能为空");
        CcAppEntity ccAppEntity = iAppSV.getAppByName(clientReqVO.getAppName());
        Assert4CC.isTrue(ccAppEntity != null, "该应用不存在或已删除！");
        List<CcAppEnvEntity> ccAppEnvEntities = iAppEnvSV.getEnvByAppIdAndEnvName(ccAppEntity.getId(), clientReqVO.getEnvName());
        Assert4CC.isTrue(ccAppEnvEntities.size()>0, "该环境不存在或已删除！");

        AppInfoVO appInfoVO = new AppInfoVO();
        appInfoVO.setAppId(ccAppEntity.getId());
        appInfoVO.setAppName(ccAppEntity.getAppName());
        appInfoVO.setEnvId(ccAppEnvEntities.get(0).getId());
        appInfoVO.setEnvName(ccAppEnvEntities.get(0).getEnvName());

        List<ClientRespVO> respList = new ArrayList<>();
        for (ZKConfigVO zkConfig : zkConfigs) {
            ClientRespVO clientRespVO = getConfigItem(appInfoVO, zkConfig.getName(), zkConfig.getVersion());
            respList.add(clientRespVO);
        }
        return respList;
    }

    private ClientRespVO getConfigItem(AppInfoVO appinfo, String itemKey, String version) {
        int itemHisId;
        try {
            itemHisId = Integer.valueOf(version);
        }catch (Exception e){
            throw new ErrorCodeException(ResultCodeEnum.PARAM_ERROR, "配置项“"+itemKey+"”的版本号“"+version+"”非法");
        }
        CcConfigItemHisEntity configItemHisEntity = iConfigItemSV.getConfigItemHisVersionByIdCheck(appinfo.getEnvId(), itemHisId);
        CcConfigItemEntity configItemEntity = iConfigItemSV.getConfigItemByIdCheck(configItemHisEntity.getItemId(), appinfo.getEnvId());
        Integer strategyId = configItemEntity.getStrategyId();
        Assert4CC.isTrue(strategyId!=null&&strategyId>0, ResultCodeEnum.DATA_ERROR, "配置项“"+itemKey+"”没有设置刷新策略");

        ClientRespVO clientRespVO = new ClientRespVO();
        clientRespVO.setConfigId(configItemEntity.getId());
        clientRespVO.setName(configItemEntity.getItemKey());
        clientRespVO.setContent(configItemEntity.getItemValue());
        clientRespVO.setVersion(version);
        ConfigUpdateStrategyVO configUpdateStrategyVO = iConfigUpdateStrategySV.getConfigUpdateStrategyAllById(strategyId, appinfo.getAppId());
        clientRespVO.setConfigUpdateStrategyVO(configUpdateStrategyVO);
        return clientRespVO;

    }

    @Override
    public ClientConfigFilesResp getAllFile(String appName, String envName) {
        Assert4CC.hasLength(appName, "应用名称为空!");
        Assert4CC.hasLength(envName, "环境名称为空!");
        ClientConfigFilesResp configFilesResq = new ClientConfigFilesResp();
        CcAppEntity ccAppEntity = iAppSV.getAppByName(appName);

        List<CcAppEnvEntity> ccAppEnvEntityList = iAppEnvSV.getEnvByAppIdAndEnvName(ccAppEntity.getId(),envName);
        AppInfoVO appInfoVO = iAppEnvSV.getAppInfoByEnvId(ccAppEnvEntityList.get(0).getId());
        Assert4CC.isTrue(appInfoVO != null, "应用信息获取失败！");
        File dirFile = new File(iGitSV.getGitFullPath(appInfoVO,"")); //获得绝对路径
        Assert4CC.isTrue(dirFile.exists(), "该应用环境目录不存在:" + dirFile.toString());
        File zipFile = CcFileUtils.zip(dirFile).getFile();
        configFilesResq.setConfigFileZipData(CcFileUtils.readFileToByteArray(zipFile));

        return configFilesResq;
    }

}
