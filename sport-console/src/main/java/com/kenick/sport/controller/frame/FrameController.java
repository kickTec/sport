package com.kenick.sport.controller.frame;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/frame")
public class FrameController {

    @RequestMapping("product_main.do")
    public String productMain(){
        return "frame/product_main";
    }

    @RequestMapping("product_left.do")
    public String productLeft(){
        return "frame/product_left";
    }
}
