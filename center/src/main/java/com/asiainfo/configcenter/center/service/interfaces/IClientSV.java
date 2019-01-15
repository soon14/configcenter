package com.asiainfo.configcenter.center.service.interfaces;

import com.asiainfo.configcenter.center.vo.client.ClientConfigFilesResp;
import com.asiainfo.configcenter.center.vo.client.ClientReqVO;
import com.asiainfo.configcenter.center.vo.client.ClientRespVO;

import java.util.List;

public interface IClientSV {

    /**
     * 根据推送配置文件信息获取配置文件数据
     * @param clientReqVO 请求参数对象
     * @return 配置文件集合
     */
    List<ClientRespVO> getPushFileAndStrategy(ClientReqVO clientReqVO);

    /**
     * 根据推送配置项信息，获取配置项数据
     * @author Erick
     * @date 2018/08/29
     * @param clientReqVO 请求参数
     * @return 配置项实体
     * */
    List<ClientRespVO> getPushConfigItem(ClientReqVO clientReqVO);

    /**
     * 根据 AppName, EnvName 获取当前所有配置文件
     *
     * @author Erick
     * @date 2018/09/06
     * @param appName 应用名称
     * @param envName 环境名称
     * @return 该应用环境下配置文件
     * */
    ClientConfigFilesResp getAllFile(String appName, String envName);

}
