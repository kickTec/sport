package com.kenick.sport.controller.upload;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Controller
@RequestMapping("upload")
public class UploadController {
    @RequestMapping("uploadPic.do")
    public void uploadPic(MultipartFile pic, HttpServletRequest request, HttpServletResponse response){
        try {
            // 改名
            String fileName = pic.getOriginalFilename();
            String extName = FilenameUtils.getExtension(fileName);
            String uuid = UUID.randomUUID().toString().replace("-","");
            String newName = uuid + "." + extName;

            // 附件上传
            String imgUrl = "\\upload\\" + newName;
            String realPath = request.getServletContext().getRealPath("");
            String realUrlPath = realPath + imgUrl;
            pic.transferTo(new File(realUrlPath));

            // 响应
            PrintWriter writer = response.getWriter();
            writer.write(imgUrl);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
