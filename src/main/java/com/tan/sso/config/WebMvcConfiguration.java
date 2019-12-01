package com.tan.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tan.sso.interceptors.AuthInterceptor;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error", "/index.html");
		super.addInterceptors(registry);
	}
}
