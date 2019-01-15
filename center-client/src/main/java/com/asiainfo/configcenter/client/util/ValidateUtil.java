package com.asiainfo.configcenter.client.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    //用户名校验，3-10个字符，只能由英文字母和数字组成,首字母不能为数字Digi
    public static final String CHECK_STR_DIGIT = "[0-9]*";

    /**
     * 校验字符串是否符合正则表达式
     * @param reg 正则表达式
     * @param str 被校验字符串
     * @return 校验通过返回true
     */
    //[0-9]*
    public static boolean check(String reg, String str){
        return Pattern.matches(reg, str);
    }
}
