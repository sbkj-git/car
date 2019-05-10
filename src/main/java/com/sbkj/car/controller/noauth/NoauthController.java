package com.sbkj.car.controller.noauth;

import com.sbkj.car.common.SbkjTool;
import com.sbkj.car.common.JwtTool;
import com.sbkj.car.common.SecuritySHATool;
import com.sbkj.car.model.ResponseBody;
import com.sbkj.car.model.UserPo;
import com.sbkj.car.service.UserService;
import com.sbkj.car.enums.StatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 不需要token认证的方法都写在这里面
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 15:11
 * CrossOrigin // 设置跨域
 */
@RestController
@RequestMapping("/noauth")
public class NoauthController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    /**
     * @param userPo
     * @return com.sbkj.car.model.ResponseBody
     * @Description: 登录
     * @Author Zangdy
     * @CreateTime 2019/4/22 17:40
     */
    @RequestMapping("/login")
    public ResponseBody login(UserPo userPo, HttpServletResponse response) {
        ResponseBody responseBody = new ResponseBody();

        String username = userPo.getUsername();
        String password = userPo.getPassword();
        // 用户名为空
        if (StringUtils.isBlank(username)) {
            return SbkjTool.responseReturn(StatusEnum.USERNAMEISNULL, null);
        }
        // 密码为空
        if (StringUtils.isBlank(password)) {
            return SbkjTool.responseReturn(StatusEnum.PASSWORDISNULL, null);
        }

        userPo = userService.findUserByName(username);

        // 根据登录名来查询用户信息
        if (userPo == null) {
            return SbkjTool.responseReturn(StatusEnum.USERNAMEERROR, null);
        }

        // 密码错误(MD5加密)
        if (!userPo.getPassword().equals(SecuritySHATool.md5Encrypt(password))) {
            return SbkjTool.responseReturn(StatusEnum.PASSWORDERROR, null);
        }

        response.setHeader(header, tokenHead + jwtTool.generateToken(username));
        BoundSetOperations userSet = redisTemplate.boundSetOps("usernameSet");
        userSet.add(username);
        // 登录成功
        return SbkjTool.responseReturn(StatusEnum.LOGINSUCCESS, null);
    }

}
