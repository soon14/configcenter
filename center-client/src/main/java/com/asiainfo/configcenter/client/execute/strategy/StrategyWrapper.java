package com.asiainfo.configcenter.client.execute.strategy;

import com.asiainfo.configcenter.client.pojo.ConstructorStrategy;
import com.asiainfo.configcenter.client.pojo.FieldStrategy;
import com.asiainfo.configcenter.client.pojo.MethodStrategy;
import com.asiainfo.configcenter.client.pojo.Strategy;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class StrategyWrapper{
    private Strategy strategy;
    private List executeResults;
    private Map<String,String> configs;

    public StrategyWrapper(Strategy strategy,List executeResults,Map<String,String> configs) {
        this.strategy = strategy;
        this.executeResults = executeResults;
        this.configs = configs;
    }

    public Object executeStrategy() throws InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        if (strategy instanceof ConstructorStrategy) {
            System.out.println("开始执行构造器刷新策略");
            ConstructorStrategy constructorStrategy = (ConstructorStrategy)strategy;
            ConstructorStrategyOperation operation = new ConstructorStrategyOperation(constructorStrategy,executeResults,configs);
            return operation.execute();
        } else if(strategy instanceof FieldStrategy) {
            System.out.println("开始执行属性的刷新策略");
            FieldStrategy fieldStrategy = (FieldStrategy)strategy;
            FieldStrategyOperation operation = new FieldStrategyOperation(fieldStrategy,executeResults,configs);
            return operation.execute();
        } else if(strategy instanceof MethodStrategy) {
            System.out.println("开始执行方法的刷新策略");
            MethodStrategy methodStrategy = (MethodStrategy) strategy;
            MethodStrategyOperation operation = new MethodStrategyOperation(methodStrategy,executeResults,configs);
            return operation.execute();
        }
        return null;
    }
}
