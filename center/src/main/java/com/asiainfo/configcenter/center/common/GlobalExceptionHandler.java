package com.asiainfo.configcenter.center.common;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 控制层异常统一处理类
 * Created by bawy on 18/7/3.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = Logger.getLogger(GlobalExceptionHandler.class);


    /**
     * 校验异常统一处理
     * @param e 校验异常
     * @return Result对象
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleValidationException(ConstraintViolationException e){
        logger.error(e.getCause().getMessage());
        Result result  = new Result();
        result.setErrorCode(ResultCodeEnum.PARAM_ERROR.getErrorCode());
        for(ConstraintViolation<?> s:e.getConstraintViolations()){
            result.setErrorMsg( s.getInvalidValue()+": "+s.getMessage());
        }
        return result;
    }

    /**
     * 自定义异常统一处理
     * @param e 自定义异常
     * @return Result对象
     */
    @ExceptionHandler(value = ErrorCodeException.class)
    @ResponseBody
    public Result dealErrorCodeException(ErrorCodeException e) throws Exception{
        logger.error(ErrorInfo.errorInfo(e));
        Result result = new Result();
        result.setSuccess(false);
        result.setErrorCode(e.getCode());
        result.setErrorMsg(e.getMessage());
        return result;
    }

    /**
     * 其他异常统一处理
     * @param e 其他异常
     * @return Result对象
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result dealException(Exception e) throws Exception{
        logger.error(ErrorInfo.errorInfo(e));
        return ErrorInfo.handError(e);
    }

}
