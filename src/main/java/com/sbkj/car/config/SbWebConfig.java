package com.sbkj.car.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sbkj.car.component.interceptor.SbAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 拦截器的配置类
 * @Author: 臧东运
 * @CreateTime: 2019/4/16 16:04
 */
@Configuration
public class SbWebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private SbAuthInterceptor sbAuthInterceptor;

    /**
     * @param interceptorRegistry
     * @return void
     * @Description: 拦截器配置
     * @Author Zangdy
     * @CreateTime 2019/4/17 14:40
     */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(sbAuthInterceptor).addPathPatterns("/**");
    }
}
