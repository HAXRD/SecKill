package com.haxrdz.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haxrdz.seckill.pojo.Goods;
import com.haxrdz.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author HAXRD
 * @since 2022-08-16
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
