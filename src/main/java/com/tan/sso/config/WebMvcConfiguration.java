package com.tan.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tan.sso.interceptors.AuthInterceptor;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
 
	@Autowired
	AuthInterceptor authInterceptor;
	
	@Override
    protected void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error", "/index.html", "/assets/**");
        super.addInterceptors(registry);
    }
 
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {//WebMvcConfigurationSupport 使自动配置的静态资源路径失效
        registry.addResourceHandler("/**")
        		.addResourceLocations("classpath:/static/")
        		.addResourceLocations("classpath:/templates/")
        		.addResourceLocations("classpath:/resources/")
        		.addResourceLocations("classpath:/");
        super.addResourceHandlers(registry);
    }
}

//@Configuration
//public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
//	@Autowired
//	AuthInterceptor authInterceptor;
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error", "/index.html", "/assets/js/*");
//		super.addInterceptors(registry);
//	}
//}
