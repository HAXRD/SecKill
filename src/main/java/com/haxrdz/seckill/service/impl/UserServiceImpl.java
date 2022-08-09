package com.haxrdz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haxrdz.seckill.mapper.UserMapper;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IUserService;
import org.springframework.stereotype.Service;

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

}
