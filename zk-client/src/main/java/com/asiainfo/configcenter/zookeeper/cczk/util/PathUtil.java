package com.asiainfo.configcenter.zookeeper.cczk.util;

/**
 * Created by oulc on 2018/8/7.
 */
public class PathUtil {
    public static String getConfigRootNodePath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/configs");
        return sb.toString();
    }

    public static String getConfigFileRootNodePath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/configs/files");
        return sb.toString();
    }

    public static String getConfigItemRootNodePath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/configs/items");
        return sb.toString();
    }
    public static String getInstanceRootPath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-info");
        return sb.toString();
    }

    /**
     * 获取配置文件的节点路径
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param configItemName 配置项名称
     * @return 路径
     */
    public static String getConfigItemNodePath(String projectName,String envName,String configItemName){
        StringBuilder sb = new StringBuilder();
        sb.append("/apps/")
                .append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/configs/items/")
                .append(configItemName);
        return sb.toString();
    }

    /**
     * 获取配置文件的节点路径
     * @param projectName
     * @param envName
     * @param configFileName
     * @return
     */
    public static String getConfigFileNodePath(String projectName,String envName,String configFileName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/configs/files/")
                .append(configFileName);
        return sb.toString();
    }

    /**
     * 获取实例的路径
     * @param projectName 项目名称
     * @param envName 环境名称
     * @param insName 实例名称
     * @return 路径
     */
    public static String getInstancePath(String projectName,String envName,String insName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-info/")
                .append(insName);
        return sb.toString();
    }

    public static String getInstanceUpdateRootPath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-update-info");
        return sb.toString();
    }

    public static String getInstanceUpdatePath(String projectName,String envName,String insName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-update-info/")
                .append(insName);
        return sb.toString();
    }

    public static String getInstanceResultRootPath(String projectName,String envName) {
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/instances-result-info");
        return sb.toString();
    }


}
