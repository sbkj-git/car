package com.sbkj.car.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 响应信息实体类
 * @Author: 臧东运
 * @CreateTime: 2019/4/15 16:40
 */
@Data
public class ResponseBody<T> implements Serializable{
    /**
     * 响应状态
     */
    private String status;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T result;
    /**
     * 响应时的唯一标志
     */
    private String uuid;
}
