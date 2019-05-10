package com.sbkj.car.model;

import lombok.Data;

/**
 * @Description: 菜单实体类
 * @Author: 臧东运
 * @CreateTime: 2019/5/5 10:59
 */
@Data
public class MenuPo {
    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单表主键',
     */
    private Integer id;

    /**
     * `name` varchar(15) DEFAULT NULL COMMENT '菜单名称',
     */
    private Integer name;

    /**
     * `parent_id` int(11) DEFAULT NULL COMMENT '父级菜单id',
     */
    private Integer parentId;

    /**
     * `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
     */
    private String createTime;

    /**
     * `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
     */
    private String updateTime;

    /**
     * `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
     */
    private Integer createUserId;

    /**
     * `update_user_id` int(11) DEFAULT NULL COMMENT '更新人id',
     */
    private Integer updateUserId;
}
