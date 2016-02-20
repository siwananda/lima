package com.byteme.lima.config

import com.byteme.lima.Application
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@ComponentScan(basePackageClasses = Application.class)
class AppConfig extends WebMvcConfigurerAdapter {

}