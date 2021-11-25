package com.hungry.taskmanager.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString()+ "/upload/";
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+path);
        System.out.println("file:" + path);
    }
}
