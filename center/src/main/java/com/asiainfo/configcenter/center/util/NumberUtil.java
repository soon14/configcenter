package com.asiainfo.configcenter.center.util;

import java.util.Random;

/**
 * 数字工具类
 * Created by bawy on 2018/7/19 22:44.
 */
public class NumberUtil {

    /**
     * 生成指定长度的随机字符串
     * @author bawy
     * @date 2017/8/30 15:40
     * @param len 长度
     * @return 随机字符串
     */
    public static String getRandomStr(int len){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomStr(10));
    }


}
