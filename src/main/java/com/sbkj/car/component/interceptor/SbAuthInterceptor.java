package com.sbkj.car.component.interceptor;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 拦截器
 * @Author: 臧东运
 * @CreateTime: 2019/4/15 14:45
 */
@Component
public class SbAuthInterceptor implements HandlerInterceptor {
    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute("zzz", "zzzz");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址         
//        corsConfiguration.addAllowedHeader("token"); // 2 设置访问源请求头   
//        corsConfiguration.addAllowedMethod("POST,GET"); // 3 设置访问源请求方法         
//        corsConfiguration.setMaxAge(1800000L); //间隔30分钟验证一次是否允许跨域
    }
}
