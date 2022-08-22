package com.haxrdz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haxrdz.seckill.pojo.Goods;
import com.haxrdz.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-16
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
