package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import com.asiainfo.configcenter.zookeeper.cczk.util.PathUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

/**
 * The type Config file node oper.
 */
public class ConfigFileNodeOper {
    private ZookeeperManager zookeeperManager;

    /**
     * Instantiates a new Config file node oper.
     *
     * @param zookeeperManager the zookeeper manager
     */
    public ConfigFileNodeOper(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    /**
     * 创建配置文件节点
     *
     * @param projectName    the project name
     * @param envName        the env name
     * @param configFileName the config file name
     * @param value          the value
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public void createConfigFileNode(String projectName,String envName,String configFileName,String value)throws KeeperException,InterruptedException{
        String []fileNameSplit =  configFileName.split("/");
        if(fileNameSplit.length > 0){
            String nodeName = fileNameSplit[0];
            for(int i = 0 ;i < fileNameSplit.length ;i++){
                if(i != fileNameSplit.length){
                    if(!zookeeperManager.existNode(PathUtil.getConfigFileNodePath(projectName, envName, nodeName))){
                        zookeeperManager.createNode(PathUtil.getConfigFileNodePath(projectName, envName, nodeName),value, CreateMode.PERSISTENT);
                    }
                }else{
                    zookeeperManager.createNode(PathUtil.getConfigFileNodePath(projectName, envName, nodeName),value, CreateMode.PERSISTENT);
                }
                if(i < fileNameSplit.length -1){
                    nodeName = nodeName + "/" + fileNameSplit[i+1];
                }
            }
        }

    }




    /**
     * 删除配置文件节点
     *
     * @param projectName    the project name
     * @param envName        the env name
     * @param configFileName the config file name
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public void deleteConfigFileNode(String projectName,String envName,String configFileName)throws KeeperException,InterruptedException{
        zookeeperManager.deleteNodeRecursion(PathUtil.getConfigFileNodePath(projectName,envName,configFileName));
    }

    /**
     * 写配置文件节点数据
     *
     * @param projectName    the project name
     * @param envName        the env name
     * @param configFileName the config file name
     * @param value          the value
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public void writeConfigFileNodeData(String projectName,String envName,String configFileName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.writeNodeData(PathUtil.getConfigFileNodePath(projectName,envName,configFileName),value);
    }

    /**
     * 读配置文件节点数据
     *
     * @param projectName    the project name
     * @param envName        the env name
     * @param configFileName the config file name
     * @return string
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public String readConfigFileNodeData(String projectName,String envName,String configFileName)throws KeeperException,InterruptedException{
        return zookeeperManager.readNodeData(PathUtil.getConfigFileNodePath(projectName,envName,configFileName));
    }

    /**
     * Config file node exist boolean.
     *
     * @param projectName    the project name
     * @param envName        the env name
     * @param configFileName the config file name
     * @return the boolean
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public boolean configFileNodeExist(String projectName,String envName,String configFileName)throws KeeperException,InterruptedException{
        return zookeeperManager.existNode(PathUtil.getConfigFileNodePath(projectName, envName, configFileName));
    }



}
