package com.dcx.reggie.config;


import com.dcx.reggie.common.JacksonObjectMapper;
import com.dcx.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.EmptyStackException;
import java.util.List;


@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        log.info("静态资源启动成功！！！");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mc = new MappingJackson2HttpMessageConverter();
        mc.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,mc);
    }


}
