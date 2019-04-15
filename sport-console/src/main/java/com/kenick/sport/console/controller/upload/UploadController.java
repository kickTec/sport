package com.kenick.sport.console.controller.upload;

import com.kenick.sport.common.utils.SaveFileUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("upload")
public class UploadController {
    @RequestMapping("uploadPic.do")
    public void uploadPic(MultipartFile pic, HttpServletRequest request, HttpServletResponse response){
        try {
            // 文件保存到本地
            String imgUrl = SaveFileUtil.saveFileToLocal(pic, request.getSession().getServletContext().getRealPath("/"));

            // 响应
            PrintWriter writer = response.getWriter();
            writer.write(imgUrl);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // kindeditor上传
    @RequestMapping("uploadFck.do")
    public void uploadFck(MultipartFile uploadFile, HttpServletRequest request, HttpServletResponse response){
        try {
            // 图片保存到本地
            String imgUrl = SaveFileUtil.saveFileToLocal(uploadFile, request.getSession().getServletContext().getRealPath("/"));
            String basePath = request.getContextPath();
            imgUrl = basePath+"/"+imgUrl;

            // 响应上传结果
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url",imgUrl); // 固定
            jsonObject.put("error",0); // 固定
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(jsonObject.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
