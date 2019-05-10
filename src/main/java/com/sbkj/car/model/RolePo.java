package com.sbkj.car.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @Description: 角色集合
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 17:12
 */
@Getter
@Setter
public class RolePo {
    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色表主键',
     **/
    private Integer id;
    /**
     * `name` varchar(15) DEFAULT NULL COMMENT '角色名称',
     **/
    private String name;
    /**
     * `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
     **/
    private Date createTime;
    /**
     * `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
     **/
    private Date updateTime;
    /**
     * `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
     **/
    private String createUserId;
    /**
     * `update_user_id` int(11) DEFAULT NULL COMMENT '更新人id',
     **/
    private String updateUserId;
    /**
     * 权限集合
     */
    private List<RightsPo> rightsList;
}
