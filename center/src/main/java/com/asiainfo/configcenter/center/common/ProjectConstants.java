package com.asiainfo.configcenter.center.common;

/**
 * 项目常量值
 * Created by bawy on 18/7/3.
 */
public class ProjectConstants {


    public static final String PROJECT_USE_MODE_TEST = "test";


    /**
     * 用户状态值
     * 0：失效
     * 1：生效
     * 2：待审核
     */
    public static final byte STATUS_INVALID = 0;
    public static final byte STATUS_VALID = 1;
    public static final byte STATUS_AUDIT = 2;

    /**
     * session中保存登录信息的属性名称
     */
    public static final String CURRENT_USER = "cUser";

    /**
     * 权限类型
     * 1：菜单
     * 2：后端服务url
     */
    public static final byte PERMISSION_TYPE_MENU = 1;
    public static final byte PERMISSION_TYPE_SERVICE = 2;

    /**
     * 后台服务类型
     * 0：无需登陆
     */
    public static final byte SERVICE_TYPE_NO_LOGIN = 0;
    public static final byte SERVICE_TYPE_NEED_LOGIN = 1;

    /**
     * 根菜单标识
     */
    public static final int ROOT_MENU_ID = 0;

    /**
     * 管理员帐号
     */
    public static final int ACCOUNT_ROOT = 100000;

    /**
     * 角色类型
     * 1：系统角色
     * 2：应用角色
     * 3：组织角色
     */
    public static final byte ROLE_TYPE_SYSTEM = 1;
    public static final byte ROLE_TYPE_APP = 2;
    public static final byte ROLE_TYPE_ORG = 3;

    /**
     * 角色编码
     * 系统角色
     * 1000：管理员
     * 1001：普通用户
     * 应用角色
     * 1002: 经理
     * 1003：维护人员
     * 1004：开发人员
     */
    public static final int ROLE_ROOT = 1000;
    public static final int ROLE_NORMAL = 1001;

    public static final int ROLE_MANAGER = 1002;
    public static final int ROLE_MAINTAINER = 1003;
    public static final int ROLE_DEVELOPER = 1004;

    /**
     * 用户扩展信息
     * phone:手机号码
     */
    public static final String USER_EXT_INFO_PHONE = "PHONE";


    /**
     * web socket 用户key
     */
    public static String WEB_SOCKET_USER_KEY = "webSocketUser";

    public static String NOTIFICATION_REDIS_KEY = "config-center-notify";


    public static String APP_INSTANCE_INFO_IP = "instanceIp";
    public static final String APP_INSTANCE_INFO_CONFIG_ITEM = "configItem";
    public static final String APP_INSTANCE_INFO_CONFIG_FILE = "configFile";
    public static final String APP_INSTANCE_INFO_CONFIG_FILE_NAME = "configFileName";
    public static final String APP_INSTANCE_INFO_CONFIG_ITEM_NAME = "configItemName";
    public static final String APP_INSTANCE_INFO_CONFIG_FILE_VERSION = "configFileVersion";
    public static final String APP_INSTANCE_INFO_CONFIG_ITEM_VERSION = "configItemVersion";
    public static final String APP_INSTANCE_INFO_ID = "instanceId";



    /**
     * redis缓存键值
     */
    public static final String REDIS_STATIC_DATA_KEY = "cc:static-data";
    public static final String REDIS_ADMINISTRATOR_LIST_KEY = "cc:administrator-list";
    public static final String REDIS_SYSTEM_ROLE_MENU_KEY = "cc:system-role:menu";
    public static final String REDIS_APP_ROLE_SERVICE_KEY = "cc:app-role:service";
    public static final String REDIS_SYS_ROLE_SERVICE_KEY = "cc:sys-role:service";
    public static final String REDIS_ORG_TREE_KEY = "cc:org-tree";
    public static final String REDIS_ORG_TREE_PATH_KEY = "cc:org-tree-path";
    public static final String REDIS_ROLE_KEY = "cc:role";

    public static final String GIT_BRANCH_NAME = "master";

    /**
     * 任务状态
     * 1：待提交
     * 2：审核中
     * 3：结束
     */
    public static final byte TASK_STATE_TO_SUBMIT = 1;
    public static final byte TASK_STATE_IN_REVIEW = 2;
    public static final byte TASK_STATE_END = 3;


