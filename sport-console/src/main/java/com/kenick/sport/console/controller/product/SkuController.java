package com.kenick.sport.console.controller.product;

import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.service.product.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/sku")
public class SkuController {

    @Resource
    SkuService skuService;

    @RequestMapping("/list.do")
    public String list(Long productId, Model model,Integer pageNo,Integer pageSize,
                       String queryProductName, String queryBrandId, String queryIsShow){
        List<Sku> skuList = skuService.selectSkuAndColorByProductId(productId);

        if(queryProductName != null){
            model.addAttribute("queryProductName",queryProductName);
        }
        if(queryBrandId != null){
            model.addAttribute("queryBrandId",queryBrandId);
        }
        if(queryIsShow != null){
            model.addAttribute("queryIsShow",queryIsShow);
        }
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("skuList",skuList);
        return "sku/list";
    }

    @RequestMapping("/update.do")
    public void update(Long skuId,Float marketPrice,Float price,Integer stock,Integer upperLimit,Float deliveFee,
        HttpServletResponse response){
        try {
            Sku sku = new Sku();
            sku.setId(skuId);
            sku.setMarketPrice(marketPrice);
            sku.setPrice(price);
            sku.setStock(stock);
            sku.setUpperLimit(upperLimit);
            sku.setDeliveFee(deliveFee);
            Integer ret = skuService.updateSku(sku);

            if(ret > 0){
                PrintWriter writer = response.getWriter();
                writer.write(1+"");
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}