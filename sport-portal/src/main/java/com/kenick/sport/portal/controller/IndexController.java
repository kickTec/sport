package com.kenick.sport.portal.controller;

import com.kenick.sport.service.product.AdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class IndexController {
    @Resource
    private AdService adService;

    @RequestMapping("/")
    public String index(Model model){
        String adJson = adService.selectAdListByPositionIdForPortal(89L);
        model.addAttribute("adJson",adJson);
        return "index";
    }
}
