package com.asiainfo.configcenter.client.execute.strategy;


import com.asiainfo.configcenter.client.pojo.MethodStrategy;
import com.asiainfo.configcenter.client.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MethodStrategyOperation {
    private MethodStrategy methodStrategy;
    private List<Object> executeResults;
    private Map<String,String> configs;

    public MethodStrategyOperation(MethodStrategy methodStrategy,List<Object> executeResults,Map<String,String> configs) {
        this.methodStrategy = methodStrategy;
        this.executeResults = executeResults;
        this.configs = configs;
    }

    public Object execute() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> methodClass =  Class.forName(methodStrategy.getClazz());
        String methodName = methodStrategy.getMethodName();
        List<Class<?>> types = getParamsType();
        Class<?>[] cla = new Class[types.size()];
        types.toArray(cla);
        Method method = this.getFinalMethod(methodClass, methodName, cla);
        if (method == null) {
            throw new RuntimeException("获取指定的方法失败");
        }
        method.setAccessible(true);
        Object[] values = getParamsValue(types);
        Object invokeResult;
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        if (isStatic) {
            invokeResult = method.invoke(null, values);
        } else {
            String classInstance = methodStrategy.getClassInstance();
            if (StringUtils.isNotBlank(classInstance) && "$".equals(classInstance.substring(0,1))) {
                String ref = classInstance.substring(1);
                Object refObject = executeResults.get(Integer.parseInt(ref) - 1);
                invokeResult = method.invoke(refObject,values);
            } else {
                throw new RuntimeException("获取类实例出错");
            }
        }
        return invokeResult;
    }

    private List<Class<?>> getParamsType() {
        String paramsType = methodStrategy.getParamsType();
        String[] paramsTypes = new String[0];
        if (StringUtils.isNotBlank(paramsType)) {
            paramsTypes = paramsType.split(",");
        }
        return ReflectUtil.getParamsTypes(paramsTypes);
    }

    private Object[] getParamsValue(List<Class<?>> types) {
        String paramsValue = methodStrategy.getParamsValue();
        String[] paramsValues = new String[0];
        if(StringUtils.isNotBlank(paramsValue)) {
            paramsValues = paramsValue.split(",");
        }
        if (paramsValues.length != types.size()) {
            throw new RuntimeException("方法的参数类型个数和对应的值个数不相等");
        }
        List<Object> values = new ArrayList<Object>();
        for (int i=0;i<paramsValues.length;i++) {
            values.add(ReflectUtil.getParamsValue(paramsValues[i],types.get(i),executeResults,configs));
        }
        Object[] objects = new Object[values.size()];
        values.toArray(objects);
        return objects;
    }

    private Method getFinalMethod(Class<?> clazz,String methodName,Class<?>[] cla) {
        Method finalMethod = null;
        Class<?> aClass = clazz;
        while (aClass != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method:methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                boolean isEquals = Arrays.equals(parameterTypes, cla);
                if (methodName.equals(method.getName()) && isEquals) {
                    finalMethod = method;
                    break;
                }
            }
            aClass = aClass.getSuperclass();
        }
        return finalMethod;
    }

}
