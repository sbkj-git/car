package com.sbkj.car.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 权限集合
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 17:14
 */
@Getter
@Setter
public class RightsPo {
    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限表主键',
     **/
    private Integer id;
    /**
     * `name` varchar(15) DEFAULT NULL COMMENT '权限名称',
     **/
    private String name;
    /**
     * `url` varchar(20) DEFAULT NULL COMMENT '权限路径',
     **/
    private String url;
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
}
