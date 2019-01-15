package com.asiainfo.configcenter.client.execute.strategy;


import com.asiainfo.configcenter.client.pojo.FieldStrategy;
import com.asiainfo.configcenter.client.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public class FieldStrategyOperation {
    private FieldStrategy fieldStrategy;
    private List<Object> executeResults;
    private Map<String,String> configs;

    public FieldStrategyOperation(FieldStrategy fieldStrategy,List<Object> executeResults,Map<String,String> configs) {
        this.fieldStrategy = fieldStrategy;
        this.executeResults = executeResults;
        this.configs = configs;
    }

    public Object execute() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> fieldClass = Class.forName(fieldStrategy.getClazz());
        Field field = fieldClass.getDeclaredField(fieldStrategy.getFieldName());
        field.setAccessible(true);
        String fieldValue = fieldStrategy.getFieldValue();
        Class dataClass = ReflectUtil.getKeyClass(fieldStrategy.getDataType());
        Object paramsValue = ReflectUtil.getParamsValue(fieldValue, dataClass, executeResults, configs);
        boolean isStatic = Modifier.isStatic(field.getModifiers());
        if (isStatic) {
            field.set(null,paramsValue);
        } else {
            String classInstance = fieldStrategy.getClassInstance();
            if (StringUtils.isNotBlank(classInstance) && "$".equals(classInstance.substring(0,1))) {
                String ref = classInstance.substring(1);
                Object refObject = executeResults.get(Integer.parseInt(ref) - 1);
                field.set(refObject,paramsValue);
            } else {
                throw new RuntimeException("获取类实例出错");
            }
        }
        return fieldValue;
    }
}
