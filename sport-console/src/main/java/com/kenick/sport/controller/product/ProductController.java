package com.kenick.sport.controller.product;

import com.kenick.sport.PaginationUtil;
import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.service.product.BrandService;
import com.kenick.sport.service.product.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Resource
    private BrandService brandService;

    @RequestMapping("list.do")
    public String list(String productName, Long brandId, Boolean isShow, Integer pageNo,
                       Integer pageSize, Model model, HttpServletRequest request){
        if(productName != null){
            if("GET".equals(request.getMethod())){
                productName = new String(productName.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            }
        }

        List<Brand> brandList = brandService.selectBrandListByQueryNoPage(new BrandQuery());
        List<Product> productList = productService.selectProductByQuery(productName, brandId, isShow, pageNo, pageSize);
        model.addAttribute("productList", productList);
        model.addAttribute("brandList", brandList);

        // 查询条件回显
        if(productName != null){
            model.addAttribute("queryProductName",productName);
        }
        if(brandId != null){
            model.addAttribute("queryBrandId",brandId);
        }
        if(isShow != null){
            model.addAttribute("queryIsShow",isShow?1:0);
        }
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = 5;
        }

        // 分页相关
        Integer productTotalSize = productService.getProductTotalSize(productName, brandId, isShow);
        PaginationUtil paginationUtil = new PaginationUtil();
        paginationUtil.setPageNo(pageNo);
        paginationUtil.setPageSize(pageSize);
        paginationUtil.setTotalSize(productTotalSize);

        Integer prePage = paginationUtil.getPrePage();
        Integer nextPage = paginationUtil.getNextPage();
        Integer lastPage = paginationUtil.getLastPage();
        List<String> centerPageList = paginationUtil.getCenterPageList(5);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("prePage",prePage);
        model.addAttribute("nextPage",nextPage);
        model.addAttribute("lastPage",lastPage);
        model.addAttribute("centerPageList",centerPageList);
        return "product/list";
    }

    @RequestMapping("add.do")
    public String add(String listProductName,Integer listBrandId,Boolean listIsShow,
                      Integer pageNo,Integer pageSize,Model model){

        if(listProductName != null){
            model.addAttribute("listProductName",listProductName);
        }
        if(listBrandId != null){
            model.addAttribute("listBrandId",listBrandId);
        }
        if(listIsShow != null){
            model.addAttribute("listIsShow",listIsShow);
        }
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        return "product/add";
    }
}