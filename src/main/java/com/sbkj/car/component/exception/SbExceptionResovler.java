package com.sbkj.car.component.exception;

import com.sbkj.car.common.SbkjTool;
import com.sbkj.car.enums.StatusEnum;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @DESC: 公共异常处理
 * @Author: 臧东运
 * @CreateTime: 2019/4/15 13:13
 */
@Component
public class SbExceptionResovler implements HandlerExceptionResolver{
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器对象
     * @param e        异常对象
     * @return org.springframework.web.servlet.ModelAndView
     * @Description: 此方法就是处理异常，并前往错误页面
     * @Author Zangdy
     * @CreateTime 2019/4/15 13:27
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        logger.error("===========异常公共处理类:"+e.getMessage());
        SbException commonException = null;
        // 判断是否为自定义异常类型
        if (e instanceof SbException) {
            commonException = (SbException) e;
        } else {
            commonException = new SbException("服务器响应异常,请稍后重试!");
        }
        try {
            //响应给页面
            StatusEnum.CUSTOM.setMessage(commonException.getMessage());
            SbkjTool.responseWriter(StatusEnum.CUSTOM, response);
        } catch (IOException e1) {
            logger.error("=========== 异常处理器响应失败,失败原因:" + e1.getMessage());
        }
        return null;
    }
}
