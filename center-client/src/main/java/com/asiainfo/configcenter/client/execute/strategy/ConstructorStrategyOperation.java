package com.asiainfo.configcenter.client.execute.strategy;

import com.asiainfo.configcenter.client.pojo.ConstructorStrategy;
import com.asiainfo.configcenter.client.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConstructorStrategyOperation {

    private ConstructorStrategy constructorStrategy;
    private List<Object> executeResults;
    private Map<String,String> configs;

    public ConstructorStrategyOperation(ConstructorStrategy constructorStrategy,List<Object> executeResults,Map<String,String> configs) {
        this.constructorStrategy = constructorStrategy;
        this.executeResults = executeResults;
        this.configs = configs;
    }

    public Object execute() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, ClassNotFoundException {
        Class<?> constructorClass = Class.forName(constructorStrategy.getClazz());
        List<Class<?>> types = getParamsType();
        Class<?>[] cla = new Class[types.size()];
        types.toArray(cla);
        Constructor<?> constructor = constructorClass.getDeclaredConstructor(cla);
        constructor.setAccessible(true);
        Object[] objects = getParamsValue(types);
        return constructor.newInstance(objects);
    }

    private List<Class<?>> getParamsType() {
        String paramsType = constructorStrategy.getParamsType();
        String[] paramsTypes = new String[0];
        if (StringUtils.isNotBlank(paramsType)) {
            paramsTypes = paramsType.split(",");
        }
        List<Class<?>> types = ReflectUtil.getParamsTypes(paramsTypes);
        return types;
    }

    private Object[] getParamsValue(List<Class<?>> types) {
        String paramsValue = constructorStrategy.getParamsValue();
        String[] paramsValues = new String[0];
        if(StringUtils.isNotBlank(paramsValue)) {
            paramsValues = paramsValue.split(",");
        }
        if (paramsValues.length != types.size()) {
            throw new RuntimeException("构造器的参数类型个数和对应的值个数不相等");
        }
        List<Object> values = new ArrayList<Object>();
        for (int i=0;i<paramsValues.length;i++) {
            values.add(ReflectUtil.getParamsValue(paramsValues[i],types.get(i),executeResults,configs));
        }
        Object[] objects = new Object[values.size()];
        values.toArray(objects);
        return objects;
    }
}
