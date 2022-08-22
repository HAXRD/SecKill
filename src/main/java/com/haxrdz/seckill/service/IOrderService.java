package com.haxrdz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haxrdz.seckill.pojo.Order;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-16
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
