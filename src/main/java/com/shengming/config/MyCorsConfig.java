package com.shengming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
/*处理跨域请求*/
public class MyCorsConfig {

    //创建Cors
    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1) 允许的域,不要写*，否则cookie就无法使用了
        corsConfiguration.addAllowedOrigin("http://localhost:9528");
        //2) 是否发送Cookie信息
        corsConfiguration.setAllowCredentials(true);
        //3) 允许的请求方式
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("HEAD");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PATCH");
        // 4）允许的头信息
        corsConfiguration.addAllowedHeader("*");
        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
