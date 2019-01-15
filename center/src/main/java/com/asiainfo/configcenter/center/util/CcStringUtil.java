package com.asiainfo.configcenter.center.util;

import org.apache.commons.lang3.StringUtils;

public class CcStringUtil {
    /**
     * 字符串根据逗号分割，然后转成int数组
     * @param str 字符串
     * @return 整型数组
     */
    public static int[] splitByCommaAndConvertToInt(String str){
        if (StringUtils.isBlank(str)){
            return new int[0];
        }
        String[] strArray = str.split(",");
        int [] result = new int[strArray.length];
        for( int i = 0 ;i < strArray.length ;i ++){
            result[i] = Integer.parseInt(strArray[i]);
        }
        return result;
    }

    /**
     * 把数组拼成根据逗号分割的字符串
     * @author oulc
     * @date 18-8-14 下午5:40
     * @param datas 数组
     * @return 字符串
     */
    public static String convertIntArrayToString(int[] datas){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<datas.length; i++){
            if (i>0){
                sb.append(",");
            }
            sb.append(datas[i]);
        }
        return sb.toString();
    }
}
