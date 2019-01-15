/*
 *   Copyright 1999-2016 Asiainfo Technologies(China),Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.asiainfo.configcenter.center.config;

import com.asiainfo.configcenter.center.interceptor.RequestInterceptor;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

import static com.asiainfo.configcenter.center.common.ProjectConstants.PROJECT_USE_MODE_TEST;

/**
 * 统一配置类
 * Created by bawy on 18/7/3.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Resource
    private RequestInterceptor requestInterceptor;

    @Value("${project.use.mode}")
    private String mode;

    @Value("${web.server.url}")
    private String webServerUrl;

    /**
     * 增加拦截器
     * @author bawy
     * @date 2018/7/3 11:29
     * @param registry 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!PROJECT_USE_MODE_TEST.equals(mode)) {
            registry.addInterceptor(requestInterceptor).
                    addPathPatterns("/center/**").excludePathPatterns("/center/client/**");
        }
    }

    /**
     * 跨域配置
     * @author bawy
     * @date 2018/7/3 11:30
     * @param registry 跨域配置注册
     */
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*//**")
                .allowedOrigins(webServerUrl)
                .allowedMethods(ALL)
                .allowedHeaders(ALL)
                .allowCredentials(true);
    }*/

    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

}