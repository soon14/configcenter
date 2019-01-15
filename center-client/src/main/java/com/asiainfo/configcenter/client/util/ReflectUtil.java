package com.asiainfo.configcenter.client.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflectUtil {
    private static final Logger logger  = LoggerFactory.getLogger(ReflectUtil.class);
    /**
     * 将字符串表示的类型，转化为对应的Class对象
     * @param key 用字符串表示的类型
     * @return 数据类型的class类型
     */
    public static Class getItemClass(String key) {
        if("byte".equals(key)) {
            return byte.class;
        } else if("short".equals(key)) {
            return short.class;
        } else if("int".equals(key)) {
            return int.class;
        } else if("long".equals(key)) {
            return long.class;
        } else if("float".equals(key)) {
            return float.class;
        } else if("double".equals(key)) {
            return double.class;
        } else if("boolean".equals(key)) {
            return boolean.class;
        } else if("char".equals(key)) {
            return char.class;
        } else {
            Class c = null;
            try {
                c =  Class.forName(key);
            } catch (ClassNotFoundException e) {
                logger.error("类型输入有误",e);
                e.printStackTrace();
            }
            return c;
        }
    }

    /**
     * 将字符串表示的数组类型，转化为对应的Class对象
     * @param key 用字符串表示的数组类型
     * @return 数据类型的class类型
     * */
    public static Class getArrayClass(String key) {
        Class clazz = getItemClass(key);
        return Array.newInstance(clazz, 0).getClass();
    }

    /**
     * 统一处理单一对象的类型和数组类型
     * */
    public static Class getKeyClass(String key){
        if (StringUtils.isNotBlank(key)) {
            int index = key.indexOf("[]");
            if (index != -1) {
                String keySub = key.substring(0,index);
                return getArrayClass(keySub);
            } else {
                return getItemClass(key);
            }
        } else {
            throw new RuntimeException("类型不能为空");
        }
    }

    /**
     * 将字符串表示的数据类型转化为Class对象
     * @param paramsTypes 字符串数组表示的参数类型
     * @return 数据类型的class类型
     */
    public static List<Class<?>> getParamsTypes(String[] paramsTypes) {
        List<Class<?>> types= new ArrayList<Class<?>>(paramsTypes.length);
        for (String type:paramsTypes) {
            types.add(getKeyClass(type));
        }
        return types;
    }

    /**
     * 根据字符串的值得到相应的参数值
     * @param paramsValue 字符串表示的值
     * @param type 字符串值对应的数据类型
     * @param executeResults 执行结果保存容器
     * @param configs 服务端获取的刷新配置文件或者配置项
     * @return 对应数据类型的值
     */
    public static Object getParamsValue(String paramsValue,Class<?> type,List<Object> executeResults,Map<String,String> configs) {
        if("$".equals(paramsValue.substring(0,1))) {
            String ref = paramsValue.substring(1);
            if(ValidateUtil.check(ValidateUtil.CHECK_STR_DIGIT,ref)) {
                int refCount = Integer.parseInt(ref);
                return executeResults.get(refCount-1);
            } else {
               return configs.get(ref);
            }
        } if(paramsValue.endsWith("]")) {//数组类型的处理方式
            int index = paramsValue.indexOf("]");
            String str = paramsValue.substring(1,index);
//            return str.split(";");
            return dealArrayValues(str.split(";"), type, executeResults, configs);
        } else {
            String s = JSONUtil.obj2JsonStr(paramsValue);
            return JSONUtil.jsonStrToBean(s, type);
        }
    }

    /**
     * 获取数组类型的变量值
     * @param arrayValues 待处理的数值
     * @param type 字符串值对应的数据类型
     * @param executeResults 执行结果保存容器
     * @param configs 服务端获取的刷新配置文件或者配置项
     * @return 将字符串表示的值转化对应的数组值
     * */
    private static Object[] dealArrayValues(String[] arrayValues,Class<?> type,List<Object> executeResults,Map<String,String> configs) {
        Class<?> componentType = type.getComponentType();
        Object[] newInstance = (Object[]) Array.newInstance(componentType, arrayValues.length);
        for (int i=0;i<arrayValues.length;i++) {
            Object value = getParamsValue(arrayValues[i], componentType, executeResults, configs);
            newInstance[i] = value;
        }
        return newInstance;
    }
}
