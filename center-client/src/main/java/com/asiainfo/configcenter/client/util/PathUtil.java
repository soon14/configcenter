package com.asiainfo.configcenter.client.util;

public class PathUtil {

    public static String getEnvRootPath(String projectName,String envName){
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName);
        return sb.toString();
    }

    public static String getInstancePath(String projectName,String envName,String nodeType,String instanceName) {
        StringBuilder sb = new StringBuilder("/apps/");
        sb.append(projectName)
                .append("/envs/")
                .append(envName)
                .append("/")
                .append(nodeType)
                .append("/")
                .append(instanceName);
        return sb.toString();
    }
}
