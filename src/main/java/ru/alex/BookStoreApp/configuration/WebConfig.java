package ru.alex.BookStoreApp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:static/")
                .setCacheControl(CacheControl.noCache());
        registry.addResourceHandler("/bookImages/**")
                .addResourceLocations("classpath:bookImages/")
                .setCacheControl(CacheControl.noCache());
    }
}
