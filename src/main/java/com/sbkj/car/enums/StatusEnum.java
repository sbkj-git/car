package com.sbkj.car.enums;

/**
 * @Description:
 * @Author: 臧东运
 * @CreateTime: 2019/4/22 10:53
 */
public enum  StatusEnum {

    /**未登录**/
    UNLOGIN("0000","未登录"),

    /**
     * 状态 02 开头为成功
     * */
    LOGINSUCCESS("0201","登陆成功"),
    LOGOUTSUCCESS("0202","注销成功"),
    UPLOADSUCCESS("0203","文件上传成功"),


    /**
     * 状态 04 开头为失败
     */
    IPBLOCKED("0400","因非法请求,已被禁止访问,请稍后重试!"),
    ACCESSDENIED("0401","无权限访问"),
    /**
     * 登录失败
     */
    USERNAMEISNULL("0402","用户名不能为空!"),
    PASSWORDISNULL("0402","密码不能为空!"),
    USERNAMEERROR("0402","用户名不存在!"),
    PASSWORDERROR("0402","密码错误!"),
    /**
     * 认证失败
     */
    NOHEADER("0403","缺少头信息"),
    TOKENOUTTIME("0403","token认证已过期"),
    APPTIMESIGNISNULL("0403","sign认证参数为空"),
    SIGNUNTRUE("0403","sign参数不正确"),
    SIGNOUTTIME("0403","sign签名已过期"),
    UUIDISNULL("0403","缺少uuid"),

    ERROR("0499","服务器异常,请稍后重试。如有问题,请联系管理员"),
    CUSTOM("0500","自定义异常");

    private String status;
    private String message;

    StatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
