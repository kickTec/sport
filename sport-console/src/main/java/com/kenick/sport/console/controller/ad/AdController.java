package com.kenick.sport.console.controller.ad;

import com.kenick.sport.console.controller.BaseController;
import com.kenick.sport.pojo.ad.Advertising;
import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.service.product.AdService;
import com.kenick.sport.service.product.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/ad")
public class AdController extends BaseController {
    @Resource
    private AdService adService;

    @Resource
    private PositionService positionService;

    @RequestMapping("/list.do")
    public String list(Long positionId, Model model){
        List<Advertising> adList = adService.selectAdListByPositionId(positionId);
        model.addAttribute("adList",adList);
        model.addAttribute("positionId",positionId);
        return "ad/list";
    }

    @RequestMapping("/add.do")
    public String add(Long positionId,Model model){
        Position position = positionService.selectPositonById(positionId);
        model.addAttribute("position",position);
        return "ad/add";
    }

    @RequestMapping("/addSubmit.do")
    public String addSubmit(Long positionId,String title,String url,Integer width,Integer height,
                            MultipartFile pic, HttpServletRequest request){
        try {
            String imgUrl = super.savePic(pic, request);
            Advertising advertising = new Advertising();
            advertising.setPositionId(positionId);
            advertising.setTitle(title);
            advertising.setUrl(url);
            advertising.setWidth(width);
            advertising.setHeight(height);
            advertising.setPicture(imgUrl);
            adService.insertAd(advertising);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:list.do?positionId="+positionId;
    }
}