package com.haxrdz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haxrdz.seckill.exception.GlobalException;
import com.haxrdz.seckill.mapper.UserMapper;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IUserService;
import com.haxrdz.seckill.utils.CookieUtil;
import com.haxrdz.seckill.utils.MD5Utils;
import com.haxrdz.seckill.utils.UUIDUtil;
import com.haxrdz.seckill.vo.LoginVo;
import com.haxrdz.seckill.vo.RespBean;
import com.haxrdz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 参数校验
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }


        // 根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 判断密码是否正确
        if (!MD5Utils.serverStr2DBStr(password, user.getSalt()).equals(user.getPassword())) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 生成Cookie
        String ticket = UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket, user);
        // 将用户信息存入redis中
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success();
    }

    /**
     * 根据Cookie获取User
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
