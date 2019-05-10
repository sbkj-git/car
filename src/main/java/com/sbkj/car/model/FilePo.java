package com.sbkj.car.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/29 13:35
 */
@Getter
@Setter
public class FilePo {
    /**
     * `id` bigint(20) NOT NULL COMMENT '文件表主键',
     **/
    private Long id;
    /**
     * `name` varchar(35) DEFAULT NULL COMMENT '文件名称',
     **/
    private String name;
    /**
     * `url` varchar(50) NOT NULL COMMENT '文件路径',
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