    /**
     * 操作状态
     * 1：提交成功
     * 2：待审核
     * 3：审核中
     * 4：审核通过
     * 5：审核不通过
     */
    public static final byte OPERATE_STATE_SUBMIT = 1;
    public static final byte OPERATE_STATE_TO_AUDIT = 2;
    public static final byte OPERATE_STATE_IN_REVIEW = 3;
    public static final byte OPERATE_STATE_REVIEW_PASS = 4;
    public static final byte OPERATE_STATE_REVIEW_NOT_PASS = 5;

    /**
     * 任务操作类型
     *  1：提交
     *  2：审核通过
     *  3：审核不通过
     */
    public static final byte OPERATE_TYPE_SUBMIT = 1;
    public static final byte OPERATE_TYPE_PASS= 2;
    public static final byte OPERATE_TYPE_NOT_PASS = 3;

    /**
     * 配置变更类型
     * 1：新增
     * 2：修改
     * 3：删除
     * 4: 策略变更
     */
    public static final byte CONFIG_CHANGE_TYPE_ADD = 1;
    public static final byte CONFIG_CHANGE_TYPE_MOD = 2;
    public static final byte CONFIG_CHANGE_TYPE_DEL = 3;
    public static final byte CONFIG_CHANGE_TYPE_STRATEGY_CHANGE = 4;

    public static final String CONFIG_MOD_OPTION = "strategy";

    /**
     * 任务类型
     *  1：配置变更任务
     *  2：配置推送任务
     */
    public static final byte TASK_TYPE_CONFIG_CHANGE = 1;
    public static final byte TASK_TYPE_CONFIG_PUSH = 2;

    /**
     * 配置类型
     *  1：配置文件
     *  2：配置项
     */
    public static final byte CONFIG_TYPE_FILE = 1;
    public static final byte CONFIG_TYPE_ITEM = 2;

    /**
     * 操作记录详情类型
     * 1:实例更新详情
     */
    public static final byte OPER_RECORD_TYPE_INSTANCE_INFO = 1;

    //创建临时文件 临时目录的前缀
    public static final String TEMP_FILE_PREFIX = "configCenter";

    public static final String DEFAULT_ENCODE_TYPE = "UTF-8";

    //配置历史数据展示数量
    public static final int CONFIG_HIS_DATA_SIZE = 10;

    /**
     * 更新策略类型
     * F：反射对field操作
     * M：反射调用方法
     * C：构造方法
     */
    public static final String STRATEGY_TYPE_FIELD = "F";
    public static final String STRATEGY_TYPE_METHOD = "M";
    public static final String STRATEGY_TYPE_CONSTRUCTOR = "C";


    /**
     * 配置推送类型 1:立即推送 2：定时推送
     */
    public static final byte PUSH_TYPE_NOW = 1;
    public static final byte PUSH_TYPE_TIME = 2;

    /**
     * 任务详情扩展信息
     *  PUSH_TIME:推送时间表达式
     *  INSTANCES:实例信息
     */
    public static final String TASK_EXT_INFO_PUSH_TIME = "PUSH_TIME";
    public static final String TASK_EXT_INFO_INSTANCES = "INSTANCES";

    public static final String TASK_INFO_KEY_SELECTED_TYPE = "selectedType";
    public static final String TASK_INFO_KEY_SELECTED_ITEM = "selectedItem";
    public static final String TASK_INFO_KEY_CANCEL_ITEM = "cancelItem";
    public static final String TASK_INFO_KEY_INS_NAME = "insName";
    public static final String TASK_INFO_KEY_INS_IP = "insIp";
    public static final String TASK_INFO_KEY_PUSH_TIME = "pushTime";

    public static final String INSTANCE_SELECTED_TYPE_NORMAL = "1";
    public static final String INSTANCE_SELECTED_TYPE_ALL = "2";

    public static final String QUARTZ_PUSH_CONFIG_GROUP_NAME = "push-config-group";

    public static final String QUARTZ_PUSH_CONFIG_TASK_ID = "taskId";


    /**
     *
     */
    public static final String USER_ACCESS_RECENT_PROJECT = "RECENT_PROJECT";

    /**
     * 组织变更类型
     */
    public static final byte ORG_CHANGE_TYPE_ADD = 1;
    public static final byte ORG_CHANGE_TYPE_MOD = 2;
    public static final byte ORG_CHANGE_TYPE_DEL = 3;

}
