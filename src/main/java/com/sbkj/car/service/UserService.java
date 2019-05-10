package com.sbkj.car.service;

import com.sbkj.car.model.RightsPo;
import com.sbkj.car.model.UserPo;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 16:32
 */
public interface UserService {
    /**
     * 根据用户名查询user
     * @param username
     * @return
     */
    UserPo findUserByName(String username);

    /**
     * 验证该用户有没有访问该路径的权限
     * @param username
     * @param url
     * @return
     */
    boolean verifyRightsByUsername(String username,String url);
}
