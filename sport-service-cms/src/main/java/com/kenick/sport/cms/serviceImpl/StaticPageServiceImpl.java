package com.kenick.sport.cms.serviceImpl;

import com.kenick.sport.service.product.StaticPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StaticPageServiceImpl implements StaticPageService, ServletContextAware {

    // 通过set注入 freeMarker配置
    private Configuration configuration;
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer){
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }


    @Override
    public void freeMarkerProductDetail(Map<String, Object> rootMap, String productId) {
        try {
            // 静态页面相对路径
            String pathName = "/freeMarker/html/product/"+productId+".html";
            String realPath = servletContext.getRealPath(pathName);

            // 如果父路径不存在，进行创建
            File file = new File(realPath);
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }

            // 获取freeMarker模板
            Template template = configuration.getTemplate("product.html");
            Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            template.process(rootMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
