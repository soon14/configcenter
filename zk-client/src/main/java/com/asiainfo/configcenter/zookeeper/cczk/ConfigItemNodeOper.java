package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.util.PathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

public class ConfigItemNodeOper {
    private ZookeeperManager zookeeperManager;
    public ConfigItemNodeOper(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }
    /**
     * 创建配置项节点
     * @param projectName
     * @param envName
     * @param configItemName
     * @param value
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createConfigItemNode(String projectName,String envName,String configItemName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.createNode(PathUtil.getConfigItemNodePath(projectName,envName,configItemName),value, CreateMode.PERSISTENT);
    }

    /**
     * 删除配置项节点
     * @param projectName
     * @param envName
     * @param configIteName
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteConfigItemNode(String projectName,String envName,String configIteName)throws KeeperException,InterruptedException{
        zookeeperManager.deleteNodeRecursion(PathUtil.getConfigItemNodePath(projectName,envName,configIteName));
    }

    /**
     * 写配置项数据
     * @param projectName
     * @param envName
     * @param configItemName
     * @param value
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void writeConfigItemNodeData(String projectName,String envName,String configItemName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.writeNodeData(PathUtil.getConfigItemNodePath(projectName,envName,configItemName),value);
    }

    /**
     * 读配置项数据
     * @param projectName
     * @param envName
     * @param configItemName
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String readConfigItemNodeData(String projectName,String envName,String configItemName)throws KeeperException,InterruptedException{
        return zookeeperManager.readNodeData(PathUtil.getConfigItemNodePath(projectName,envName,configItemName));
    }

    /**
     * 判断配置项节点是否存在
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param configItemName 配置项名称
     * @return 存在:true  不存在:false
     * @throws KeeperException zk异常
     * @throws InterruptedException zk异常
     */
    public boolean configItemNodeExist(String projectName,String envName,String configItemName)throws KeeperException,InterruptedException{
        return zookeeperManager.existNode(PathUtil.getConfigItemNodePath(projectName, envName, configItemName));
    }

}
