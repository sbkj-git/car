package com.sbkj.car.controller.auth;

import com.sbkj.car.common.SbkjTool;
import com.sbkj.car.enums.StatusEnum;
import com.sbkj.car.model.ResponseBody;
import com.sbkj.car.model.UserPo;
import com.sbkj.car.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/29 17:21
 */
@RestController
@RequestMapping("/auth")
public class UserController {
    private Logger logger = Logger.getLogger(this.getClass());

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
     * @Description: 注销
     * @Author Zangdy
     * @CreateTime 2019/4/22 17:40
     */
    @RequestMapping("/logOut")
    public ResponseBody logOut(UserPo userPo, HttpServletResponse response) {

        BoundSetOperations userSet = redisTemplate.boundSetOps("usernameSet");
        // 移除注销用户
        userSet.remove(userPo.getUsername());
        // 清空token
        response.setHeader(header,"你猜我猜不猜");
        // 注销成功
        return SbkjTool.responseReturn(StatusEnum.LOGOUTSUCCESS,null);
    }


}
