package com.asiainfo.configcenter.center.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by oulc on 2018/7/25.
 */
public class InvokeUtil {
    public static Object invokePrivateFunction(Class clz,Object instance, String methodName, Class[] paramsType,Object ... data)throws Throwable{
        try {
            Method method = clz.getDeclaredMethod(methodName,paramsType);
            return method.invoke(instance,data);
        }catch (Exception e){
            throw e.getCause();
        }
    }
}
