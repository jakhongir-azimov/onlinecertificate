package com.company.certificate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/images/**",
                "/css/**",
                "/vendor/**",
                "/js/**").addResourceLocations(
                "classpath:/static/images/",
                "classpath:/static/css/",
                "classpath:/static/vendor/",
                "classpath:/static/js/"
        );
    }
}
