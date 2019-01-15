package com.asiainfo.configcenter.client.execute;

import com.asiainfo.configcenter.client.execute.strategy.StrategyWrapper;
import com.asiainfo.configcenter.client.pojo.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteStrategy {

    /**
     * 按顺序执行刷新策略的每一步骤
     * @param strategies 刷新策略步骤集合
     * @param configs 配置项的map集合
     * @return 该刷新策略执行结果
     */
    public static boolean execute(List<Strategy> strategies,Map<String,String> configs) {
        System.out.println("开始执行刷新策略");
        try {
            List<Object> resultObjects = new ArrayList<Object>();
            for(Strategy strategy:strategies) {
                resultObjects.add(new StrategyWrapper(strategy,resultObjects,configs).executeStrategy());
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}