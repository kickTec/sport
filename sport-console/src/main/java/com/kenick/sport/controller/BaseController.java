package com.kenick.sport.controller;

import com.kenick.sport.service.upload.UploadService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BaseController {
    private UploadService uploadService;

    protected void setUploadService(UploadService uploadService){
        this.uploadService = uploadService;
    }

    /**
     *  存储图片资源
     * @param pic 图片
     * @param request 请求
     * @return 图片url
     * @throws IOException 文件保存异常
     */
    protected String savePic(MultipartFile pic, HttpServletRequest request) throws IOException {
        String imgUrl = null;
        String originalFilename = pic.getOriginalFilename();
        // 图片保存到服务器
        if (!"".equals(originalFilename)) { // 上传文件不为空
            if (uploadService.fastDFSIsConn()) { // 分布式文件系统正常
                imgUrl = uploadService.fastDFSUploadFile(pic.getBytes(), originalFilename);
            } else {
                String realPath = request.getSession().getServletContext().getRealPath("/");
                imgUrl = "/upload/" + System.currentTimeMillis() + "_" +
                        UUID.randomUUID().toString().replaceAll("-", "")
                        + originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilePath = realPath + imgUrl;
                pic.transferTo(new File(newFilePath));
            }
        }
        return imgUrl;
    }
}
