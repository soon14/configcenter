package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.util.PathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.Arrays;
import java.util.List;

public class EnvNodeOper {

    private ZookeeperManager zookeeperManager;
    public EnvNodeOper(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    /**
     * 创建环境节点
     * @param projectName
     * @param envName
     * @param value
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createEnvNode(String projectName,String envName,String value)throws KeeperException,InterruptedException{
        //创建环境节点
        zookeeperManager.createNode(getEnvNodePath(projectName,envName),value, CreateMode.PERSISTENT);
        //创建实例根节点
        zookeeperManager.createNode(PathUtil.getInstanceRootPath(projectName, envName),"",CreateMode.PERSISTENT);
        //创建实例更新节点
        zookeeperManager.createNode(PathUtil.getInstanceUpdateRootPath(projectName,envName),"",CreateMode.PERSISTENT);
        //创建实例返回结果节点
        zookeeperManager.createNode(PathUtil.getInstanceResultRootPath(projectName,envName),"",CreateMode.PERSISTENT);
        //创建配置根节点
        zookeeperManager.createNode(PathUtil.getConfigRootNodePath(projectName,envName),"",CreateMode.PERSISTENT);
        //创建配置文件根节点
        zookeeperManager.createNode(PathUtil.getConfigFileRootNodePath(projectName,envName),"",CreateMode.PERSISTENT);
        //创建配置项根节点
        zookeeperManager.createNode(PathUtil.getConfigItemRootNodePath(projectName,envName),"",CreateMode.PERSISTENT);
    }

    /**
     * 删除环境节点
     * @param projectName
     * @param envName
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteEnvNode(String projectName,String envName)throws KeeperException,InterruptedException{
        String filesRootPath = getEnvNodePath(projectName,envName)+"/configs/files";
        if(zookeeperManager.existNode(filesRootPath,null)){
            List<String> fileChild = zookeeperManager.getChild(filesRootPath,null);
            if(fileChild != null && fileChild.size() >0){
                throw new RuntimeException("项目:"+projectName+",环境:"+envName+"删除失败，原因：环境下存在配置文件子节点:"+ Arrays.toString(fileChild.toArray(new String[0])));
            }
        }
        String itemRootPath = getEnvNodePath(projectName,envName)+"/configs/items";
        if(zookeeperManager.existNode(itemRootPath)){
            List<String> itemChild = zookeeperManager.getChild(itemRootPath,null);
            if(itemChild != null && itemChild.size() >0){
                throw new RuntimeException("项目:"+projectName+",环境:"+envName+"删除失败，原因：环境下存在配置项子节点:"+ Arrays.toString(itemChild.toArray(new String[0])));
            }
        }
        zookeeperManager.deleteNodeRecursion(getEnvNodePath(projectName,envName));

    }

    /**
     * 写环境节点数据
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param value 节点数据
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public void writeEnvNodeData(String projectName,String envName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.writeNodeData(getEnvNodePath(projectName ,envName),value);
    }

    /**
     * 读环境节点数据
     * @param projectName 项目名称
     * @param envName 环境名称
     * @return 节点数据
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public String readEnvNodeData(String projectName,String envName)throws KeeperException,InterruptedException{
        return zookeeperManager.readNodeData(getEnvNodePath(projectName ,envName));
    }

    /**
     * 判断节点是否存在
     * @param projectName 项目名称
     * @param envName 环境名称
     * @return true:存在  false:不存在
     * @throws KeeperException 异常
     * @throws InterruptedException 异常
     */
    public boolean envNodeExist(String projectName,String envName)throws KeeperException,InterruptedException{
        return zookeeperManager.existNode(getEnvNodePath(projectName, envName));
    }

    private String getEnvNodePath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName);
        return sb.toString();
    }

    private String getInstanceRootPath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-info");
        return sb.toString();
    }
}
