package com.kenick.sport.controller.product;

import com.kenick.sport.common.utils.PaginationUtil;
import com.kenick.sport.controller.BaseController;
import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.service.product.BrandService;
import com.kenick.sport.service.product.ColorService;
import com.kenick.sport.service.product.ProductService;
import com.kenick.sport.service.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{
    @Resource
    private ProductService productService;

    @Resource
    private BrandService brandService;

    @Resource
    private ColorService colorService;

    @Autowired
    public void setBaseUploadService(UploadService uploadService){
        super.setUploadService(uploadService);
    }

    @RequestMapping("/list.do")
    public String list(String productName, Long brandId, Boolean isShow,
                       Integer pageNo, Integer pageSize,
                       Model model, HttpServletRequest request){
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

    @RequestMapping("/add.do")
    public String add(String queryProductName,Integer queryBrandId,Boolean queryIsShow,
                      Integer pageNo,Integer pageSize,Model model){
        // 颜色信息
        List<Color> colorList = colorService.selectColorByParentIdNot(0L);
        if(colorList != null){
            model.addAttribute("colorList", colorList);
        }

        // 品牌信息
        List<Brand> brandList = brandService.selectBrandListByQueryNoPage(new BrandQuery());
        if(brandList != null){
            model.addAttribute("brandList", brandList);
        }

        // 返回时定位列表位置
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
        return "product/add";
    }

    @RequestMapping("/addSubmit.do")
    public String addSubmit(Product product, @RequestParam MultipartFile[] pics,
                            String queryProductName,Integer queryBrandId,Boolean queryIsShow,
                            Integer pageNo,Integer pageSize,
                            HttpServletRequest request,Model model){
        // 保存商品信息
        String imgUrl = this.saveProductPics(pics, request);
        product.setImgUrl(imgUrl);
        productService.saveProduct(product);

        // 跳转到list页面
        model.addAttribute("productName",queryProductName);
        model.addAttribute("brandId",queryBrandId);
        model.addAttribute("isShow",queryIsShow);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pageSize",pageSize);
        return "redirect:/product/list.do";
    }

    @RequestMapping("/deleteProduct.do")
    public String deleteProduct(String productId,Model model,
                String queryProductName,Integer queryBrandId,Boolean queryIsShow,
                Integer pageNo,Integer pageSize)
    {
        productService.deleteProductById(Long.parseLong(productId));

        if(queryProductName != null){
            model.addAttribute("queryProductName", queryProductName);
        }
        if(queryBrandId != null){
            model.addAttribute("queryBrandId", queryBrandId);
        }
        if(queryIsShow != null){
            model.addAttribute("queryIsShow", queryIsShow);
        }
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        return "redirect:/product/list.do";
    }

    /**
     *  存储图片资源
     * @param pics 图片
     * @param request 请求
     * @return 图片url
     * @throws IOException 文件保存异常
     */
    private String saveProductPics(MultipartFile[] pics, HttpServletRequest request){
        String imgUrl = "";

        try {
            for(MultipartFile pic:pics){
                imgUrl = imgUrl + super.savePic(pic, request) + ",";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}