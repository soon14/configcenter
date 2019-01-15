package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.PageRequestContainer;

import java.util.regex.Pattern;

/**
 * 数据校验工具类
 * Created by bawy on 2018/7/17 14:27.
 */
public class ValidateUtil {

	//用户名校验，3-10个字符，只能由英文字母和数字组成,首字母不能为数字
	public static final String CHECK_USERNAME = "^[a-zA-Z]{1}[a-zA-Z0-9]{2,9}$";
	//密码校验，6-16个字符，只能由英文字母和数字加下划线组成
	public static final String CHECK_PASSWORD = "^[a-zA-Z0-9_]{6,16}$";
	//校验app名称，5-64个字符，只能由英文字母和数字加下划线组成，首字符只能为字母
	public static final String CHECK_APP_NAME = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,63}$";
	//校验环境名称,2-64个字符，只能由英文字母和数字加下划线组成，首字符只能为字母
	private static final String CHECK_ENV_NAME = "^[a-zA-Z]{1}[a-zA-Z0-9_]{1,63}$";
	//校验配置项key,2-50个字符，只能由英文字母和数字加下划线组成，首字符只能为字母
	public static final String CHECK_ITEM_KEY = "^[a-zA-Z]{1}[a-zA-Z0-9_]{1,49}$";
	//手机校验
	private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	//邮箱校验
	private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	//校验文件路径
	private static final String FILE_PATH = "/^([A-Za-z]{1}:\\/[\\w\\/]*)?\\w+\\.{1}[a-zA-Z]+$/";
	//校验类路径
	private static final String CLZ_PATH = "[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*\\.[a-zA-Z]+[0-9a-zA-Z_]*(\\$[a-zA-Z]+[0-9a-zA-Z_]*)*";
	//校验类名

	/**
	 * 校验字符串是否符合正则表达式
	 * @param reg 正则表达式
	 * @param str 被校验字符串
	 * @return 校验通过返回true
	 */
	public static boolean check(String reg, String str){
		return Pattern.matches(reg, str);
	}

	/**
	 * 检查是否是邮箱
	 * @param mail 邮箱
	 * @return true:是 false:不是
	 * @author oulc
	 * @date 2018/7/17 14:29
	 */
	public static boolean checkMail(String mail){
		return Pattern.matches(REGEX_EMAIL,mail);
	}

	/**
	 * 检查是否是手机号码
	 * @param phone 手机号码
	 * @return true:是 false:不是
	 * @author oulc
	 * @date 2018/7/17 14:38
	 */
	public static boolean checkPhone(String phone){
		return Pattern.matches(REGEX_MOBILE,phone);
	}

	/**
	 * 校验分页参数
	 * @param pageRequestContainer 分页参数
	 */
	public static void checkPageParam(PageRequestContainer pageRequestContainer){
		//校验当前页
		Assert4CC.isTrue(pageRequestContainer.getCurrentPage() > -1,"currentPage："+pageRequestContainer.getCurrentPage()+",不合法,必须要大于等于0");

		//校验页大小
		Assert4CC.isTrue(pageRequestContainer.getPageSize() > 0 ,"pageSize："+pageRequestContainer.getPageSize()+",不合法，必须要大于0");

		Assert4CC.notNull(pageRequestContainer.getData() ,"data参数不能为空");
	}

	public static boolean checkAppEnvName(String envName){
		return Pattern.matches(CHECK_ENV_NAME,envName);
	}

	public static boolean checkFilePath(String filePath){
		return Pattern.matches(FILE_PATH,filePath);
	}

	public static boolean checkClzPath(String clzPath){
		return Pattern.matches(CLZ_PATH,clzPath);
	}

}
