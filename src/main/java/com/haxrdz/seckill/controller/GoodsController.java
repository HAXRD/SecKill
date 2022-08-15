package com.haxrdz.seckill.controller;

import com.haxrdz.seckill.pojo.User;
import com.haxrdz.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

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
        return "goodsList";
    }
}
