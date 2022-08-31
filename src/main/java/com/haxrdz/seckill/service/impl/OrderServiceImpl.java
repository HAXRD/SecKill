package com.haxrdz.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haxrdz.seckill.exception.GlobalException;
import com.haxrdz.seckill.mapper.OrderMapper;
import com.haxrdz.seckill.pojo.*;
import com.haxrdz.seckill.service.IGoodsService;
import com.haxrdz.seckill.service.IOrderService;
import com.haxrdz.seckill.service.ISeckillGoodsService;
import com.haxrdz.seckill.service.ISeckillOrderService;
import com.haxrdz.seckill.vo.GoodsVo;
import com.haxrdz.seckill.vo.OrderDetailVo;
import com.haxrdz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-16
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        // 商品减库存
        Goods stockGoods = goodsService.getOne(new QueryWrapper<Goods>().eq("id", goods.getId()));
        stockGoods.setGoodsStock(stockGoods.getGoodsStock() - 1);
//        goodsService.updateById(stockGoods);
        boolean goodsResult = goodsService.update(
                new UpdateWrapper<Goods>().
                        setSql("stock_count = stock_count - 1").
                        eq("goods_id", goods.getId()).
                        gt("stock_count", 0));
        if (!goodsResult) {
            return null;
        }
        // 秒杀商品减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillGoods);
        boolean seckillGoodsResult = seckillGoodsService.update(
                new UpdateWrapper<SeckillGoods>().
                        setSql("stock_count = stock_count - 1").
                        eq("goods_id", goods.getId()).
                        gt("stock_count", 0));
        if (!seckillGoodsResult) {
            return null;
        }
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);

        redisTemplate.opsForValue().set("order:" + user.getId() + ":"+goods.getId(), seckillOrder);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXISTS);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoods(goodsVo);
        System.out.println(detail);
        return detail;
    }
}
