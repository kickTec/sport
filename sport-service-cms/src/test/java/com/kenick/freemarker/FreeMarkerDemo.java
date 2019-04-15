package com.kenick.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMarkerDemo {
    public static void main(String[] args) {
        try {
            System.out.println("Start!");
            // 指定模板位置
            String pathname = FreeMarkerDemo.class.getResource("/").getPath().substring(1);
            File file = new File(pathname);
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(file);

            // 获取模板
            Template template = configuration.getTemplate("hello.html");

            List<User> users = new ArrayList<>();
            User user1 = new User();
            user1.setId("1");
            user1.setName("test1");
            users.add(user1);
            User user2 = new User();
            user2.setId("2");
            user2.setName("test2");
            users.add(user2);

            Map<String, Object> rootMap = new HashMap<>();
            rootMap.put("users",users);

            FileWriter fileWriter = new FileWriter(pathname + "/word.html");
            template.process(rootMap,fileWriter);
            System.out.println("End!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
