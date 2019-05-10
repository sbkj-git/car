package com.sbkj.car.service.impl;

import com.sbkj.car.mapper.UserMapper;
import com.sbkj.car.model.RolePo;
import com.sbkj.car.model.UserPo;
import com.sbkj.car.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 16:30
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * @param username 用户名
     * @return com.sbkj.car.model.UserPo
     * @Description: 根据用户名查询用户
     * @Author Zangdy
     * @CreateTime 2019/4/22 17:31
     */
    @Override
    public UserPo findUserByName(String username) {
        UserPo userPo = userMapper.findUserByName(username);
        if (userPo != null) {
            List<RolePo> roleList = userMapper.findRoleByUserId(userPo.getId());
            userPo.setRoles(roleList);
            return userPo;
        }
        return null;
    }

    /**
     * @param username
     * @return
     * @Description: 验证用户有没有该权限
     * @Author Zangdy
     * @CreateTime 2019/4/26 17:04
     */
    @Override
    public boolean verifyRightsByUsername(String username, String url) {
        Map<String,String> map = new HashMap<>(16);
        map.put("username",username);
        map.put("url",url);
        if (userMapper.verifyRightsByUsername(map) != null) {
            return true;
        }
        return false;
    }
}
