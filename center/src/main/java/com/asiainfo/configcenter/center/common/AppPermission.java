package com.asiainfo.configcenter.center.common;

import java.lang.annotation.*;

/**
 * 应用权限拦截注解
 * Created by bawy on 2018/7/24 17:26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AppPermission {

    String description() default "";

}
