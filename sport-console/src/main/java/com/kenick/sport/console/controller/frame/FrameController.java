package com.kenick.sport.console.controller.frame;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frame")
public class FrameController {

    @RequestMapping("/product_main.do")
    public String productMain(){
        return "frame/product_main";
    }

    @RequestMapping("/product_left.do")
    public String productLeft(){
        return "frame/product_left";
    }

    @RequestMapping("/ad_main.do")
    public String adMain(){
        return "frame/ad_main";
    }

    @RequestMapping("/ad_left.do")
    public String adLeft(){
        return "frame/ad_left";
    }
}
