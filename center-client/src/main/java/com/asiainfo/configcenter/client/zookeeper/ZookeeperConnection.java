package com.asiainfo.configcenter.client.zookeeper;

import com.asiainfo.configcenter.client.common.ConfigLoader;
import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.asiainfo.configcenter.client.execute.UpdateConfig;
import com.asiainfo.configcenter.client.pojo.Instance;
import com.asiainfo.configcenter.client.util.JSONUtil;
import com.asiainfo.configcenter.client.util.PathUtil;
import com.asiainfo.configcenter.zookeeper.ZookeeperManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ZookeeperConnection {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperConnection.class);
    private static ZookeeperManager zookeeperManager;
    private static String projectName;
    private static String envName;
    private static String instanceName;
    private static String ip;

    static  {
        projectName = System.getProperty(ProjectConstants.CLIENT_PROJECT_NAME);
        envName = System.getProperty(ProjectConstants.CLIENT_ENV_NAME);
        instanceName = System.getProperty(ProjectConstants.CLIENT_INSTANCE_NAME);
        ip = getHostIpAddress();
    }

    public static void zkConnection() throws IOException, InterruptedException {
        checkParams();
        String host = ConfigLoader.getConfigWithCheck(ProjectConstants.CONFIG_KEY_ZOOKEEPER_HOST);
        String defaultPreFix = ConfigLoader.getConfigDefault(ProjectConstants.CONFIG_KEY_ZOOKEEPER_PREFIX, ProjectConstants.DEFAULT_ZOOKEEPER_PREFIX);
        zookeeperManager = ZookeeperManager.getInstance();
        zookeeperManager.init(host,defaultPreFix,false, new ClientCallback());
    }

    private static void checkParams() {
        if(!StringUtils.isNotEmpty(projectName)) {
            logger.error("应用名称不能为空");
            throw new Error("应用名称不能为空");
        } else if(!StringUtils.isNotEmpty(envName)) {
            logger.error("应用环境不能为空");
            throw new Error("应用环境不能为空");
        } else if(!StringUtils.isNotEmpty(instanceName)) {
            logger.error("实例名称不能为空");
        } else if(!StringUtils.isNotEmpty(ip)) {
            logger.error("实例所在ip获取失败");
            throw new Error("实例所在ip获取失败");
        }
    }

    public static void createNode() throws KeeperException, InterruptedException {
        String envRootPath = PathUtil.getEnvRootPath(projectName,envName);
        if(zookeeperManager.existNode(envRootPath)) {
            createInstanceInfoChildNode();
            createInstanceUpdateInfoChildNode();
            createInstanceResultInfoChildNode();
        } else {
            logger.error("指定的应用或者环境节点不存在，请先到配置中心创建应用和环境");
            throw new Error("指定的应用或者环境节点不存在，请先到配置中心创建应用和环境");
        }
    }

    private static void createInstanceInfoChildNode() throws KeeperException, InterruptedException {
        String instanceInfoPath = PathUtil.getInstancePath(projectName,envName,"instances-info",instanceName);
        Instance instance = new Instance(instanceName,ip,System.currentTimeMillis());
        zookeeperManager.createNode(instanceInfoPath, JSONUtil.obj2JsonStr(instance), CreateMode.EPHEMERAL);
    }

    private static void createInstanceUpdateInfoChildNode() throws KeeperException, InterruptedException {
        String instanceUpdateInfoPath = PathUtil.getInstancePath(projectName,envName,"instances-update-info",instanceName);
        zookeeperManager.createNode(instanceUpdateInfoPath,"",CreateMode.EPHEMERAL);
        zookeeperManager.existNode(instanceUpdateInfoPath,new NodeWatcher(instanceUpdateInfoPath));
    }

    private static void createInstanceResultInfoChildNode() throws KeeperException, InterruptedException {
        String instanceResultInfoPath = PathUtil.getInstancePath(projectName,envName,"instances-result-info",instanceName);
        zookeeperManager.createNode(instanceResultInfoPath,"",CreateMode.EPHEMERAL);
    }

    public static void writeDataToResultInfoChildNode(String nodeInfo) throws KeeperException, InterruptedException {
        String instanceResultInfoPath = PathUtil.getInstancePath(projectName,envName,"instances-result-info",instanceName);
        zookeeperManager.writeNodeData(instanceResultInfoPath,nodeInfo);
    }

    //节点监听器
    private static class NodeWatcher implements Watcher {

        private String instanceUpdateInfoPath;

        private NodeWatcher(String instanceUpdateInfoPath) {
            this.instanceUpdateInfoPath = instanceUpdateInfoPath;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            try {
                getUpdateInfo(instanceUpdateInfoPath);
            } catch (Exception e) {
                logger.error("回调接口出错",e);
                e.printStackTrace();
            }
        }
    }

    private static void getUpdateInfo(String instanceUpdateInfoPath) throws KeeperException, InterruptedException {
        //监听器是一次性触发的，所以要每次获取数据后都需要将监听器重新获取
        String nodeInfo = zookeeperManager.readNodeData(instanceUpdateInfoPath,new NodeWatcher(instanceUpdateInfoPath));
        UpdateConfig.updateConfig(nodeInfo);
    }

    private static String getHostIpAddress() {
        String ipAddress = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ipAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取实例IP出错",e);
        }
        return ipAddress;
    }
}
