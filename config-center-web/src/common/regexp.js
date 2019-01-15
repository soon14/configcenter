//正则表达式

/**
 * 手机号校验表达式
 * @type {string}
 */
export const REGEXP_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

//密码校验
export const CHECK_PASSWORD = "^[a-zA-Z0-9_]{6,16}$";
//验证码
export const CHECK_AUTH_CODE = "^[0-9]{10}$";

//用户名校验
export const CHECK_USERNAME = "^[a-zA-Z]{1}[a-zA-Z0-9]{2,9}$";

//应用的名称校验
export const CHECK_APP_NAME = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,63}$";

//环境名称校验
export const CHECK_ENV_NAME = "^[a-zA-Z]{1}[a-zA-Z0-9_]{1,63}$";

//配置项校验
//KEY,Value 校验
export const CHECK_KEY_VALUE = "^[a-zA-Z]{1}[a-zA-Z0-9_]{1,33}$";


