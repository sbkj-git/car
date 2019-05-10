package com.sbkj.car.component.filter;

import com.sbkj.car.common.SbkjTool;
import com.sbkj.car.common.JwtTool;
import com.sbkj.car.common.SecuritySHATool;
import com.sbkj.car.enums.StatusEnum;
import com.sbkj.car.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: token/sign认证过滤器
 * @Author: 臧东运
 * @CreateTime: 2019/4/17 16:39
 */
@Component
public class TokenAndSignFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${sign.appKey}")
    private String appKeyLocal;

    @Value("${sign.securityKey}")
    private String securityKeyLocal;

    private static List<String> urlPassList = new ArrayList<>();
    private static List<String> noauthUrlList = new ArrayList<>();
    /**ip访问次数集合**/
    private Map<String, List<Long>> ipCountMap = new HashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /** 遵循阿里规范,尽量不出现任何魔法值(未定义的变量) 将变量提前声明 **/
    /** 认证权限时获取权限地址时使用**/
    private String urlLastIndex = "/";
     /** 限制ip多长时间内**/
    private int ipTime = 1000;
    /** 限制ip一定时间内的访问次数**/
    private int ipCount = 10;
     /** 封锁ip十分钟**/
    private int ipBlockedTime = 1000*60*10;
     /** 签名有效时间 **/
    private int signTime = 1000*60*5*1000;

    static {
        urlPassList.add("/favicon.ico");
        urlPassList.add("/js/");
        urlPassList.add("/css/");
        urlPassList.add("/img/");
        urlPassList.add("/html/");
        urlPassList.add("/uploads/");

        noauthUrlList.add("/noauth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
            String ip = SbkjTool.getIpAddress(httpServletRequest);
        try {
            String url = httpServletRequest.getRequestURI();
            String username = null;
            String uuid = "uuid";

            // 放行静态资源
            for (String urlPass : urlPassList) {
                if (url.contains(urlPass)) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }
            // 限制一段时间内的ip访问次数 1秒10次
            if (!restrictIp(httpServletRequest, httpServletResponse)) {
                return;
            }

            // 放行不需要验证token的url
            for (String noauthUrl : noauthUrlList) {
                if (url.contains(noauthUrl)) {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }

            // 获取header中的token信息:
            String authHeader = httpServletRequest.getHeader(this.tokenHeader);
            // 是否含有token的头信息:
            if (authHeader == null || !authHeader.startsWith(this.tokenHead)) {
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.NOHEADER);
                // 缺少头信息:
                SbkjTool.responseWriter(StatusEnum.NOHEADER, httpServletResponse);
                return;
            }
            // "Bearer "
            final String authToken = authHeader.substring(tokenHead.length());
            // 获取出token中的用户名
            String userAccount = jwtTool.getuserAccountFromToken(authToken);
            // 取出redis中的在线用户名集合
            BoundSetOperations usernameSet = redisTemplate.boundSetOps("usernameSet");
            logger.info("token验证过滤器[JwtAuthenticationTokenFilter]_用户名为:" + userAccount);
            // 是否登录过
            if (!usernameSet.isMember(userAccount)) {
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.UNLOGIN);
                SbkjTool.responseWriter(StatusEnum.UNLOGIN, httpServletResponse);
                return;
            }
            username = userAccount;
            // 验证过期时间
            if (jwtTool.isTokenExpired(authToken)) {
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.TOKENOUTTIME);
                SbkjTool.responseWriter(StatusEnum.TOKENOUTTIME, httpServletResponse);
                return;
            }
            // 验证权限
            if (!verifyRights(username, url.substring(url.lastIndexOf(urlLastIndex)))) {
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.ACCESSDENIED);
                SbkjTool.responseWriter(StatusEnum.ACCESSDENIED, httpServletResponse);
                return;
            }

            // 验证uuid是否存在
            if(StringUtils.isBlank(httpServletRequest.getParameter(uuid))){
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.UUIDISNULL);
                SbkjTool.responseWriter(StatusEnum.UUIDISNULL, httpServletResponse);
                return;
            }
            // 验证签名
            if (!verifySign(httpServletRequest, httpServletResponse)) {
                return;
            }
        } catch (IOException e) {
            logger.error("ip:"+ip+"=====信息:"+e.getMessage());
            // 服务器异常
            throw new RuntimeException(StatusEnum.ERROR.getMessage());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * @param httpServletRequest
     * @return void
     * @Description: 限制一段时间内ip访问次数  1秒10次
     * @Author Zangdy
     * @CreateTime 2019/4/22 13:10
     */
    private boolean restrictIp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        // 获取ip
        String ip = SbkjTool.getIpAddress(httpServletRequest);
        // 查看ip是否被封
        BoundHashOperations ipBlockedMap = redisTemplate.boundHashOps("ipBlockedMap");
        if (ipBlockedMap.hasKey(ip)) {
            // 解封的毫秒值
            Long timeLong = (Long) ipBlockedMap.get(ip);
            // 当前的毫秒值
            long time = System.currentTimeMillis();
            // 未解封
            if (time < timeLong) {
                SbkjTool.responseWriter(StatusEnum.IPBLOCKED, httpServletResponse);
                return false;
            }
            // 解封后移除
            ipBlockedMap.delete(ip);
            logger.info(ip + "已解除被封状态");
        }
        List<Long> timeList = ipCountMap.get(ip);


        long time = System.currentTimeMillis();
        if (timeList != null && timeList.size() > 0) {
            Iterator<Long> timeIterator = timeList.iterator();
            while (timeIterator.hasNext()) {
                Long nextTime = timeIterator.next();
                // 不在一秒钟内
                if (time > nextTime + ipTime) {
                    timeIterator.remove();
                }
            }
            timeList.add(time);
            if (timeList.size() > ipCount) {
                // 将ip封10分钟
                ipBlockedMap.put(ip, time + ipBlockedTime);
                logger.info(ip + "已被封10分钟");
                SbkjTool.responseWriter(StatusEnum.IPBLOCKED, httpServletResponse);
                return false;
            }
        } else {
            // 为空,创建
            if (timeList == null) {
                synchronized (this.getClass()) {
                    if (timeList == null) {
                        timeList = new ArrayList<>();
                    }
                }
            }
            timeList.add(Long.valueOf(System.currentTimeMillis()));
            ipCountMap.put(ip, timeList);
        }
        return true;
    }


    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @return void
     * @Description: 验证sign签名
     * @Author Zangdy
     * @CreateTime 2019/4/22 12:57
     */
    private boolean verifySign(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String ip = SbkjTool.getIpAddress(httpServletRequest);
        // 签名
        String appKey = httpServletRequest.getParameter("appKey");
        String timeStamp = httpServletRequest.getParameter("timeStamp");
        String sign = httpServletRequest.getParameter("sign");
        // 判断参数是否为空
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(timeStamp) || StringUtils.isBlank(sign)) {
            logger.error("ip:"+ip+"=====信息:"+StatusEnum.APPTIMESIGNISNULL);
            SbkjTool.responseWriter(StatusEnum.APPTIMESIGNISNULL, httpServletResponse);
            return false;
        } else {
            StringBuilder signSb = new StringBuilder();
            // 获取url里的请求参数
            Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
            for (String paraName : parameterMap.keySet()) {
                if (!"sign".equals(paraName)&&!"uuid".equals(paraName)) {
                    signSb.append(paraName).append("=").append(parameterMap.get(paraName)[0]).append("&");
                }
            }
            // 判断请求方式是否为post  此处以流的方式读取,但是读取完毕之后controller里的参数就为空了
//            if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
//                InputStream is = httpServletRequest.getInputStream();  // 获取请求体的流
//                if (is != null) {
//                    byte[] bodyBytes = SbkjTool.readBytes(is, httpServletRequest.getContentLength());// 转成字节数组
//                    is.close(); // 关闭流
//                    String bodyStr = new String(bodyBytes);  // 转成字符串
//                    JSONObject bodyJObject = JSONObject.parseObject(bodyStr); // 将字符串转成json对象
//                    for (String paraName : bodyJObject.keySet()) {
////                        signSb.append(paraName).append("=").append(bodyJObject.get(paraName)).append("&");
//                        logger.info(paraName+"==="+bodyJObject.getString(paraName));
//                    }
//                }
//            }
            // 获取拼接后的签名
            String signNew = signSb.substring(0, signSb.length() - 1);
            String[] signArr = signNew.split("&");
            // 将参数排序
            Arrays.sort(signArr);
            // 数组转字符串
            signNew = SbkjTool.arrToString(signArr);
            signNew = signNew.replace("=", "");
            // 拼接securityKey
            signNew += securityKeyLocal;
            if (!sign.equals(SecuritySHATool.shaEncrypt(signNew))) {
                logger.error("ip:"+ip+"=====信息:"+StatusEnum.SIGNUNTRUE);
                SbkjTool.responseWriter(StatusEnum.SIGNUNTRUE, httpServletResponse);
                return false;
            }
            // 获取当前时间毫秒值
            long time = System.currentTimeMillis();
            try {
                // 一次请求签名在五分钟内
                if ((time - SbkjTool.timeStampToDateTime(timeStamp)) > signTime) {
                    logger.error("ip:"+ip+"=====信息:"+StatusEnum.SIGNOUTTIME);
                    SbkjTool.responseWriter(StatusEnum.SIGNOUTTIME, httpServletResponse);
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * @param username
     * @return boolean
     * @Description: 权限验证
     * @Author Zangdy
     * @CreateTime 2019/4/26 16:59
     */
    private boolean verifyRights(String username, String url) {
        return userService.verifyRightsByUsername(username, url);
    }

}
