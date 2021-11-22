package com.hungry.taskmanager.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

//@Configuration
//public class WebMvcConfig {
//    private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(){
//        return builder -> {
//            builder.simpleDateFormat(dateTimeFormat);
//            builder.timeZone(TimeZone.getTimeZone("GMT+19:00"));
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//
//        };
//    }
//}
