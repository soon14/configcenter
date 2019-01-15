package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.util.PathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oulc on 2018/7/26.
 */
public class InstanceNodeOper {
    private ZookeeperManager zookeeperManager;

    public InstanceNodeOper(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    /**
     * 获取环境下存活的实例
     * @param projectName 项目名称
     * @param envName 环境名称
     * @return 存活的实例
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public List<String> getInstances(String projectName,String envName)throws KeeperException,InterruptedException{
        return zookeeperManager.getChild(PathUtil.getInstanceRootPath(projectName, envName),null);
    }

    /**
     * 读取实例节点的数据
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param insName 实例名称
     * @return 节点数据
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public String readInstanceNodeData(String projectName,String envName,String insName)throws KeeperException,InterruptedException{
        return zookeeperManager.readNodeData(PathUtil.getInstancePath(projectName, envName, insName));
    }

    /**
     * 创建一个实例的节点
     * @param projectName 应用名称
     * @param envName 环境名称
     * @param insName 实例名称
     * @param value 节点数据
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public void createInstanceNode(String projectName,String envName,String insName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.createNode(PathUtil.getInstancePath(projectName, envName, insName),value, CreateMode.EPHEMERAL);
    }

    /**
     * 判断实例是否存在
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param isnName 实例名称
     * @return 存在:true 不存在:false
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public boolean instanceExist(String projectName,String envName,String isnName)throws KeeperException,InterruptedException{
        return zookeeperManager.existNode(PathUtil.getInstancePath(projectName, envName, isnName));
    }

    public  void writeInstanceNodeData(String projectName,String envName,String insName,String data)throws KeeperException,InterruptedException{
        zookeeperManager.writeNodeData(PathUtil.getInstanceUpdatePath(projectName, envName, insName),data);
    }



}
