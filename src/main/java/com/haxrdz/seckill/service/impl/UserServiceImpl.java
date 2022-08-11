package com.haxrdz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haxrdz.seckill.mapper.UserMapper;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IUserService;
import com.haxrdz.seckill.utils.MD5Utils;
import com.haxrdz.seckill.utils.ValidatorUtil;
import com.haxrdz.seckill.vo.LoginVo;
import com.haxrdz.seckill.vo.RespBean;
import com.haxrdz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.sql.SQLOutput;

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

    @Override
    public RespBean doLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if (!ValidatorUtil.isMobile(mobile)) {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }

        // 根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if (!MD5Utils.serverStr2DBStr(password, user.getSalt()).equals(user.getPassword())) {
            System.out.println("type2");
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        return RespBean.success();
    }
}
