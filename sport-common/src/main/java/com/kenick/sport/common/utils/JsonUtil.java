package com.kenick.sport.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.StringWriter;

public class JsonUtil {
    /**
     *  将对象转换为json串，可通过@JsonProperty替换json字符串key名称
     * @param object 带转换的对象
     * @return json字符串
     * @throws Exception 异常
     */
    public static String parseObjectToJson(Object object){
        StringWriter stringWriter = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 去掉值为空的key
            stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter,object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.setName("tom");
        demo.setAge(10);

        String json = parseObjectToJson(demo);
        System.out.println(json);
    }

    private static class Demo{
        @JsonProperty("name1")
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
