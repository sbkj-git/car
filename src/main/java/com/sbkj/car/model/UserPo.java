package com.sbkj.car.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description: 用户实体类
 * @Author: 臧东运
 * @CreateTime: 2019/4/17 14:29
 */
@Getter
@Setter
public class UserPo {
    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
     **/
    private Long id;
    /**
     * username` varchar(15) NOT NULL COMMENT '登录名',
     */
    private String username;
    /**
     * `name` varchar(10) DEFAULT NULL COMMENT '昵称',
     */
    private String name;
    /**
     * `password` varchar(32) NOT NULL COMMENT '密码(MD5加密)',
     */
    private String password;
    /**
     * `create_time` varchar(20) NOT NULL COMMENT '创建时间',
     */
    private Date createTime;
    /**
     * `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 角色列表
     **/
    private List<RolePo> roles;
}
