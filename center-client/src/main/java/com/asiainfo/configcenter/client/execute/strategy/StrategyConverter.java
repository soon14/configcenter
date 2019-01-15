package com.asiainfo.configcenter.client.execute.strategy;

import com.asiainfo.configcenter.client.pojo.*;
import com.asiainfo.configcenter.client.vo.UpdateStrategyStepVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略转换器
 * Created by bawy on 2018/9/11 15:18.
 */
public class StrategyConverter {

    public static List<Strategy> transferServerBeanToClientBean(List<UpdateStrategyStepVO> strategyStepVOS) {
        List<Strategy> strategies = new ArrayList<Strategy>();
        for(UpdateStrategyStepVO updateStrategyStepVO:strategyStepVOS) {
            if("C".equals(updateStrategyStepVO.getStrategyType())) {
                Strategy constructorStrategy = transferStepConstructorToConstructorStrategy(updateStrategyStepVO.getStepNum(),updateStrategyStepVO.getCcStrategyStepConstructorEntity());
                strategies.add(constructorStrategy);
            } else if("F".equals(updateStrategyStepVO.getStrategyType())) {
                Strategy fieldStrategy = transferStepFieldToFieldStrategy(updateStrategyStepVO.getStepNum(),updateStrategyStepVO.getCcStrategyStepFieldEntity());
                strategies.add(fieldStrategy);
            } else if("M".equals(updateStrategyStepVO.getStrategyType())) {
                Strategy methodStrategy = transferStepMethodToMethodStrategy(updateStrategyStepVO.getStepNum(),updateStrategyStepVO.getCcStrategyStepMethodEntity());
                strategies.add(methodStrategy);
            }
        }
        return strategies;
    }

    private static Strategy transferStepConstructorToConstructorStrategy(byte stepNum, CcStrategyStepConstructorEntity stepConstructor) {
        ConstructorStrategy constructorStrategy = new ConstructorStrategy();
        constructorStrategy.setStepNum(stepNum);
        constructorStrategy.setClazz(stepConstructor.getClazz());
        constructorStrategy.setParamsType(stepConstructor.getParamsType());
        constructorStrategy.setParamsValue(stepConstructor.getParamsValue());
        return constructorStrategy;
    }

    private static Strategy transferStepFieldToFieldStrategy(byte stepNum, CcStrategyStepFieldEntity stepField) {
        FieldStrategy fieldStrategy = new FieldStrategy();
        fieldStrategy.setStepNum(stepNum);
        fieldStrategy.setClazz(stepField.getClazz());
        fieldStrategy.setClassInstance(stepField.getClassInstance());
        fieldStrategy.setDataType(stepField.getDataType());
        fieldStrategy.setFieldName(stepField.getFieldName());
        fieldStrategy.setFieldValue(stepField.getFieldValue());
        return fieldStrategy;
    }

    private static Strategy transferStepMethodToMethodStrategy(byte stepNum, CcStrategyStepMethodEntity stepMethod) {
        MethodStrategy methodStrategy = new MethodStrategy();
        methodStrategy.setStepNum(stepNum);
        methodStrategy.setClazz(stepMethod.getClazz());
        methodStrategy.setClassInstance(stepMethod.getClassInstance());
        methodStrategy.setMethodName(stepMethod.getMethodName());
        methodStrategy.setParamsType(stepMethod.getParamsType());
        methodStrategy.setParamsValue(stepMethod.getParamsValue());
        return methodStrategy;
    }

}
