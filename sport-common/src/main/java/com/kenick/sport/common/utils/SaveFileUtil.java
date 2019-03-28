package com.kenick.sport.common.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 *  文件保存相关类
 */
public class SaveFileUtil {

    /**
     *  将文件保存到当前项目下
     * @param file 文件
     * @param projectRealPath 当前项目绝对地址 如 c:\\workspace\\console\\webapp
     * @return 项目下文件路径 \\upload\\xxx,绝对地址为c:\\workspace\\console\\webapp\\upload\\xxx.
     */
    public static String saveFileToLocal(MultipartFile file, String projectRealPath){
        String imgUrl = "";
        try {
            // 改名
            String fileName = file.getOriginalFilename();
            String extName = FilenameUtils.getExtension(fileName);
            String uuid = UUID.randomUUID().toString().replace("-","");
            String newName = uuid + "." + extName;

            // 附件上传
            imgUrl = "\\upload\\" + newName;
            String realUrlPath = projectRealPath + "\\" + imgUrl;
            file.transferTo(new File(realUrlPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
}
