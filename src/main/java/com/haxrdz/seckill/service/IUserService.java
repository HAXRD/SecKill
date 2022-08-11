package com.haxrdz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.vo.LoginVo;
import com.haxrdz.seckill.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-09
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo);
}
