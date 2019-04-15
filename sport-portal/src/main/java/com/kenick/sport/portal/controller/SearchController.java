package com.kenick.sport.portal.controller;

import com.kenick.sport.pojo.page.Pagination;
import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.service.CmsService;
import com.kenick.sport.service.product.BrandService;
import com.kenick.sport.service.product.ProductService;
import com.kenick.sport.service.product.SolrService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class SearchController {
    @Resource
    private BrandService brandService;

    @Resource
    private SolrService solrService;

    @Resource
    private ProductService productService;

    @Resource
    private CmsService cmsService;

    @RequestMapping("/Search")
    public String search(String keyword,String brandId,String price,Integer pageNo, Model model){
        Pagination pagination = null;
        try {
            if(pageNo == null || pageNo < 1){
                pageNo = 1;
            }
            pagination = solrService.selectProductListFromSolr(keyword,brandId,price,pageNo);
            pagination.setPageNo(pageNo);
            pagination.pageView("/Search","keyword="+keyword+"&brandId="+brandId+"&price="+price);

            List<Brand> brandList = brandService.selectBrandListFromRedis();
            if(brandList != null){
                model.addAttribute("brands",brandList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 查询条件
        Map<String, String> queryMap = new HashMap<>();

        if(brandId != null && !"".equals(brandId)){
            model.addAttribute("brandId",brandId);
            String brandName = brandService.selectBrandNameFromRedis(Long.parseLong(brandId));
            model.addAttribute("brandName",brandName);
            queryMap.put("品牌", brandName);
        }
        if(price != null && !"".equals(price)){
            model.addAttribute("price",price);
            if(price.contains("-")){
                queryMap.put("价格:",price);
            }else{
                queryMap.put("价格:",price+"以上");
            }
        }
        if(keyword != null && !"".equals(keyword)){
            model.addAttribute("keyword",keyword);
        }
        if(pagination != null){
            model.addAttribute("pagination",pagination);
        }
        model.addAttribute("map",queryMap);
        return "search";
    }

    @RequestMapping("/product/detail")
    public String productDetail(Long id, Model model){
        // 商品
        Product product = cmsService.selectProductById(id);
        if(product != null){
            model.addAttribute("product", product);
        }

        // 库存
        List<Sku> skus = cmsService.selectSkuListByProductIdAnStockMoreThanZero(id);
        model.addAttribute("skus", skus);

        // 颜色
        Set<Color> colors = new HashSet<>();
        for(Sku sku:skus){
            colors.add(sku.getColor());
        }
        model.addAttribute("colors", colors);
        return "product";
    }
}