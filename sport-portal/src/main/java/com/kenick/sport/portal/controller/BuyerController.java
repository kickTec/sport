package com.kenick.sport.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @RequestMapping("/trueBuy")
    public String trueBuy(String orderInfo, HttpServletRequest request, HttpServletResponse response){
        System.out.println("orderInfo:"+orderInfo);
        return "order";
    }
}
