package com.sbkj.car.component.conversion;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串转日期
 * @Author: 臧东运
 * @CreateTime: 2019/4/9 4:12
 */
@Component
public class StringToDate implements Converter<String,Date> {
    private Logger logger = Logger.getLogger(this.getClass());
    /**格式1**/
    private String pattern1 = "yyyy-MM-dd HH:mm:ss";
    /**格式2**/
    private String pattern2 = "yyyy/MM/dd HH:mm:ss";

    @Override
    public Date convert(String dateStr) {
        // 参数为空或空白符
        if(StringUtils.isBlank(dateStr)){
            throw new NullPointerException("没有要转换的日期数据!");
        }
        try{
            // 匹配第一种格式
            SimpleDateFormat sdfP1 = new SimpleDateFormat(pattern1);
            return sdfP1.parse(dateStr);
        }catch (Exception e) {
            // 匹配第二种格式
            SimpleDateFormat sdfP2 = new SimpleDateFormat(pattern2);
            try {
                return sdfP2.parse(dateStr);
            } catch (ParseException e1) {
                logger.error(this.getClass().getName()+":"+e.getMessage());
                throw new RuntimeException("时间转化时出现异常!");
            }
        }
    }
}
