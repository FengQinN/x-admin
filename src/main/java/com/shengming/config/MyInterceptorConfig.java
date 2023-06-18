package com.shengming.config;

import com.shengming.common.interceptor.JwtValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    JwtValidateInterceptor jwtValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*注册拦截器*/
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(jwtValidateInterceptor);
        /*拦截器申请拦截的请求*/
        interceptorRegistration.addPathPatterns("/**").
                excludePathPatterns(
                        "/user/login",
                        "/user/info",
                        "/user/logout",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/**");
    }
}
