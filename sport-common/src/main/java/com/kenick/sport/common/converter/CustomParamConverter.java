package com.kenick.sport.common.converter;

import org.springframework.core.convert.converter.Converter;

public class CustomParamConverter implements Converter<String,String> {
    @Override
    public String convert(String source) {
        if(source != null && !"".equals(source)){
            source = source.trim();
            if(!"".equals(source)){
                return source;
            }
        }
        return source;
    }
}