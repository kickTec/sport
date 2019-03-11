package com.kenick.sport.controller.brand;

import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;
import com.kenick.sport.service.product.BrandService;
import com.kenick.sport.service.upload.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {
    private final static Integer MIN_PAGESIZE = 5;

    @Resource
    private BrandService brandService;

    @Resource
    private UploadService uploadService;

    @RequestMapping("/add.do")
    public String add(String listName, Integer listIsDisplay, Integer pageNo, Integer pageSize,
                      Model model,HttpServletRequest request){
        if(listName != null){
            if("GET".equals(request.getMethod())){
                listName = new String(listName.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            }
        }

        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("listName", listName);
        model.addAttribute("listIsDisplay", listIsDisplay);
        return "brand/add";
    }

    @RequestMapping("/addSubmit.do")
    public String addSubmit(String listName, Integer listIsDisplay, Integer pageNo, Integer pageSize,
                            String name,MultipartFile pic,String description,Integer sort,Short isDisplay,
                            Model model){
        try {
            String imgUrl = null;
            String originalFilename = pic.getOriginalFilename();
            if(!"".equals(originalFilename)){
                imgUrl = uploadService.fastDFSUploadFile(pic.getBytes(), originalFilename);
            }

            // 保存品牌信息
            Brand brand = new Brand();
            brand.setName(name);
            brand.setDescription(description);
            brand.setSort(sort);
            brand.setIsDisplay(isDisplay);
            brand.setImgUrl(imgUrl);
            brandService.addBrand(brand);

            // 页面重定向
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("name", listName);
            model.addAttribute("isDisplay", listIsDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/brand/list.do";
    }

    @RequestMapping("/list.do")
    public String list(String name, Integer isDisplay, Integer pageNo, Integer pageSize, Model model,HttpServletRequest request){
        try {
            if(pageNo == null){
                pageNo = 1;
            }
            if(pageSize == null){
                pageSize = MIN_PAGESIZE;
            }
            if(name != null){
                String method = request.getMethod();
                if("GET".equals(method)){
                    name = new String(name.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
                }
            }
            BrandQuery brandQuery = new BrandQuery();
            brandQuery.setName(name);
            if(isDisplay != null && isDisplay != 2){
                brandQuery.setIsDisplay(isDisplay.shortValue());
            }

            // 结果集
            List<Brand> brandList = brandService.selectBrandListByQueryAndPage(brandQuery,pageNo,pageSize);
            // 总条目
            Integer totalNum = brandService.selectBrandTotalByQuery(brandQuery);

            model.addAttribute("queryName", name); // 查询条件
            model.addAttribute("queryIsDisplay", isDisplay); // 查询条件
            model.addAttribute("brandList", brandList); // 查询结果集

            // 分页相关
            model.addAttribute("pageNo", pageNo); // 当前页码
            model.addAttribute("pageSize", pageSize); // 每页大小
            model.addAttribute("totalNum", totalNum); // 总条目
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "brand/list";
    }

    @RequestMapping("/edit.do")
    public String edit(Integer brandId,String name,String isDisplay,Integer listPageNo,Integer listPageSize,
                       Model model,HttpServletRequest request){
        if(listPageNo == null){
            listPageNo = 1;
        }
        if(listPageSize == null){
            listPageSize = MIN_PAGESIZE;
        }
        if(name != null){
            String method = request.getMethod();
            if("GET".equals(method)){
                name = new String(name.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            }
        }

        Brand brand = brandService.selectBrandById(brandId+"");

        model.addAttribute("name", name);
        model.addAttribute("isDisplay", isDisplay);
        model.addAttribute("listPageNo", listPageNo);
        model.addAttribute("listPageSize", listPageSize);
        model.addAttribute("brand", brand);
        return "brand/edit";
    }

    @RequestMapping("/editSubmit.do")
    public String editSubmit(Long brandId, String name, String description, Integer sort, Short isDisplay,
                           String pageNo,String pageSize,String listName,String listIsDisplay,
                             MultipartFile pic, Model model){
        try {
            String imgUrl = null;
            String originalFilename = pic.getOriginalFilename();

            // 图片保存到服务器
//            if(!"".equals(originalFilename)){ // 上传文件不为空
//                String realPath = request.getServletContext().getRealPath("");
//                System.out.println("realPath:"+realPath);
//                imgUrl  = "/upload/"+ System.currentTimeMillis() + "_" +
//                        UUID.randomUUID().toString().replaceAll("-","")
//                        + originalFilename.substring(originalFilename.lastIndexOf("."));
//                String newFilePath = realPath + imgUrl;
//                pic.transferTo(new File(newFilePath));
//            }

            // 图片上传到fastDFS
            if(!"".equals(originalFilename)){
                imgUrl = uploadService.fastDFSUploadFile(pic.getBytes(), originalFilename);
            }

            // 保存修改后的品牌信息
            Brand brand = new Brand();
            brand.setId(brandId);
            brand.setName(name);
            brand.setDescription(description);
            brand.setSort(sort);
            brand.setIsDisplay(isDisplay);
            brand.setImgUrl(imgUrl);
            brandService.updateBrand(brand);

            model.addAttribute("pageNo",pageNo);
            model.addAttribute("pageSize",pageSize);
            model.addAttribute("name",listName);
            model.addAttribute("isDisplay",listIsDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/brand/list.do";
    }

    @RequestMapping("/delete.do")
    public String delete(Integer brandId,Integer listPageNo,Integer listPageSize,String name,Short isDisplay,
                         Model model,HttpServletRequest request){
        if(name != null){
            if("GET".equals(request.getMethod())){
                name = new String(name.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            }
        }

        model.addAttribute("name",name);
        model.addAttribute("isDisplay",isDisplay);
        model.addAttribute("pageNo",listPageNo);
        model.addAttribute("pageSize",listPageSize);

        Boolean ret = brandService.deleteBrand(brandId);
        return "redirect:/brand/list.do";
    }

    @RequestMapping("/deleteList.do")
    public void deleteList(String ids, HttpServletResponse response){
        try {
            String[] idArray = ids.split(",");
            Boolean ret = brandService.deleteBrandByArray(idArray);
            PrintWriter writer = response.getWriter();
            writer.write(ret+"");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}