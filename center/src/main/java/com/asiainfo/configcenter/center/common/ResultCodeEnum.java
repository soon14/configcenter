package com.asiainfo.configcenter.center.common;

/**
 * 异常编码枚举
 * Created by bawy on 2018/7/6 9:46.
 */
public enum ResultCodeEnum {

    /**
     * 公用枚举
     */
    SUCCESS("0000","成功"),
    PARAM_ERROR("0001","参数校验错误,{0}"),
    DUP_SUBMIT_ERROR("0002","非法提交或重复提交"),
    NOT_LOGIN_ERROR("0003","用户{0}未登录  "),
    SESSION_TIMEOUT_ERROR("0004","会话超时，请重新登录"),
    DATA_ERROR("0005", "数据异常，{0}"),
    ILLEGAL_OPERATION_ERROR("0110", "非法操作，{0}"),
    USER_NOT_EXIST("0006","用户不存在:{0}"),
    USER_COMMON_ERROR("0007","{0}"),

    /**
     * 系统模块ES0001开始
     */
    SYSTEM_COMMON_ERROR("ES0001","{0}"),
    USERNAME_OR_PASSWORD_ERROR("ES0002","用户名或密码错误"),
    EMAIL_IS_USE("ES0003", "邮箱已经注册"),
    USERNAME_IS_USE("ES0004", "用户名已经被使用"),
    USER_STATUS_ABNORMAL("ES0005", "用户状态异常,{0}"),
    OLD_PASSWORD_ERROR("ES0006", "原密码输入错误"),
    SELECT_KEY_ERROR("ES0007", "下拉列表数据对应Key错误，无法获取对应下拉列表数据"),
    ORG_CAN_NOT_DEL_ERROR("ES0008", "该组织不可以删除，{0}"),
    ORG_NO_PERMISSION_ERROR("ES0009", "{0}，无权限进行此操作"),
    ORG_COMMON_ERROR("ES0010","{0}"),
    ORG_LEVEL_LIMIT("ES0011", "最多添加五级组织"),
    /**
     * 任务模块ET0001开始
     */
    TASK_COMMON_ERROR("ET0001","{0}"),
    TASK_CONFIG_CHANGE_EXIST("ET0002", "配置变更已经存在于待提交任务中，{0}"),
    TASK_NO_AUDIT_STRATEGY_ERROR("ET0003", "操作员{0}在当前环境{1}中尚未配置审核策略，不允许提交任务"),
    TASK_AUDIT_OPERATOR_NOT_EXIST("ET0004", "操作员{0}的上级操作员{1}已经不在当前应用{2}中，需要修改审核策略"),
    TASK_AUDIT_STRATEGY_OPERATOR_REPEAT_ERROR("ET0005", "审核策略异常，操作员{0}重复出现在审核链中"),
    TASK_TYPE_ILLEGAL("ET0006", "任务类型非法"),
    TASK_CONFIG_CHANGE_REPEAT_ERROR("ET0007", "{0}新增或变更已存在与待审核任务{1}中，在此任务结束前无法重复新增或变更此配置"),
    TASK_CONFIG_PUSH_EXIST_ERROR("ET0008", "当前环境已存在正在审核中的配置推送任务，请等待该任务审核结束"),
    TASK_CONFIG_PUSH_HAS_THIS_ERROR("ET0009", "该配置的推送已经存在于待提交任务中,{0}"),
    TASK_AUDIT_STRATEGY_ORG_EXIST_ERROR("ET0010", "此环境的审核策略中已经包含该组织"),
    TASK_CAN_NOT_CHANGE_AUDIT_STRATEGY_ERROR("ET0011", "当前环境存在正在审核中的任务，不可变更审核策略"),
    TASK_ENV_NO_STRATEGY_ERROR("ET0012", "当前环境未设置审核策略，不允许提交任务"),
    TASK_SELF_ORG_NOT_IS_FIRST_ERROR("ET0013", "提交人自身组织必须放置在审核策略第一步"),
    /**
     * 应用模块EA0001开始
     */
    APP_COMMON_ERROR("EA0001","{0}"),
    APP_EXIST_ERROR("EA0002","同名App已存在，{0}"),
    APP_NOT_EXIST_ERROR("EA0003","项目不存在"),
    APP_PERMISSION_ERROR("EA0004", "用户当前角色无此操作权限"),
    APP_USER_EXIST_ERROR("EA0005", "用户已经加入该应用"),
    APP_USER_NOT_EXIST_ERROR("EA0006", "异常，用户不在此应用中"),
    APP_ENV_NOT_EXIST_ERROR("EA0007","环境{0}不存在"),
    /**
     * 配置模块EA0001开始
     */
    CONFIG_COMMON_ERROR("EC0001","{0}"),
    CONFIG_ITEM_KEY_EXIST_ERROR("EC0002", "该环境下已存在key为{0}的配置项"),
    CONFIG_FILE_DELETE_ERROR("EC0003","删除配置文件失败,{0}"),

    /**
     * ZOOKEEPER
     */
    ZK_COMMON_ERROR("EZ0001","{0}"),
    ZK_ERROR("EZ0002","zk异常，请尝试重新操作。如果过多次操作不成功，请联系管理人员。"),


    /**
     * git
     * @return
     */
    GIT_COMMON_ERROR("EG0001","{0}"),
    GIT_ERROR("EG0001","git出现异常，请尝试重新操作，如果多次失败，请联系管理员"),

    INSTANCE_COMMON_ERROR("EI0001","{0}"),


    STRATEGY_COMMON_ERROR("ES0001","{0}"),

    UNDEFINE_ERROR("9999","未知错误");

    private String errorCode;
    private String errorMsg;

    ResultCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }



    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
