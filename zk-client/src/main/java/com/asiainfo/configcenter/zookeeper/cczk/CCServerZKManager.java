package com.asiainfo.configcenter.zookeeper.cczk;

import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置中心zk管理器
 */
public class CCServerZKManager {
    private static CCServerZKManager ccZookeeperZkManager;
    private static ZookeeperManager zookeeperManager;
    private static ProjectNodeOper projectNodeOper;
    private static EnvNodeOper envNodeOper;
    private static ConfigFileNodeOper configFileNodeOper;
    private static ConfigItemNodeOper configItemNodeOper;
    private static InstanceNodeOper instanceNodeOper;

    /**
     * 获取实例
     * @return 实例
     */
    public static CCServerZKManager getInstance(){
        return ccZookeeperZkManager;
    }


    /**
     * 初始化
     */
    public static void init(String host,String defaultPrefix){
        try {
            //初始化zk连接
            ccZookeeperZkManager = new CCServerZKManager();
            zookeeperManager = ZookeeperManager.getInstance();
            zookeeperManager.init(host,defaultPrefix,true);
            projectNodeOper = new ProjectNodeOper(zookeeperManager);//项目管理器
            envNodeOper = new EnvNodeOper(zookeeperManager);//环境管理器
            configFileNodeOper = new ConfigFileNodeOper(zookeeperManager);//配置文件管理器
            configItemNodeOper = new ConfigItemNodeOper(zookeeperManager);//配置项管理器
            instanceNodeOper =new InstanceNodeOper(zookeeperManager);//实例管理器
        }catch (IOException e){
            throw new RuntimeException("初始化zookeeper失败",e);
        } catch (InterruptedException e) {
            throw new RuntimeException("初始化zookeeper失败",e);
        }
    }

    /**
     * 获取zk原始管理器
     * @return zk管理器
     */
    public ZookeeperManager getZKManager(){
        return zookeeperManager;
    }

    /**
     * 获取项目zk管理器
     * @return 项目zk管理器
     */
    public ProjectNodeOper getProjectNodeOper() {
        return projectNodeOper;
    }

    /**
     * 获取环境zk管理器
     * @return 环境zk管理器
     */
    public EnvNodeOper getEnvNodeOper() {
        return envNodeOper;
    }

    /**
     * 获取配置文件zk管理器
     * @return 配置文件zk管理器
     */
    public ConfigFileNodeOper getConfigFileNodeOper() {
        return configFileNodeOper;
    }

    /**
     * 获取配置项zk管理器
     * @return 配置项zk管理器
     */
    public ConfigItemNodeOper getConfigItemNodeOper() {
        return configItemNodeOper;
    }

    /**
     * 获取实例zk管理器
     * @return 实例zk管理器
     */
    public InstanceNodeOper getInstanceNodeOper(){
        return instanceNodeOper;
    }
}
