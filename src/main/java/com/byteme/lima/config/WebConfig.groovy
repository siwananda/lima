package com.byteme.lima.config

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.view.InternalResourceViewResolver

@Configuration
@ComponentScan('com.byteme.lima')
@EnableWebMvc
@EnableAutoConfiguration
class WebConfig extends WebMvcConfigurerAdapter {
    static final Log LOG = LogFactory.getLog(WebConfig.class)

    @Autowired Environment env;

    @Override
    void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/static/");
    }

    @Override
    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*@Bean
    static InternalResourceViewResolver internalResourceViewResolver() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver()
        viewResolver.setPrefix("/WEB-INF/views/")
        viewResolver.setSuffix(".jsp")

        viewResolver
    }*/
}
