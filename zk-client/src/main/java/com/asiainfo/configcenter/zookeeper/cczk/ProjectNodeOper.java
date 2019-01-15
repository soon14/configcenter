package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.Arrays;
import java.util.List;

public class ProjectNodeOper {
    private ZookeeperManager zookeeperManager;

    public ProjectNodeOper(ZookeeperManager zookeeperManager){
        this.zookeeperManager = zookeeperManager;
    }

    /**
     * 创建项目节点 同时需要创建项目环境的根节点
     * @param projectName 项目名称
     * @param value 节点数据
     * @throws KeeperException zk异常
     * @throws InterruptedException 异常
     */
    public void createProjectNode(String projectName,String value)throws KeeperException,InterruptedException{
        zookeeperManager.createNode(getProjectNodePath(projectName),value, CreateMode.PERSISTENT);
        String envRootPath = getEnvRootPath(projectName);
        if(!zookeeperManager.existNode(envRootPath)){
            zookeeperManager.createNode(envRootPath,"",CreateMode.PERSISTENT);
        }
    }

    /**
     * 删除项目节点  节点下不能存在环境节点
     * @param projectName
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteProjectNode(String projectName)throws KeeperException,InterruptedException{
        //查看是否有环境节点
        String projectNodePath = getProjectNodePath(projectName);
        String envsNodePath = projectNodePath + "/envs";
        if(zookeeperManager.existNode(envsNodePath)){
            List<String> child = zookeeperManager.getChild(envsNodePath,null);
            if(child != null && child.size() >0){
                throw new RuntimeException("项目："+projectNodePath+"下存在环境节点："+ Arrays.toString(child.toArray(new String[0])));
            }
        }
        zookeeperManager.deleteNodeRecursion(projectNodePath);
    }

    public boolean projectNodeExist(String projectName)throws KeeperException,InterruptedException{
        return zookeeperManager.existNode(getProjectNodePath(projectName));
    }

    private String getProjectNodePath(String projectName){
        return "/apps/"+projectName;
    }

    private String getEnvRootPath(String projectName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs");
        return sb.toString();
    }

}
