package com.codmain.orderapi.config;

import java.time.format.DateTimeFormatter;

import com.codmain.orderapi.converters.OrderConverter;
import com.codmain.orderapi.converters.ProductConverter;
import com.codmain.orderapi.converters.UserConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ConverterConfig {

    @Value("${config.datetimeFormat}")
    private String dateTimeFormat;

    @Bean
    public ProductConverter getProductConverter() {
        return new ProductConverter();
    }

    @Bean
    public OrderConverter getOrderConverter() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateTimeFormat);
        return new OrderConverter(format, getProductConverter(), getUserConverter());
    }

    @Bean
    public UserConverter getUserConverter() {
        return new UserConverter();
    }
}
