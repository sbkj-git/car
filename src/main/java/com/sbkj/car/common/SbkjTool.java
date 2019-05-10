package com.sbkj.car.common;

import com.alibaba.fastjson.JSONObject;
import com.sbkj.car.enums.StatusEnum;
import com.sbkj.car.model.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Description: 通用工具类
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 9:51
 */
public class SbkjTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(SbkjTool.class);

    /**
     * @param is         输入流
     * @param contentLen 流的长度
     * @return byte[]
     * @Description: 将输入流转成字节数组
     * @Author Zangdy
     * @CreateTime 2019/4/22 9:52
     */
    public static byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return new byte[]{};
    }

    /**
     * @param objects
     * @return java.lang.String
     * @Description: 数组转字符串
     * @Author Zangdy
     * @CreateTime 2019/4/22 10:18
     */
    public static String arrToString(Object[] objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append(object);
        }
        return sb.toString();
    }


    /**
     * @param timeStamp
     * @return java.lang.Long
     * @Description: 将时间戳转为毫秒值   yyyyMMddHHmmss
     * @Author Zangdy
     * @CreateTime 2019/4/22 11:42
     */
    public static Long timeStampToDateTime(String timeStamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.parse(timeStamp).getTime();
    }



    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return ip字符串
     */
    public static String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * @param statusEnum
     * @param response
     * @return java.lang.String
     * @Description: 发生错误时响应给请求者, 拦截器过滤器层面,需要HttpServletResponse
     * @Author Zangdy
     * @CreateTime 2019/4/22 11:09
     */
    public static void responseWriter(StatusEnum statusEnum, HttpServletResponse response) throws IOException {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(statusEnum.getStatus());
        responseBody.setMessage(statusEnum.getMessage());
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(responseBody));
    }

    /**
     * @param statusEnum
     * @return java.lang.String
     * @Description: 发生错误时响应给请求者, Controller层面,不需要HttpServletResponse
     * @Author Zangdy
     * @CreateTime 2019/4/22 11:09
     */
    public static <T>ResponseBody responseReturn(StatusEnum statusEnum,T result){
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(statusEnum.getStatus());
        responseBody.setMessage(statusEnum.getMessage());
        responseBody.setResult(result);
        return responseBody;
    }


}
