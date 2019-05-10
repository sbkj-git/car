package com.sbkj.car.component.exception;

/**
 * @Description: 公共异常类
 * @Author: 臧东运
 * @CreateTime: 2019/4/15 13:41
 */
public class SbException extends Exception {

    private String message;

    public SbException(String message) {
        this.message=message;
    }


    @Override
    public String getMessage() {
        return message;
    }
}
