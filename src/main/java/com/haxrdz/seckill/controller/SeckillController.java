package com.haxrdz.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haxrdz.seckill.pojo.Order;
import com.haxrdz.seckill.pojo.SeckillOrder;
import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IGoodsService;
import com.haxrdz.seckill.service.IOrderService;
import com.haxrdz.seckill.service.ISeckillGoodsService;
import com.haxrdz.seckill.service.ISeckillOrderService;
import com.haxrdz.seckill.vo.GoodsVo;
import com.haxrdz.seckill.vo.RespBean;
import com.haxrdz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * Mac优化前QPS：550
     * 缓存QPS:600
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSeckill2(Model model, User user, Long goodsId) {
        if (user == null) {
            return "/login";
        }
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return "seckillFail";
        }
        // 判断订单是否重复
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATED_ORDER);
            return "seckillFail";
        }
        Order order = orderService.seckill(user, goods);
        model.addAttribute("order", order);
        model.addAttribute("goods", goods);
        return "orderDetail";
    }

    /**
     * Mac优化前QPS：550
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goods.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 判断订单是否重复
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);


        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEATED_ORDER);
            return RespBean.error(RespBeanEnum.REPEATED_ORDER);
        }
        Order order = orderService.seckill(user, goods);
        return RespBean.success(order);
    }
}
