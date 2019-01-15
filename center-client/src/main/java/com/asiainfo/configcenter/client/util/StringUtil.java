package com.asiainfo.configcenter.client.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 字符串处理工具类
 * Created by bawy on 2018/9/10 15:17.
 */
public class StringUtil {

    /**
     * 打印异常栈
     * @author bawy
     * @date 2018/5/15 10:45
     * @param t 异常
     * @return 返回异常栈对应的字符串
     */
    public static String printStackTraceToString(Throwable t) {
        try {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw, true));
            return sw.getBuffer().toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }
}
