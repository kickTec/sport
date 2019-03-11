package com.kenick.sport.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("product")
public class ProductController {
    @RequestMapping("list.do")
    public String list(){
        return "product/list";
    }
}
