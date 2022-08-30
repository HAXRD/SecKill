package com.haxrdz.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    // 通用
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),
    // 登录模块 5002
    LOGIN_ERROR(500210, "用户名或密码不正确"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常"),
    MOBILE_NOT_EXISTS(500513, "手机号码不存在"),
    PASSWORD_UPDATE_FAILURE(500514, "更新密码失败"),
    SESSION_ERROR(500515, "用户不存在"),
    // 秒杀模块 5005
    EMPTY_STOCK(500500, "库存不足"),
    REPEATED_ORDER(500501, "该商品每人限购一件"),
    // 订单模块 5003
    ORDER_NOT_EXISTS(500300, "该订单不存在")
    ;

    private final Integer code;
    private final String message;
}
