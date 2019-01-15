package com.asiainfo.configcenter.client.constants;

public class ProjectConstants {
    /**
     * 启动类参数
     * */
    public static final String CLIENT_PROJECT_NAME = "client.projectName";
    public static final String CLIENT_ENV_NAME = "client.envName";
    public static final String CLIENT_INSTANCE_NAME = "client.instanceName";
    public static final String CLIENT_IP = "client.ip";
    public static final String BOOT_STRAP_CLASS_PATH = "sun.boot.class.path";
    public static final String USER_CLASS_PATH= "java.class.path";


    public static final String TEMP_FILE_PREFIX = "configCenter";
    /**
     * httpClient常量
     * */
    public static final String DEFAULT_MIME_TYPE = "application/json";
    public static final String RETURN_RESULT_NAME = "result";
    public static final int CONNECTION_TIME_OUT = 60000;
    public static final int SOCKET_TIME_OUT = 15000;

    /**
     * 文件操作常量
     * */
    public static final String FILE_DIR_PREFIX = "";
    public static final String CONFIG_FILE_TEMP_DIR = "CCTemp";
    public static final String CONFIG_FILE_TEMP_DIR_BACKUP = "CCTemp_backup";

    /**
     * 服务器URL
     * */
    public static final String DOWN_LOAD_FILE_URL = "/center/client/getPushFileAndStrategy";
    public static final String DOWNLOAD_APP_ENV_FILE_URL = "/center/client/getAllFile";
    public static final String DOWNLOAD_CONFIG_ITEM_URL = "/center/client/getPushConfigItem";

    /**
     * 配置中心客户端配置文件
     */
    public static final String CONFIG_FILE_NAME = "cc-client.properties";

    /**
     * 配置文件中的key
     * */
    public static final String CONFIG_KEY_SERVER_HOST = "server.host";
    public static final String CONFIG_KEY_ZOOKEEPER_HOST = "zookeeper.host";
    public static final String CONFIG_KEY_ZOOKEEPER_PREFIX = "zookeeper.prefix";
    public static final String CONFIG_KEY_FILE_ENCODING = "file.encoding";

    //默认配置
    public static final String DEFAULT_ZOOKEEPER_PREFIX = "/config-center";
    public static final String DEFAULT_ENCODING_TYPE = "UTF-8";
    public static final String CONFIG_FILE_ZIP = "configFileZipData";

}
