package com.haxrdz.seckill.controller;

import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IGoodsService;
import com.haxrdz.seckill.service.IUserService;
import com.haxrdz.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;


    /**
     * 获取商品列表
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/toList")
//    public String toList(HttpSession session, Model model, @CookieValue("userTicket") String ticket) {
    public String toList(Model model, User user) {
//        if (StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
////        User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if (user == null) {
//            return "login";
//        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        // 秒杀状态
        int seckillStatus = 0;
        int remainSeconds = 0;
        // 秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = ((int)((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }
}
