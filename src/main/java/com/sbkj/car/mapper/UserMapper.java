package com.sbkj.car.mapper;

import com.sbkj.car.model.RightsPo;
import com.sbkj.car.model.RolePo;
import com.sbkj.car.model.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 16:38
 */
@Repository
@Mapper
public interface UserMapper {
    /**
     * 根据用户名查询user
     * @param username
     * @return
     */
    UserPo findUserByName1(String username);
    UserPo findUserByName(String username);

    /**
     * 根据用户id查询角色集合
     * @param userId
     * @return
     */
    List<RolePo> findRoleByUserId(Long userId);

    /**
     * 根据角色id查询权限集合
     * @param roleId
     * @return
     */
    List<RightsPo> findRightsByRoleId(Integer roleId);

    /**
     * 验证该用户是否有访问该地址的权限
     * @param map
     * @return
     */
    RightsPo verifyRightsByUsername(Map<String,String> map);
}
